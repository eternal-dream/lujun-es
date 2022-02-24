package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.math.BigInteger;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Innocence
 * @since 2021-09-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_EXPERT_SHARING")
@ApiModel(value = "ArmExpertSharing对象", description = "")
public class ArmExpertSharing extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String url;

    private String title;

    @TableField("USER_ID")
    private Long userId;

    @TableField("BROWSE_NUM")
    private Integer browseNum;


}
