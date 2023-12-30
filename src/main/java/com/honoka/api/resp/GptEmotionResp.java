package com.honoka.api.resp;

import lombok.Data;

@Data
public class GptEmotionResp {

    /**
     * 回答
     */
    private String answer;

    /**
     * 情绪
     */
    private String emotion;
}
