package com.cqvip.innocence.project.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import java.sql.Clob;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 屏蔽词表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.ARM_BLOCK_WORD")
@ApiModel(value = "ArmBlockWord对象", description = "屏蔽词表")
public class ArmBlockWord extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;


}
