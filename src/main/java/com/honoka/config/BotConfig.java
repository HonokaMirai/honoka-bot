package com.honoka.config;

import lombok.Data;
import net.mamoe.mirai.utils.MiraiLogger;

import java.util.List;

/**
 * @description 机器人基础信息配置
 * @author honoka
 * @date 2022-10-12 15:46:05
 */
@Data
public class BotConfig {

    public volatile static MiraiLogger logger;

    private GptConfig gptConfig = new GptConfig();

    @Data
    public static class GptConfig {

        // host
        private String host = "";

        // apikey
        private String apikey = "";

        // 可支持的模型列表
        private List<String> models;

    }

}
