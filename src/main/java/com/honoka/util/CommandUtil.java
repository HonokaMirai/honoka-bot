package com.honoka.util;

import com.honoka.HonokaBotPlugin;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandUtil {

    public static List<String> commandToParamsList(String command) {
        List<String> list = new ArrayList<>();
        String[] split = command.split("\\s+");
        for (int i = 1; i < split.length; i++) {
            String s = split[i];
            if (StringUtils.isNotBlank(s)) {
                list.add(s);
            }
        }
        return list;
    }

    /**
     * ChatGPT返回只过滤掉开头的\n
     * @param content
     * @return
     */
    public static String filter(String content) {
        return content.replaceFirst("\n", "").replaceFirst("\n", "");
    }

}
