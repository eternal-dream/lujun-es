package com.cqvip.generator.service.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.querys.DMQuery;
import com.cqvip.generator.constant.GeneratorType;
import com.cqvip.generator.service.GeneratorService;
import com.cqvip.innocence.common.util.yml.YmlReadUtil;

import static com.cqvip.innocence.common.constant.ConfigConstant.GENERATOR_CONFIG_FILE;


/**
 * @ClassName DaMengGenerator
 * @Description TODO    达梦数据库
 * @Author INNOCENCE
 * @Date 2021/8/12 14:25
 * @Version 1.0
 */
@GeneratorType(dbType = DbType.DM)
public class DaMengGenerator extends GeneratorService {

    public static String[] superEntityColumns=new String[]{"ID","CREATE_TIME","MODIFY_TIME"};
    public static final String SCHEMA_NAME = YmlReadUtil.newInstance().getValueToString(GENERATOR_CONFIG_FILE,"dameng.schemaName");
    @Override
    public void generator() {
        AutoGenerator autoGenerator = new AutoGenerator(setDataSource(new DMQuery(),SCHEMA_NAME));
        autoGenerator.global(setGlobal());
        autoGenerator.packageInfo(setPackage());
        autoGenerator.strategy(setStrategy(superEntityColumns));
        autoGenerator.template(setTemplate());
        autoGenerator.injection(setInjection());
        autoGenerator.execute();
    }

}
