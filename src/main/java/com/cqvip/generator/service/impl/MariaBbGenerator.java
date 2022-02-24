package com.cqvip.generator.service.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.querys.MariadbQuery;
import com.cqvip.generator.constant.GeneratorType;
import com.cqvip.generator.service.GeneratorService;

/**
 * @ClassName MariaBbGenerator
 * @Description TODO
 * @Author Innocence
 * @Date 2021/6/30 13:28
 * @Version 1.0
 */
@GeneratorType(dbType = DbType.MARIADB)
public class MariaBbGenerator extends GeneratorService {

    public static String[] superEntityColumns=new String[]{"id","create_time","modify_time"};

    @Override
    public void generator() {
        AutoGenerator autoGenerator = new AutoGenerator(setDataSource(new MariadbQuery(),null));
        autoGenerator.global(setGlobal());
        autoGenerator.packageInfo(setPackage());
        autoGenerator.strategy(setStrategy(superEntityColumns));
        autoGenerator.template(setTemplate());
        autoGenerator.injection(setInjection());
        autoGenerator.execute();
    }
}
