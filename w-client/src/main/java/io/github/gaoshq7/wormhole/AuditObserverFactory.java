package io.github.gaoshq7.wormhole;

import io.github.gaoshq7.wormhole.impl.AuditObserverImpl;
import io.github.gaoshq7.wormhole.protocol.AuditObserver;

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
        return new AuditObserverImpl();
    }

}
