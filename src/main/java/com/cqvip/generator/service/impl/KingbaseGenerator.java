package com.cqvip.generator.service.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.querys.KingbaseESQuery;
import com.cqvip.generator.constant.GeneratorType;
import com.cqvip.generator.service.GeneratorService;
import com.cqvip.innocence.common.util.yml.YmlReadUtil;


import static com.cqvip.innocence.common.constant.ConfigConstant.GENERATOR_CONFIG_FILE;

/**
 * @ClassName KingbaseGenerator
 * @Description 人大金仓
 * @Author Innocence
 * @Date 2020/10/28 11:01
 * @Version 1.0
 */
@GeneratorType(dbType = DbType.KINGBASE_ES)
public class KingbaseGenerator extends GeneratorService {

    public static String[] superEntityColumns=new String[]{"ID","CREATE_TIME","MODIFY_TIME"};
    public static final String SCHEMA_NAME = YmlReadUtil.newInstance().getValueToString(GENERATOR_CONFIG_FILE,"kingbase.schemaName");
    @Override
    public void generator(){
        AutoGenerator autoGenerator = new AutoGenerator(setDataSource(new KingbaseESQuery(),SCHEMA_NAME));
        autoGenerator.global(setGlobal());
        autoGenerator.packageInfo(setPackage());
        autoGenerator.strategy(setStrategy(superEntityColumns));
        autoGenerator.template(setTemplate());
        autoGenerator.injection(setInjection());
        autoGenerator.execute();
    }
}
