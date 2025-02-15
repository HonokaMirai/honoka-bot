package com.honoka.model.ai;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : HonokaMirai
 * @version : 1.0
 * @since : 2025-02-14
 */
@Data
@Accessors(chain = true)
public class AssistantCompletionMessage {

    // 回答内容
    private String content;

    // 思维链
    private String reasoningContent;
}
