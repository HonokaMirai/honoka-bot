package com.honoka.config;

import cn.hutool.core.collection.CollectionUtil;
import com.honoka.dao.entity.GptPromptConfig;
import com.honoka.mirai.config.OpenAiGptConfig;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import lombok.Data;
import okhttp3.OkHttpClient;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description ChatGPT相关配置
 * @author Honoka
 * @date 2023-02-10 22:48:07
 */
@Data
public class ChatGPTConfig {

    /**
     * 初始化OpenAiClient
     */
    public static OpenAiClient openAiClient;

    /**
     * 保存每个用户的会话记录
     */
    public static Map<Long, List<Message>> chatgptMap = new HashMap<>(16);

    /**
     * 最大会话次数
     */
    public static final Integer MAX_SIZE = 30;

    /**
     * 单次会话的system预设
     */
    public static GptPromptConfig systemPreset;

    /**
     * 单次会话的角色预设
     */
    public static String character = "Shinigamichan";


    /**
     * 设置的可用参数常量
     */
    public static class Command {

        /**
         * 持续性会话
         */
        public static final String T = "t";

        /**
         * 持续性会话-系统角色设置
         */
        public static final String SYS = "sys";

        /**
         * 设置单次对话的system预设
         */
        public static final String SET = "set";

        /**
         * 设置所有人单次对话的system预设
         */
        public static final String SET_ALL = "setAll";

        /**
         * 清除所有人单次对话的system预设
         */
        public static final String REFRESH = "refresh";

        /**
         * 设置角色
         */
        public static final String CHARACTER = "character";

        /**
         * 结束会话-清空会话列表
         */
        public static final String END = "end";
    }

    /**
     * 初始化openAiClient，创建单例对象
     */
    public static OpenAiClient getOpenAiClient() {
        if (openAiClient == null) {
            synchronized (ChatGPTConfig.class) {
                if (openAiClient == null) {
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new OpenAiResponseInterceptor())
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build();
                    openAiClient = OpenAiClient.builder()
                            //支持多key传入，请求时候随机选择
                            .apiKey(Arrays.asList(OpenAiGptConfig.INSTANCE.API_KEY.get()))
                            //自定义key的获取策略：默认KeyRandomStrategy
                            //.keyStrategy(new KeyRandomStrategy())
                            //.keyStrategy(new FirstKeyStrategy())
                            .okHttpClient(okHttpClient)
                            .build();
                    //openAiClient = new OpenAiClient(OpenAiGptConfig.INSTANCE.API_KEY.get(), 60L, 60L, 60L, null);
                }
            }
        }
        return openAiClient;
    }

    /**
     * 生成ChatCompletion
     * @param messageList
     * @return
     */
    public static ChatCompletion getChatCompletion(List<Message> messageList) {
        return ChatCompletion.builder().messages(messageList).temperature(1.0).build();
    }

    /**
     * 获取用户的历史消息列表
     * @param qq
     * @return
     */
    public static List<Message> getUserMessageList(Long qq) {
        List<Message> messageList = chatgptMap.get(qq);
        if (CollectionUtil.isEmpty(messageList)) {
            messageList = new ArrayList<>();
            chatgptMap.put(qq, messageList);
        }
        return messageList;
    }

    /**
     * 给用户添加一个历史消息列表
     * @param qq
     * @param messageList
     * @return
     */
    public static List<Message> addUserMessageList(Long qq, List<Message> messageList) {
        return chatgptMap.put(qq, messageList);
    }

    /**
     * 移除用户的历史消息列表
     * @param qq
     * @return
     */
    public static void removeUserMessageList(Long qq) {
        chatgptMap.remove(qq);
    }

    /**
     * 设置单次会话的system预设
     * @param config
     */
    public static void setSystemPreset(GptPromptConfig config) {
        systemPreset = config;
    }

    /**
     * 获取单次会话的system预设
     * @return
     */
    public static String getSystemPresetType() {
        if (Objects.nonNull(systemPreset)) {
            return systemPreset.getType();
        }
        return null;
    }

    /**
     * 获取单次会话的system预设
     * @return
     */
    public static Message getSystemPresetPrompt() {
        if (Objects.nonNull(systemPreset)) {
            //return new Message(Message.Role.SYSTEM.getName(), systemPreset.getSysPrompt());
            return Message.builder().role(Message.Role.SYSTEM.getName()).content(systemPreset.getSysPrompt()).build();
        }
        return null;
    }

    /**
     * 清除单次会话的system预设
     */
    public static void refreshSystemPreset() {
        systemPreset = null;
    }
}
