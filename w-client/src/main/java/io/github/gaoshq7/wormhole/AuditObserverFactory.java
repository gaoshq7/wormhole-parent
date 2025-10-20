package io.github.gaoshq7.wormhole;

import io.github.gaoshq7.wormhole.impl.AuditObserverImpl;
import io.github.gaoshq7.wormhole.protocol.AuditObserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.wormhole.AuditObserverFactory
 *
 * @author : gsq
 * @date : 2025-10-15 11:13
 * @note : It's not technology, it's art !
 **/
@Factory("audit")
public class AuditObserverFactory extends WormholeFactory<AuditObserver> {

    @Override
    public AuditObserver create(Connection connection) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(connection.getHostname(), connection.getPort()), 2000);
        } catch (IOException e) {
            throw new RuntimeException(connection.getHostname()+"的 wormhole 服务不可用!");
        }
        return new AuditObserverImpl(connection.getHostname(), connection.getPort());
    }

}
