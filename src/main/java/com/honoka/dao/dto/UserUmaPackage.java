package com.honoka.dao.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserUmaPackage {

    private Long userId;

    private Long qq;

    private Long umaId;

    private String umaCnName;

    private Integer num;

}
