package io.github.gaoshq7.wormhole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.wormhole.Connection
 *
 * @author : gsq
 * @date : 2025-10-14 17:40
 * @note : It's not technology, it's art !
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Connection {

    private String hostname;

    private Integer port;

    private String token;

}
