package io.github.gaoshq7.wormhole;

/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.wormhole.CommandHandler
 *
 * @author : gsq
 * @date : 2025-10-14 16:16
 * @note : It's not technology, it's art !
 **/
public interface CommandHandler {

    /**
     * @Description : 命令任务执行 id 处理程序
     * @param id : 命令任务执行的唯一标识
     * @Return : void
     * @Author : gsq
     * @Date : 2025/10/14 16:19
     * @Note : ⚠️ 抛出异常则终止脚本任务 !
     **/
    void id(String id);

    /**
     * @Description : 阻塞任务信息流处理程序
     * @param line : 每一行信息
     * @Return : void
     * @Author : gsq
     * @Date : 2025/10/14 16:20
     * @Note : ⚠️ 抛出异常不会影响脚本任务的运行 !
     **/
    void line(String line);

}
