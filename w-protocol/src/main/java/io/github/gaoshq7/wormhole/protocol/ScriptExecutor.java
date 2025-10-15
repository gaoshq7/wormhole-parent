package io.github.gaoshq7.wormhole.protocol;

import io.github.gaoshq7.wormhole.CommandHandler;
import io.github.gaoshq7.wormhole.WormholeClient;

/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.wormhole.protocol.ScriptExecutor
 *
 * @author : gsq
 * @date : 2025-10-14 14:38
 * @note : It's not technology, it's art !
 **/
public interface ScriptExecutor extends WormholeClient {

    /**
     * @Description : 执行脚本（阻塞等待完成）
     * @param script : 执行的脚本
     * @param handler : 行信息流处理函数
     * @param args : 脚本参数
     * @Return : java.lang.Integer（脚本返回值）
     * @Author : gsq
     * @Date : 2025/10/14 15:01
     * @Note : ⚠️ 该函数要阻塞直至完成 !
     **/
    Integer execute(String script, CommandHandler handler, String... args);

}
