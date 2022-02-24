package com.cqvip.generator.factory;

import com.baomidou.mybatisplus.annotation.DbType;
import com.cqvip.generator.constant.GeneratorType;
import com.cqvip.generator.service.GeneratorService;
import com.cqvip.innocence.common.util.reflection.ClassUtils;

import java.util.Set;

/**
 * @ClassName GeneratorFactory
 * @Description Generator工厂
 * @Author Innocence
 * @Date 2020/10/28 13:30
 * @Version 1.0
 */
public class GeneratorFactory {

    public static final String PACKAGE_NAME = "com.cqvip.generator.service.impl";

    public GeneratorService getGenerator(DbType dbType){
        Set<Class<?>> classes;
        try {
            classes = ClassUtils.newInstance().getClasses(PACKAGE_NAME);
            for (Class item:classes) {
                GeneratorType annotation = (GeneratorType)item.getAnnotation(GeneratorType.class);
                if (annotation==null){
                    return null;
                }
                DbType myDbType = annotation.dbType();
                if (myDbType.getDb().equals(dbType.getDb())){
                    return  (GeneratorService)Class.forName(item.getName()).newInstance();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
