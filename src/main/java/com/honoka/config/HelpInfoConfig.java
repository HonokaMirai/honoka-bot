package com.honoka.config;

import lombok.Data;

/**
 * @author honoka
 * @description 帮助信息配置
 * @date 2022-10-14 14:57:08
 */
@Data
public class HelpInfoConfig {

    public static final String HELP_MODULE = "插件帮助指令：";
    public static final String BILIBILI_HELP = "/bili help 获取bili插件相关帮助信息";
    public static final String APEX_HELP = "/apexhelp 获取Apex插件相关帮助信息";
    public static final String PIXIV_MODULE = "PIXIV指令：";
    public static final String PIXIV_HELP = "-PIXIV [可选R18] [关键字] [关键字]";
    public static final String CHATGPT_MODULE = "ChatGPT指令:";
    public static final String CHATGPT35_MODULE = "/c [问题] : 问答模式, 单次对话, 使用3.5模型";
    public static final String CHATGPT35_SYS_HELP = "/c sys [问题] : 使用role=system, 设置场景设定";
    public static final String CHATGPT35_HELP = "/c t [问题] : 继续发起一个持续性对话，会使用设置的system设定";
    public static final String CHATGPT35_END = "/c end : 清除持续性对话";
    public static final String VTUBER_MODULE = "Vtuber音频指令:";
    public static final String VTUBER_RANDOM_HELP = "/a 随机发送一条Vtuber典";
    public static final String MUSIC_MODULE = "音乐指令:";
    public static final String MUSIC_RANDOM_HELP = "/m 随机发送一首音乐";
    public static final String SD_AI_MODULE = "SD-AI画图指令：";
    public static final String SD_AI_GPT_PROMPT = "/sd [自然语言描述]";
    public static final String SD_AI_API_PROMPT = "/sd api {\"prompt\":\"\", \"negative_prompt\":\"\", \"width\":512, \"height\":512,\"steps\":20,\"cfg_scale\":7}";


    public static String getHelpInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(HELP_MODULE).append("\n")
//                .append(HELP).append("\n")
                .append(BILIBILI_HELP).append("\n")
                .append(APEX_HELP).append("\n")
//                .append(PIXIV_MODULE).append("\n")
//                .append(PIXIV_HELP).append("\n")
                .append(CHATGPT_MODULE).append("\n")
                .append(CHATGPT35_MODULE).append("\n")
                .append(CHATGPT35_SYS_HELP).append("\n")
                .append(CHATGPT35_HELP).append("\n")
                .append(CHATGPT35_END).append("\n")
                .append(VTUBER_MODULE).append("\n")
                .append(VTUBER_RANDOM_HELP).append("\n")
                .append(MUSIC_MODULE).append("\n")
                .append(MUSIC_RANDOM_HELP).append("\n")
                .append(SD_AI_MODULE).append("\n")
                .append(SD_AI_GPT_PROMPT).append("\n")
                .append(SD_AI_API_PROMPT).append("\n")
        ;
        return sb.toString();
    }
}
