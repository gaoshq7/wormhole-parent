package io.github.gaoshq7;

import io.github.gaoshq7.wormhole.Connection;
import io.github.gaoshq7.wormhole.Wormhole;
import io.github.gaoshq7.wormhole.WormholeFactory;
import io.github.gaoshq7.wormhole.protocol.ScriptExecutor;
import org.junit.Test;

/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.WormholeClientTest
 *
 * @author : gsq
 * @date : 2025-10-14 14:19
 * @note : It's not technology, it's art !
 **/
public class WormholeClientTest {

    @Test
    public void test01() throws Exception{
        WormholeFactory<ScriptExecutor> factory = Wormhole.getFactory("script");
        ScriptExecutor executor = factory.create(new Connection("127.0.0.1", 8080));
        Integer exitCode = executor.execute("test.sh", null, "name^231");
        System.out.println(exitCode);
    }

}
