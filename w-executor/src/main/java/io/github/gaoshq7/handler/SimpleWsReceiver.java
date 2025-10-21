package io.github.gaoshq7.handler;

import io.github.gaoshq7.wormhole.CommandHandler;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

public class SimpleWsReceiver extends WebSocketClient {

    private final CommandHandler handler;

    private final CountDownLatch closeLatch = new CountDownLatch(1);

    public SimpleWsReceiver(URI serverUri, CommandHandler handler) {
        super(serverUri);
        this.handler = handler;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
    }

    @Override
    public void onMessage(String message) {
        if (handler != null) {
            handler.line(message);
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
