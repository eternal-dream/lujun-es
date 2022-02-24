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
 * 数据库分类表
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ARMYINFANTRY.DB_INFO_CLASS")
@ApiModel(value = "DbInfoClass对象", description = "数据库分类表")
public class DbInfoClass extends BaseModel {

    private static final long serialVersionUID = 1L;

    private String title;


}
