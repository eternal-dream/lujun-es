package com.cqvip.generator.service.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.querys.PostgreSqlQuery;
import com.cqvip.generator.constant.GeneratorType;
import com.cqvip.generator.service.GeneratorService;
import com.cqvip.innocence.common.util.yml.YmlReadUtil;


import static com.cqvip.innocence.common.constant.ConfigConstant.GENERATOR_CONFIG_FILE;

/**
 * @ClassName PostgresqlGenerator
 * @Description postgresql代码生成
 * @Author Innocence
 * @Date 2020/10/28 10:15
 * @Version 1.0
 */
@GeneratorType(dbType = DbType.POSTGRE_SQL)
public class PostgresqlGenerator extends GeneratorService {

    public static String[] superEntityColumns=new String[]{"id","create_time","modify_time"};

    public static final String SCHEMA_NAME = YmlReadUtil.newInstance().getValueToString(GENERATOR_CONFIG_FILE,"postgresql.schemaName");

    @Override
    public void generator(){
        AutoGenerator autoGenerator = new AutoGenerator(setDataSource(new PostgreSqlQuery(),SCHEMA_NAME));
        autoGenerator.global(setGlobal());
        autoGenerator.packageInfo(setPackage());
        autoGenerator.strategy(setStrategy(superEntityColumns));
        autoGenerator.template(setTemplate());
        autoGenerator.injection(setInjection());
        autoGenerator.execute();
    }
}
