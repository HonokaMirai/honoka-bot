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
 * @since 2023-07-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("uma_gacha_pool")
@Schema(name = "UmaGachaPool对象", description = "")
public class UmaGachaPool extends Model<UmaGachaPool> {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "马娘id")
    @TableField("uma_id")
    private Long umaId;

    @Schema(description = "马娘中文名称")
    @TableField("uma_cn_name")
    private String umaCnName;

    @Schema(description = "马娘日文名称")
    @TableField("uma_jp_name")
    private String umaJpName;

    @Schema(description = "马娘icon")
    @TableField("icon")
    private String icon;

    @Schema(description = "马娘稀有度")
    @TableField("uma_rare")
    private String umaRare;

    @Schema(description = "马娘抽取概率，单位：% * 100")
    @TableField("uma_probability")
    private Integer umaProbability;

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

    public static final String UMA_ID = "uma_id";

    public static final String UMA_CN_NAME = "uma_cn_name";

    public static final String UMA_JP_NAME = "uma_jp_name";

    public static final String ICON = "icon";

    public static final String UMA_RARE = "uma_rare";

    public static final String UMA_PROBABILITY = "uma_probability";

    public static final String STATUS = "status";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
