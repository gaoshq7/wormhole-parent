package io.github.gaoshq7.wormhole;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.ServiceLoader;


/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.wormhole.Wormhole
 *
 * @author : gsq
 * @date : 2025-10-15 10:37
 * @note : It's not technology, it's art !
 **/
@Slf4j
public class Wormhole {

    private final static Map<String, WormholeFactory<? extends WormholeClient>> factories = MapUtil.newHashMap();

    public static Map<String, WormholeFactory<? extends WormholeClient>> getFactories() {
        try {
            if (CollUtil.isEmpty(factories)) {
                ServiceLoader<WormholeFactory> factories = ServiceLoader.load(WormholeFactory.class);
                for (WormholeFactory<?> factory : factories) {
                    String name = factory.getClass().getAnnotation(Factory.class).value();
                    Wormhole.factories.put(name, factory);
                }
            }
        } catch (Exception e) {
            log.error("Failed to load WormholeFactory", e);
            factories.clear();
        }
        return factories;
    }

    public static <T extends WormholeFactory<? extends WormholeClient>> T getFactory(String name) {
        return (T) getFactories().get(name);
    }

}
