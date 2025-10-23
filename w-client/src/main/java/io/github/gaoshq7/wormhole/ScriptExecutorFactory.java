package io.github.gaoshq7.wormhole;

import io.github.gaoshq7.wormhole.impl.ScriptExecutorImpl;
import io.github.gaoshq7.wormhole.protocol.ScriptExecutor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.wormhole.ScriptExecutorFactory
 *
 * @author : gsq
 * @date : 2025-10-14 17:50
 * @note : It's not technology, it's art !
 **/
@Factory("script")
public class ScriptExecutorFactory extends WormholeFactory<ScriptExecutor> {

    @Override
    public ScriptExecutor create(Connection connection) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(connection.getHostname(), connection.getPort()), 2000);
        } catch (IOException e) {
            throw new RuntimeException(connection.getHostname()+"的 wormhole 服务不可用!");
        }
        return new ScriptExecutorImpl(connection.getHostname(), connection.getPort(), connection.getToken());
    }

}
