package com.honoka.config;

import lombok.Data;

@Data
public class UserSystemConfig {

    public final static Integer DIAMOND = 10000;

    public static class Command {

        /**
         * 抽卡
         */
        public static final String REGISTER = "register";

        /**
         * 刷新马娘卡池
         */
        public static final String REFRESH_UMA = "refresh_uma";

    }

    public static class Reply {

        /**
         * 未知指令
         */
        public static final String UNKNOWN = "未知指令~";

        /**
         * 未知指令
         */
        public static final String REGISTER_SUCCESS = "注册成功~";

        /**
         * 用户存在
         */
        public static final String USER_EXIST = "用户已存在~";

        /**
         * 用户不存在
         */
        public static final String USER_NOT_EXIST = "用户不存在，请使用 sys register 进行注册~";

        /**
         * 用户存在
         */
        public static final String REFRESH_SUCCESS = "刷新卡池成功~";

        /**
         * 钻石不足
         */
        public static final String DIAMOND_NOT_ENOUGH = "钻石不足";
    }
}
