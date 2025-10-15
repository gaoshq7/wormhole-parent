package io.github.gaoshq7.wormhole;

import io.github.gaoshq7.wormhole.impl.ScriptExecutorImpl;
import io.github.gaoshq7.wormhole.protocol.ScriptExecutor;

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
        return new ScriptExecutorImpl();
    }

}
