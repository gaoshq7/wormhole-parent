package io.github.gaoshq7.wormhole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
public class Connection {

    private String hostname;

    private Integer port;

    private String token;

    private String username;

    private String password;

    public Connection(String hostname, int port, String token) {
        this.hostname = hostname;
        this.port = port;
        this.token = token;
    }

    public Connection(String hostname, int port, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.token = "Basic " + Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

}
