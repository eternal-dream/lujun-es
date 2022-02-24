package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 新刊简目分类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_NEW_JOURNAL_CLASS")
@ApiModel(value = "ArmNewJournalClass对象", description = "新刊简目分类")
public class ArmNewJournalClass extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;

    private Integer level;

    @TableField("PARENT_ID")
    private Long parentId;

    @TableField(exist = false)
    private List<ArmNewJournal> childrenList;

}
