package io.github.gaoshq7.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.gaoshq7.wormhole.CommandHandler;
import io.github.gaoshq7.wormhole.WsMsgHandler;
import okhttp3.*;

import java.io.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExecutorHttpClient {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    private final String hostname;

    private final int port;

    private final String token;

    public ExecutorHttpClient(String hostname, int port, String token) {
        this.hostname = hostname;
        this.port = port;
        this.token = token;
    }

    /**
     * @Description : 获取执行id
     * @Param java.lang.String script :
     * @Param java.lang.String... args :
     * @Return : java.lang.String
     * @Author : syu
     * @Date : 2025/10/17
     */
    public String getExecutorId(String script, Map<String, Object> args) throws Exception {
        String url = "http://" + hostname + ":" + port + "/executions/start";
        // 创建 OkHttpClient
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
                .addHeader("Authorization", token)
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
    public void executeWebsocket(String executorId, CommandHandler handler, WsMsgHandler msgHandler){
        try {
            String str = "ws://" + hostname + ":" + port + "/executions/io/" + executorId;
            URI uri = new URI(str);
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", token);
            SimpleWsReceiver simpleWsReceiver = new SimpleWsReceiver(uri, handler, msgHandler, headers);
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
    public void cleanup(String executorId){
        String url = "http://" + hostname + ":" + port + "/executions/cleanup/" + executorId;
        // 创建一个空的 POST 请求体
        RequestBody emptyBody = RequestBody.create(new byte[0]);
        // 构造请求
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .post(emptyBody)
                .build();
        // 执行请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String err = response.body() != null ? response.body().string() : "Unknown error";
                throw new RuntimeException("cleanup 请求失败: " + response.code() + " - " + err);
            }
        } catch (IOException e) {
            throw new RuntimeException("cleanup 请求异常: " + e.getMessage(), e);
        }
    }

    /**
     * @Description : 查询日志,获取脚本执行结果码
     * @Param java.lang.String executorId :
     * @Return : java.lang.Integer
     * @Author : syu
     * @Date : 2025/10/17
     */
    public Integer executeLog(String executorId){
        String url = "http://" + hostname + ":" + port + "/history/execution_log/long/" + executorId;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errMsg = response.body() != null ? response.body().string() : "Unknown error";
                throw new RuntimeException("请求日志失败: " + response.code() + " - " + errMsg);
            }
            if (response.body() == null) {
                throw new IOException("响应体为空");
            }
            // 读取响应内容并解析
            String body = response.body().string();
            JSONObject entries = JSONUtil.parseObj(body);
            return entries.getInt("exitCode");
        } catch (IOException e) {
            throw new RuntimeException("executeLog 异常: " + e.getMessage(), e);
        }
    }

}
