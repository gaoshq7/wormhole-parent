package io.github.gaoshq7.wormhole.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.gaoshq7.wormhole.AuditMsg;
import io.github.gaoshq7.wormhole.protocol.AuditObserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.wormhole.impl.AuditObserverImpl
 *
 * @author : gsq
 * @date : 2025-10-15 13:33
 * @note : It's not technology, it's art !
 **/
public class AuditObserverImpl implements AuditObserver {

    protected String hostname;

    protected int port;

    public AuditObserverImpl(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public AuditMsg audit(Integer id) {
        AuditMsg auditMsg = new AuditMsg();
        try {
            String spec = "http://" + hostname + ":" + port + "/history/execution_log/long/" + id;
            URL url = new URL(spec);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                JSONObject entries = JSONUtil.parseObj(response);
                auditMsg.setId(entries.getStr("id"));
                auditMsg.setName(entries.getStr("script"));
                auditMsg.setCommand(entries.getStr("command"));
                auditMsg.setStatus(entries.getStr("status"));
                auditMsg.setCode(entries.getInt("exitCode"));
                auditMsg.setLog(entries.getStr("log"));
                auditMsg.setTime(entries.getStr("startTime"));
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
        return auditMsg;
    }

}
