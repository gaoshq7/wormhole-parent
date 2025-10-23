package io.github.gaoshq7.wormhole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExecutorContext {

    private String script;

    private Map<String, Object> args;

    private CommandHandler commandHandler;

    private WsMsgHandler msgHandler;

}
