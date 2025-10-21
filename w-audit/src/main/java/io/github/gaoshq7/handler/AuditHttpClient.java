package io.github.gaoshq7.handler;

import okhttp3.OkHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class AuditHttpClient {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    private final String hostname;

    private final int port;

    public AuditHttpClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
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
                result = response.toString();
            }
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

}
