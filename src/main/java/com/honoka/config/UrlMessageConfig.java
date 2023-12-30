package com.honoka.config;

import lombok.Data;

@Data
public class UrlMessageConfig {

    public static class Constant {

        /**
         * HTTP
         */
        public static final String HTTP = "http";

        /**
         * BILIBILI
         */
        public static final String BILIBILI = "bilibili";

        /**
         * VIDEO
         */
        public static final String VIDEO = "video";

        /**
         * b23.tv B站短连接
         */
        public static final String BTV = "b23.tv";

        /**
         * Youtube
         */
        public static final String YOUTUBE = "youtube";

        /**
         * Youtube短连接
         */
        public static final String YOUTU_BE = "youtu.be";
    }

    public static class Code {
        /**
         * SUCCESS
         */
        public static final String SUCCESS = "200";
    }
}
