package com.honoka.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.honoka.dao.entity.GptPromptConfig;
import com.honoka.mirai.config.OpenAiGptConfig;
import com.honoka.model.ai.AssistantCompletionMessage;
import com.honoka.util.MiraiUtil;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import lombok.Data;
import net.mamoe.mirai.console.command.CommandContext;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.message.data.MessageChain;
import okhttp3.OkHttpClient;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * HOST
     */
    public static String HOST = "https://api.holdai.top";

    /**
     * APIKEY
     */
    public static String APIKEY = "";

    /**
     * 默认模型
     */
    public static String DEFAULT_MODEL = "gpt-3.5-turbo";

    /**
     * 支持的模型列表
     */
    public static List<String> MODELS = new ArrayList<>();


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

        /**
         * change model
         */
        public static final String MODEL = "model";

        /**
         * models列表查询
         */
        public static final String MODELS = "models";
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
                            .apiKey(Arrays.asList(APIKEY))
                            .apiHost(HOST)
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
    public static ChatCompletion getChatCompletion(String model, List<Message> messageList) {
        if (StrUtil.isBlank(model)) {
            model = ChatGPTConfig.DEFAULT_MODEL;
        }
        return ChatCompletion.builder().messages(messageList).model(model).temperature(1.0).build();
    }

    /**
     * 修改model值
     */
    public static void setModel(String model) {
        ChatGPTConfig.DEFAULT_MODEL = model;
    }

    /**
     * 处理AI会话请求，处理结果
     * @param model
     * @param messageList
     * @return
     */
    public static AssistantCompletionMessage handleChatCompletionApi(String model, List<Message> messageList, CommandContext context) {
        AssistantCompletionMessage assistantCompletionMessage = new AssistantCompletionMessage();

        try {
            // 1.调用OpenAiClient的chatCompletion方法
            ChatCompletion chatCompletion = getChatCompletion(model, messageList);
            ChatCompletionResponse response = getOpenAiClient().chatCompletion(chatCompletion);
            // 2.处理返回结果
            StringBuilder sb = new StringBuilder();
            response.getChoices().forEach(choice -> {
                sb.append(choice.getMessage().getContent());
            });
            // 3.处理返回结果中的think标签
            handleThinkingContent(sb.toString(), assistantCompletionMessage);
        } catch (BaseException e) {
            // token过多，提示用户
            BotConfig.logger.error(e);
            if (Objects.nonNull(context)) {
                MessageChain message = MiraiUtil.buildQuoteReplyMessage(context.getOriginalMessage(), context.getSender().getUser().getId(), "Token超出上限，请使用/c end清空会话。");
                context.getSender().getSubject().sendMessage(message);
            }
        } catch (Exception e) {
            // 请求异常
            BotConfig.logger.error(e);
            if (Objects.nonNull(context)) {
                MessageChain message = MiraiUtil.buildQuoteReplyMessage(context.getOriginalMessage(), context.getSender().getUser().getId(), "请求发生异常，请稍后重试。");
                context.getSender().getSubject().sendMessage(message);
            }
        }
        return assistantCompletionMessage;
    }

    /**
     * 处理AI会话请求，处理结果
     * @param messageList
     * @return
     */
    public static AssistantCompletionMessage handleChatCompletionApi(List<Message> messageList) {
        return handleChatCompletionApi(null, messageList, null);
    }

    /**
     * 处理带有think标签的内容
     * @param originContent
     * @param assistantCompletionMessage
     */
    private static void handleThinkingContent(String originContent, AssistantCompletionMessage assistantCompletionMessage) {
        // 判断originContent里是否包含<think></think>，并把其中的内容提取出来
        String thinkContent = "";
        String content = originContent;
        if (originContent.contains("<think>") && originContent.contains("</think>")) {
            thinkContent = originContent.substring(originContent.indexOf("<think>") + 7, originContent.indexOf("</think>"));
            content = originContent.substring(originContent.indexOf("</think>") + 8);
        }
        // 取出content字段前后的空格或者换行符
        assistantCompletionMessage.setReasoningContent(thinkContent);
        assistantCompletionMessage.setContent(content.trim());
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
