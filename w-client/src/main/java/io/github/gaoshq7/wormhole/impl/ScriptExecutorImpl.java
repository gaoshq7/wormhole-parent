package io.github.gaoshq7.wormhole.impl;

import io.github.gaoshq7.handler.ExecutorHttpClient;
import io.github.gaoshq7.wormhole.ExecutorContext;
import io.github.gaoshq7.wormhole.protocol.ScriptExecutor;

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

    private final ExecutorHttpClient client;

    public ScriptExecutorImpl(String hostname, int port, String token) {
        this.hostname = hostname;
        this.port = port;
        this.client = new ExecutorHttpClient(hostname, port, token);
    }

    @Override
    public Integer execute(ExecutorContext context) {
        // 执行start接口
        String executorId;
        try {
            executorId = client.getExecutorId(context.getScript(), context.getArgs());
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        if (context.getCommandHandler() != null) {
            context.getCommandHandler().id(executorId);
        }
        // 执行websocket接口，接受数据, 数据传入handler
        client.executeWebsocket(executorId, context.getCommandHandler(), context.getMsgHandler());
        // 执行log接口，查询脚本执行状态
        return client.executeLog(executorId);
    }

}
