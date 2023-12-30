package com.honoka.mirai.config;

import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.data.java.JavaAutoSavePluginConfig;
import org.jetbrains.annotations.NotNull;

public class OpenAiGptConfig extends JavaAutoSavePluginConfig {
    public static final OpenAiGptConfig INSTANCE = new OpenAiGptConfig("OpenAiGptConfig");

    public OpenAiGptConfig(@NotNull String saveName) {
        super(saveName);
    }

    public final Value<String> API_KEY = value("api_key", "sk-");

    public final Value<String> SD_PROMPT = value("sd_prompt", "");

    public final Value<String> SD_URL = value("sd_url", "http://localhost:port/api");
}
