package io.github.gaoshq7.wormhole;

/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.wormhole.WormholeFactory
 *
 * @author : gsq
 * @date : 2025-10-14 17:17
 * @note : It's not technology, it's art !
 **/
public abstract class WormholeFactory<T extends WormholeClient> {

    public abstract T create(Connection connection);

}
