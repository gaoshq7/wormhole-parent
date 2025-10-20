package io.github.gaoshq7.wormhole.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import handler.SimpleWsReceiver;
import io.github.gaoshq7.wormhole.CommandHandler;
import io.github.gaoshq7.wormhole.protocol.ScriptExecutor;
import okhttp3.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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
    public Integer execute(String script, CommandHandler handler, Map<String, Object> args) {
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
    private String getExecutorId(String script, Map<String, Object> args) throws Exception {
        String url = "http://" + hostname + ":" + port + "/executions/start";
        // 创建 OkHttpClient
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("__script_name", script);
        args.forEach((key, value) -> {
            builder.addFormDataPart(key, value.toString());
        });
        RequestBody requestBody = builder.build();
        // 构造请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        // 执行请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errMsg = response.body() != null ? response.body().string() : "Unknown error";
                throw new RuntimeException(errMsg);
            }
            if (response.body() == null) {
                throw new IOException("响应体为空");
            }
            return response.body().string().trim();
        }
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
