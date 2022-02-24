package com.cqvip.innocence.project.model.entity.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName BaseModel
 * @Description 实体类公共字段
 *              1、注意字段名与数据库字段的对应，建议数据库采取java驼峰命名规则，尽量保持一致
 *              2、若使用下划线命名，需要在配置文件中开启db-column-underline: (false or true)(默认为true)
 *              3、当实体字段与数据库列名不对应，并且既定的规范（驼峰，下划线）不能满足映射关系时，使用@TableField(value="数据库列名")指定对应
 *              4、当前数据库采用下划线命名，开启了转化配置，所以不需要@TableField指定映射关系
 *              5、为了后期构造sql条件方便性，当前类添加了@TableField，同时，生成的实体类会自动加上该注解。
 * @Author Innocence
 * @Date 2020/7/11
 * @Version 1.0
 */
@Data
public abstract class BaseModel extends Model {

    private static final long serialVersionUID = -7479388678300081113L;

    @TableField("ID")
    protected Long id;

    @TableField(value = "CREATE_TIME",fill = FieldFill.INSERT)
    protected Date createTime;

    @TableField(value = "MODIFY_TIME",fill = FieldFill.INSERT_UPDATE)
    protected Date modifyTime;

    @TableLogic
    @TableField(value = "DELETED",fill = FieldFill.INSERT)
    protected Integer deleted;

    @Override
    public Serializable pkVal() {
        return super.pkVal();
    }

}
