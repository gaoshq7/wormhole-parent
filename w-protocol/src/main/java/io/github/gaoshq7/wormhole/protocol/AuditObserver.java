package io.github.gaoshq7.wormhole.protocol;

import io.github.gaoshq7.wormhole.AuditMsg;
import io.github.gaoshq7.wormhole.WormholeClient;

/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.wormhole.protocol.AuditObserver
 *
 * @author : gsq
 * @date : 2025-10-14 15:20
 * @note : It's not technology, it's art !
 **/
public interface AuditObserver extends WormholeClient {

    /**
     * @Description : 历史执行结果查询
     * @param id : 执行脚本任务的唯一标识
     * @Return : io.github.gaoshq7.wormhole.AuditMsg
     * @Author : gsq
     * @Date : 2025/10/14 16:07
     * @Note : An art cell !
     **/
    AuditMsg audit(Integer id);

}
