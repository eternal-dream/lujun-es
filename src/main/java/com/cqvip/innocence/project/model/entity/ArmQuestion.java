package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import java.sql.Clob;

import com.cqvip.innocence.project.model.enums.VipEnums;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 提问表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_QUESTION")
@ApiModel(value = "ArmQuestion对象", description = "提问表")
public class ArmQuestion extends BaseModel {

    private static final long serialVersionUID = 1L;
    /**
     * 问题标题
     */
    private String title;
    /**
     * 问题内容
     */
    private String content;
    /**
     * 用户id
     */
    @TableField("USER_ID")
    private Long userId;
    /**
     * 管理员id
     */
    @TableField("ADMIN_ID")
    private Long adminId;
    /**
     * 问题类型
     */
    private VipEnums.QuestionTypeEnum type;
    /**
     * 常见问题分类id
     */
    @TableField("CLASS_ID")
    private Long classId;

    private String answer;

    //用于保存提问人姓名于前端显示
    @TableField(exist = false)
    private String userName;


}
