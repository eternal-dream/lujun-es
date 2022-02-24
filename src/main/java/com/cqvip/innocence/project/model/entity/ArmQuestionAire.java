package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 问卷调查表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_QUESTION_AIRE")
@ApiModel(value = "ArmQuestionAire对象", description = "问卷调查表")
public class ArmQuestionAire extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    @TableField("START_TIME")
    private Date startTime;

    private Integer status;

    @TableField("END_TIME")
    private Date endTime;
    /**
     * 人数限制
     */
    @TableField("PEOPLE_COUNT")
    private Integer peopleCount;
    /**
     * 类型0为问卷，1为投票
     */
    private Integer questiontype;

    @TableField("USER_ID")
    private Long userId;


}
