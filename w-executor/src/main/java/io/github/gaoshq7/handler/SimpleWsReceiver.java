package io.github.gaoshq7.handler;

import io.github.gaoshq7.wormhole.CommandHandler;
import io.github.gaoshq7.wormhole.WsMsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class SimpleWsReceiver extends WebSocketClient {

    private final CommandHandler handler;

    private final WsMsgHandler wsMsgHandler;

    private final CountDownLatch closeLatch = new CountDownLatch(1);

    public SimpleWsReceiver(URI serverUri, CommandHandler handler, WsMsgHandler wsMsgHandler, Map<String, String> headers) {
        super(serverUri, headers);
        this.handler = handler;
        this.wsMsgHandler = wsMsgHandler;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
    }

    @Override
    public void onMessage(String message) {
        if (handler == null) return;
        String processedMsg = message;
        if (wsMsgHandler != null) {
            try {
                processedMsg = wsMsgHandler.decodeMsg(message);
            } catch (Exception e) {
                handler.line("消息处理异常：" + e.getMessage());
            }
        }
        try {
            if (processedMsg != null) {
                handler.line(processedMsg);
            }
        }catch (Exception e) {
            log.warn("处理消息异常：{}", e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        closeLatch.countDown(); // 通知主线程退出
    }

    @Override
    public void onError(Exception ex) {
    }

    public void awaitClose() throws InterruptedException {
        closeLatch.await(); // 等待关闭信号
    }
}
