package io.github.gaoshq7.handler;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AuditHttpClient {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    private final String hostname;

    private final int port;

    private final String token;

    public AuditHttpClient(String hostname, int port, String token) {
        this.hostname = hostname;
        this.port = port;
        this.token = token;
    }

    /**
     * @Description : 查询日志,获取脚本执行结果码
     * @Param java.lang.String executorId :
     * @Return : java.lang.Integer
     * @Author : syu
     * @Date : 2025/10/17
     */
    public String executeLog(Integer executorId){
        String result;
        String url = "http://" + hostname + ":" + port + "/history/execution_log/long/" + executorId;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Basic " + token)
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
            result = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException("executeLog 异常: " + e.getMessage(), e);
        }
        return result;
    }

}
