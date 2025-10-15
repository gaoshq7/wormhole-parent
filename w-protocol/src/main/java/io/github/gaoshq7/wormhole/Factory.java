package io.github.gaoshq7.wormhole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.wormhole.Client
 *
 * @author : gsq
 * @date : 2025-10-15 11:39
 * @note : It's not technology, it's art !
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Factory {

    String value();

}
