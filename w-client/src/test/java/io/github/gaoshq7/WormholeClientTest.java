package io.github.gaoshq7;

import cn.hutool.json.JSONUtil;
import io.github.gaoshq7.handler.SimpleMsgHandler;
import io.github.gaoshq7.wormhole.*;
import io.github.gaoshq7.wormhole.protocol.AuditObserver;
import io.github.gaoshq7.wormhole.protocol.ScriptExecutor;
import org.jetbrains.annotations.NotNull;
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
//            ScriptExecutor executor = factory.create(new Connection("127.0.0.1", 8080, "Basic YWRtaW46YWRtaW4xMjM0QHN1Z29u"));
            ScriptExecutor executor = factory.create(new Connection("127.0.0.1", 8080, "admin", "admin1234@sugon"));
            ExecutorContext context = getExecutorContext();
            Integer exitCode = executor.execute(context);
            System.out.println(exitCode);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static @NotNull ExecutorContext getExecutorContext() {
        HashMap<String, Object> argMap = new HashMap<>();
        argMap.put("name", "测试");
        CommandHandler commandHandler = new CommandHandler() {
            @Override
            public void id(String id) {
                System.out.println(id);
            }

            @Override
            public void line(String line) {
                System.out.print(line);
            }
        };
        return new ExecutorContext("test.sh", argMap, commandHandler, new SimpleMsgHandler());
    }

    @Test
    public void test02(){
        try {
            WormholeFactory<AuditObserver> factory = Wormhole.getFactory("audit");
            AuditObserver observer = factory.create(new Connection("127.0.0.1", 8080, "Basic YWRtaW46YWRtaW4xMjM0QHN1Z29u"));
            AuditMsg auditMsg = observer.audit(36);
            System.out.println(JSONUtil.toJsonPrettyStr(auditMsg));
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
