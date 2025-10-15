package io.github.gaoshq7.wormhole;

import lombok.Getter;
import lombok.Setter;

/**
 * Project : wormhole-parent
 * Class : io.github.gaoshq7.wormhole.AuditMsg
 *
 * @author : gsq
 * @date : 2025-10-14 15:23
 * @note : It's not technology, it's art !
 **/
@Getter
@Setter
public class AuditMsg {

    private String id;  // 执行id

    private String name;    // 脚本名称

    private String command; // 脚本命令

    private String status;  // 执行状态

    private Integer code;   // 执行结果码

    private String log; // 执行日志

    private String time;    // 执行时间

}
