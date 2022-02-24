package com.cqvip.innocence.framework.config.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName MyMetaObjectHandler
 * @Description 自动填充字段功能设置，配合BaseModel和MybatisPlusConfig使用。
 *              实现insertFill和UpdateFill
 * @Author Innocence
 * @Date 2020/7/13 10:04
 * @Version 1.0
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     *this.setFieldValByName(String arg1,Object obj,MetaObject metaObject)
     * arg1：对应的JavaBean属性名
     * obj:填充的对象
     */

    /**
     * 新增数据执行
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", new Date(),metaObject);
        this.setFieldValByName("modifyTime",new Date(),metaObject);
        this.setFieldValByName("deleted",0,metaObject);
    }

    /**
     * 更新数据执行
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("modifyTime",new Date(),metaObject);
    }
}
