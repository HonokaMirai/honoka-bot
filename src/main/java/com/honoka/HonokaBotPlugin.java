package com.honoka;

import net.mamoe.mirai.console.plugin.jvm.*;

/**
 * 使用 Java 请把
 * {@code /src/main/resources/META-INF.services/net.mamoe.mirai.console.plugin.jvm.JvmPlugin}
 * 文件内容改成 {@code org.example.mirai.plugin.JavaPluginMain} <br/>
 * 也就是当前主类全类名
 * <p>
 * 使用 Java 可以把 kotlin 源集删除且不会对项目有影响
 * <p>
 * 在 {@code settings.gradle.kts} 里改构建的插件名称、依赖库和插件版本
 * <p>
 * 在该示例下的 {@link JvmPluginDescription} 修改插件名称，id 和版本等
 * <p>
 * 可以使用 {@code src/test/kotlin/RunMirai.kt} 在 IDE 里直接调试，
 * 不用复制到 mirai-console-loader 或其他启动器中调试
 */

public final class HonokaBotPlugin extends JavaPlugin {
    public static final HonokaBotPlugin INSTANCE = new HonokaBotPlugin();

    private HonokaBotPlugin() {
        super(new JvmPluginDescriptionBuilder("com.honoka.mirai-honoka-plugin", "0.1.0")
                .info("EG")
                .build());
    }

    @Override
    public void onEnable() {
        try {
            HonokaBotApplication.start(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}