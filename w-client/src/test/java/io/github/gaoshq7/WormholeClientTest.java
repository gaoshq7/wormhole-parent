package io.github.gaoshq7;

import io.github.gaoshq7.wormhole.*;
import io.github.gaoshq7.wormhole.protocol.AuditObserver;
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
    public void test01(){
        try {
            WormholeFactory<ScriptExecutor> factory = Wormhole.getFactory("script");
            ScriptExecutor executor = factory.create(new Connection("127.0.0.1", 8080));
            HashMap<String, Object> argMap = new HashMap<>();
            argMap.put("name", "测试");
            Integer exitCode = executor.execute("test.sh", new CommandHandler() {
                @Override
                public void id(String id) {
                    System.out.println(id);
                }

                @Override
                public void line(String line) {
                    System.out.println(line);
                }
            }, argMap);
            System.out.println(exitCode);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test02(){
        try {
            WormholeFactory<AuditObserver> factory = Wormhole.getFactory("audit");
            AuditObserver observer = factory.create(new Connection("127.0.0.1", 8080));
            AuditMsg auditMsg = observer.audit(36);
            System.out.println("-------------状态-------------");
            System.out.println(auditMsg.getStatus());
            System.out.println("-------------命令-------------");
            System.out.println(auditMsg.getCommand());
            System.out.println("-------------日志-------------");
            System.out.println(auditMsg.getLog());
            System.out.println("-------------状态码-------------");
            System.out.println(auditMsg.getCode());
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
