package com.cqvip.generator.service.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.cqvip.generator.constant.GeneratorType;
import com.cqvip.generator.service.GeneratorService;


/**
 * @ClassName MysqlGenerator
 * @Description mysql代码生成
 * @Author Innocence
 * @Date 2021/1/6 9:56
 * @Version 1.0
 */
@GeneratorType(dbType = DbType.MYSQL)
public class MysqlGenerator extends GeneratorService {

    public static String[] superEntityColumns=new String[]{"id","create_time","modify_time"};

    @Override
    public void generator() {
        AutoGenerator autoGenerator = new AutoGenerator(setDataSource(new MySqlQuery(),null));
        autoGenerator.global(setGlobal());
        autoGenerator.packageInfo(setPackage());
        autoGenerator.strategy(setStrategy(superEntityColumns));
        autoGenerator.template(setTemplate());
        autoGenerator.injection(setInjection());
        autoGenerator.execute();
    }
}
