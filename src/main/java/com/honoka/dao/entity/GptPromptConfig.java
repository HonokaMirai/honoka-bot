package com.honoka.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author mybatis-plus
 * @since 2023-07-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("gpt_prompt_config")
@Schema(name = "GptPromptConfig对象", description = "")
public class GptPromptConfig extends Model<GptPromptConfig> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "类型")
    @TableField("`type`")
    private String type;

    @Schema(description = "system角色的prompt")
    @TableField("sys_prompt")
    private String sysPrompt;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "状态，0：无效 1：有效")
    @TableField("`status`")
    private Integer status;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


    public static final String ID = "id";

    public static final String TYPE = "type";

    public static final String SYS_PROMPT = "sys_prompt";

    public static final String REMARK = "remark";

    public static final String STATUS = "status";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
