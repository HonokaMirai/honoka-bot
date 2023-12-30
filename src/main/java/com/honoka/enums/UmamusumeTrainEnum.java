package com.honoka.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum UmamusumeTrainEnum {

    /**
     * 速度
     */
    SPEED("speed"),

    /**
     * 力量
     */
    POWER("power"),

    /**
     * 耐力
     */
    STAMINA("stamina"),

    /**
     * 根性
     */
    GUTS("guts"),

    /**
     * 智力
     */
    WISDOM("wisdom"),

    ;

    private String code;

    public static UmamusumeTrainEnum getEnumByCode(String code) {
        for (UmamusumeTrainEnum value : UmamusumeTrainEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
