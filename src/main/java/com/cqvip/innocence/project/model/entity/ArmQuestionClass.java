package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 问卷分类表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_QUESTION_CLASS")
@ApiModel(value = "ArmQuestionClass对象", description = "问卷分类表")
public class ArmQuestionClass extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;


}
