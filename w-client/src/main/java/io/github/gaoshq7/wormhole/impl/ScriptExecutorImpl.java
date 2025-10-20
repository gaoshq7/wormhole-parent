package io.github.gaoshq7.wormhole.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import handler.SimpleWsReceiver;
import io.github.gaoshq7.wormhole.CommandHandler;
import io.github.gaoshq7.wormhole.protocol.ScriptExecutor;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.wormhole.impl.ScriptExecutorImpl
 *
 * @author : gsq
 * @date : 2025-10-15 13:34
 * @note : It's not technology, it's art !
 **/
public class ScriptExecutorImpl implements ScriptExecutor {

    protected String hostname;

    protected int port;

    public ScriptExecutorImpl(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public Integer execute(String script, CommandHandler handler, String... args) {
        // 执行start接口
        String executorId;
        try {
            executorId = getExecutorId(script, args);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if (handler != null) {
            handler.id(executorId);
        }
        // 执行websocket接口，接受数据, 数据传入handler
        executeWebsocket(executorId, handler);
        // 执行log接口，查询脚本执行状态
        return executeLog(executorId);
    }

    /**
     * @Description : 获取执行id
     * @Param java.lang.String script :
    * @Param java.lang.String... args :
     * @Return : java.lang.String
     * @Author : syu
     * @Date : 2025/10/17
     */
    private String getExecutorId(String script, String... args) throws Exception {
        String executorId;
        try {
            String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
            String spec = "http://" + hostname + ":" + port + "/executions/start";
            URL url = new URL(spec);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setDoOutput(true);

            try (OutputStream outputStream = conn.getOutputStream()) {
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
                // 发送普通字段
                writer.append("--").append(boundary).append("\r\n");
                writer.append("Content-Disposition: form-data; name=\"__script_name\"\r\n\r\n");
                writer.append(script).append("\r\n");
                // 发送 args 数组
                for (String arg : args) {
                    String[] split = arg.split("\\^");
                    String key = split[0];
                    String value = split[1];
                    writer.append("--").append(boundary).append("\r\n");
                    writer.append("Content-Disposition: form-data; name=\"").append(key).append("\"\r\n\r\n");
                    writer.append(value).append("\r\n");
                }
                writer.flush();
            }
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                InputStream stream = conn.getErrorStream();
                StringBuilder response = new StringBuilder();
                if (stream != null) {
                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line).append('\n');
                        }
                    }
                }
                throw new RuntimeException(response.toString());
            }
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                executorId = response.toString();
            }
            conn.disconnect();
        }catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return executorId;
    }

    /**
     * @Description : 执行websocket,查询脚本执行结果
     * @Param java.lang.String executorId :
     * @Param io.github.gaoshq7.wormhole.CommandHandler handler :
     * @Return : void
     * @Author : syu
     * @Date : 2025/10/17
     */
    private void executeWebsocket(String executorId, CommandHandler handler){
        try {
            String str = "ws://" + hostname + ":" + port + "/executions/io/" + executorId;
            URI uri = new URI(str);
            SimpleWsReceiver simpleWsReceiver = new SimpleWsReceiver(uri, handler);
            simpleWsReceiver.connectBlocking();
            simpleWsReceiver.awaitClose();
            // close之后执行cleanup
            cleanup(executorId);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @Description : 执行cleanup，释放资源
     * @Param java.lang.String executorId :
     * @Return : void
     * @Author : syu
     * @Date : 2025/10/17
     */
    private void cleanup(String executorId){
        try {
            String spec = "http://" + hostname + ":" + port + "/executions/cleanup/" + executorId;
            URL url = new URL(spec);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            try (OutputStream outputStream = conn.getOutputStream()) {
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
                writer.flush();
            }
            // 读取响应码，确保请求已经执行
            conn.getResponseCode();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description : 查询日志,获取脚本执行结果码
     * @Param java.lang.String executorId :
     * @Return : java.lang.Integer
     * @Author : syu
     * @Date : 2025/10/17
     */
    private Integer executeLog(String executorId){
        try {
            String spec = "http://" + hostname + ":" + port + "/history/execution_log/long/" + executorId;
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
                return entries.getInt("exitCode");
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
