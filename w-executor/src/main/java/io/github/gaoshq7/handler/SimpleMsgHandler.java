package io.github.gaoshq7.handler;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.gaoshq7.wormhole.WsMsgHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleMsgHandler implements WsMsgHandler {
    @Override
    public String getOriginalMsg(String line) {
        return line;
    }

    @Override
    public String decodeMsg(String line) {
        if (line == null || line.isEmpty()) {
            return null;
        }
        JSONObject json = JSONUtil.parseObj(line);
        String event = json.getStr("event");
        String data = json.getStr("data", "");
        // 只处理 output 事件
        if (!"output".equalsIgnoreCase(event)) {
            return null; // 表示这条消息可以忽略
        }
        // 对 data 进行 Unicode 解码
        return unicodeDecode(data);
    }

    private String unicodeDecode(String str) {
        if (str == null) return "";
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher matcher = pattern.matcher(str);
        int lastEnd = 0;
        while (matcher.find()) {
            sb.append(str, lastEnd, matcher.start());
            char ch = (char) Integer.parseInt(matcher.group(1), 16);
            sb.append(ch);
            lastEnd = matcher.end();
        }
        sb.append(str.substring(lastEnd));
        return sb.toString();
    }
}
