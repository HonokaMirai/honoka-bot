package com.honoka.config;

import io.github.mzdluo123.silk4j.AudioUtils;

import java.io.File;
import java.io.IOException;

/**
 * @description 初始化一些工具类
 * @author honoka
 * @date 2023-03-19 14:34:39
 */
public class InitUtilConfig {

    public static void init() {
        try {
            AudioUtils.init(new File(FileConfig.TEMP_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            BotConfig.logger.error("初始化AudioUtils工具类失败: " + e);
        }
    }
}
