package com.honoka.mirai.config;

import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.data.java.JavaAutoSavePluginConfig;
import org.jetbrains.annotations.NotNull;

public class YoutubeApiConfig extends JavaAutoSavePluginConfig {

    public static final YoutubeApiConfig INSTANCE = new YoutubeApiConfig("YoutubeApiConfig");

    public YoutubeApiConfig(@NotNull String saveName) {
        super(saveName);
    }

    public final Value<String> API_KEY = value("api_key", "");
}
