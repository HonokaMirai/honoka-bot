package com.honoka.config;

import lombok.Data;
import net.mamoe.mirai.utils.MiraiLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 机器人基础信息配置
 * @author honoka
 * @date 2022-10-12 15:46:05
 */
@Data
public class BotConfig {

    public volatile static MiraiLogger logger;

    private Long qq = 0L;

    private GptConfig gptConfig = new GptConfig();

    private ReplyConfig replyConfig = new ReplyConfig();

    @Data
    public static class GptConfig {

        // host
        private String host = "";

        // apikey
        private String apikey = "";

        // 可支持的模型列表
        private List<String> models;

    }

    @Data
    public static class ReplyConfig {

        private Long collectCount = 15L;

        private String systemPrompt = "";

        private List<Long> groups = new ArrayList<>();


    }

}
