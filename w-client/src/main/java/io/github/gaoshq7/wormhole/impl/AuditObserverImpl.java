package io.github.gaoshq7.wormhole.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.gaoshq7.handler.AuditHttpClient;
import io.github.gaoshq7.wormhole.AuditMsg;
import io.github.gaoshq7.wormhole.protocol.AuditObserver;

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

    private final AuditHttpClient client;

    public AuditObserverImpl(String hostname, int port, String token) {
        this.hostname = hostname;
        this.port = port;
        this.client = new AuditHttpClient(hostname, port, token);
    }

    @Override
    public AuditMsg audit(Integer id) {
        AuditMsg auditMsg = new AuditMsg();
        try {
            String response = client.executeLog(id);
            JSONObject entries = JSONUtil.parseObj(response);
            auditMsg.setId(entries.getStr("id"));
            auditMsg.setName(entries.getStr("script"));
            auditMsg.setCommand(entries.getStr("command"));
            auditMsg.setStatus(entries.getStr("status"));
            auditMsg.setCode(entries.getInt("exitCode"));
            auditMsg.setLog(entries.getStr("log"));
            auditMsg.setTime(entries.getStr("startTime"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return auditMsg;
    }

}
