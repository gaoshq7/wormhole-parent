package io.github.gaoshq7;

import io.github.gaoshq7.wormhole.Connection;
import io.github.gaoshq7.wormhole.Wormhole;
import io.github.gaoshq7.wormhole.WormholeFactory;
import io.github.gaoshq7.wormhole.protocol.ScriptExecutor;
import org.junit.Test;

import java.util.HashMap;

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
        HashMap<String, Object> argMap = new HashMap<>();
        argMap.put("name", "测试");
        Integer exitCode = executor.execute("test.sh", null, argMap);
        System.out.println(exitCode);
    }

}
