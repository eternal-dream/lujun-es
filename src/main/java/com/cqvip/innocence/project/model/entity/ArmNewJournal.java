package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import java.sql.Clob;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 新刊表
 * </p>
 *
 * @author Innocence
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_NEW_JOURNAL")
@ApiModel(value = "ArmNewJournal对象", description = "新刊表")
public class ArmNewJournal extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;

    @TableField("CLASS_ID")
    private Long classId;

    @TableField("IMG_URL")
    private String imgUrl;

    private String content;


}
