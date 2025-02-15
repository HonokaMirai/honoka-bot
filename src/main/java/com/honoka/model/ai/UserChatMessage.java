package com.honoka.model.ai;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;
import java.util.List;

/**
 * @author : HonokaMirai
 * @version : 1.0
 * @since : 2025-02-14
 */
@Data
@Accessors(chain = true)
public class UserChatMessage {

    private String model;

    private String message;

    private List<File> fileList;
}
