package io.github.gaoshq7.wormhole;

public interface WsMsgHandler {

    String getOriginalMsg(String line);

    String decodeMsg(String line);

}
