package com.cqvip.generator.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.Controller;
import com.baomidou.mybatisplus.generator.config.builder.Entity;
import com.baomidou.mybatisplus.generator.config.builder.GeneratorBuilder;
import com.baomidou.mybatisplus.generator.config.builder.Service;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.cqvip.innocence.common.util.yml.YmlReadUtil;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

import static com.cqvip.innocence.common.constant.ConfigConstant.GENERATOR_CONFIG_FILE;
import static com.cqvip.innocence.common.constant.ConfigConstant.YML_FILE_NAME;

/**
 * 代码生成器工厂抽象类
 * @author Innocence
 * @date 2020/10/28
 */
public abstract class GeneratorService {

    public static final String URL = YmlReadUtil.newInstance().getValueToString(YML_FILE_NAME,"spring.datasource.url");
    public static final String USER_NAME =  YmlReadUtil.newInstance().getValueToString(YML_FILE_NAME,"spring.datasource.username");
    public static final String PASS_WORD =  YmlReadUtil.newInstance().getValueToString(YML_FILE_NAME,"spring.datasource.password");

    /**
     * 一些公共配置信息
     * @author Innocence
     * @date 2020/10/28
     */
    public static final String AUTHOR = YmlReadUtil.newInstance().getValueToString(GENERATOR_CONFIG_FILE,"public.author");
    public static final String MODULE_NAME = YmlReadUtil.newInstance().getValueToString(GENERATOR_CONFIG_FILE,"public.moduleName");
    public static final String PARENT = YmlReadUtil.newInstance().getValueToString(GENERATOR_CONFIG_FILE,"public.parent");
    public static final String ENTITY = YmlReadUtil.newInstance().getValueToString(GENERATOR_CONFIG_FILE,"public.entity");

    public static final String PROJECT_PATH = System.getProperty("user.dir");

    /**
     * 读取控制台内容
     * @param tip
     * @return
     */
    protected String scanner(String tip){
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 全局配置
     * @author Innocence
     * @date 2021/1/4
     * @return com.baomidou.mybatisplus.generator.config.GlobalConfig
     */
    protected GlobalConfig setGlobal(){
        GlobalConfig.Builder builder = GeneratorBuilder.globalConfigBuilder();
        //是否重写生成的文件，默认为false，这里指定后改为true
//        builder.fileOverride();
        builder.outputDir(PROJECT_PATH + "/src/main/java");
        builder.author(AUTHOR);
        //开启Swagger注解
        builder.enableSwagger();
        return builder.build();
    }

    /**
     * 全局数据配置
     * @author Innocence
     * @date 2021/1/4
     * @return com.baomidou.mybatisplus.generator.config.DataSourceConfig
     */
    protected DataSourceConfig setDataSource(IDbQuery dbQuery,String schemaName){
        DataSourceConfig.Builder builder = new DataSourceConfig.Builder(
                URL,
                USER_NAME,
                PASS_WORD
        );
        if (StrUtil.isNotBlank(schemaName)){
            builder.schema(schemaName);
        }
        builder.dbQuery(dbQuery);
        return builder.build();
    }

    /**
     * 全局策略配置
     * @author Innocence
     * @date 2021/1/4
     * @return com.baomidou.mybatisplus.generator.config.StrategyConfig
     */
    protected StrategyConfig setStrategy(String[] superEntityColumns){
        StrategyConfig.Builder builder = new StrategyConfig.Builder();
        builder.addInclude(scanner("表名，多个英文逗号分割").split(","));

        builder.enableCapitalMode();
        //实体策略配置
        Entity.Builder entityBuilder = builder.entityBuilder();
        entityBuilder.naming(NamingStrategy.underline_to_camel);
        entityBuilder.superClass(BaseModel.class);
        entityBuilder.enableLombok();
        entityBuilder.enableSerialVersionUID();
        entityBuilder.enableChainModel();
        entityBuilder.enableTableFieldAnnotation();
        entityBuilder.columnNaming(NamingStrategy.underline_to_camel);
        entityBuilder.addSuperEntityColumns(superEntityColumns);
        //Controller策略配置
        Controller.Builder controllerBuilder = builder.controllerBuilder();
        controllerBuilder.superClass(AbstractController.class);
        controllerBuilder.enableRestStyle();
        controllerBuilder.enableHyphenStyle();
        //Service策略配置
        Service.Builder serviceBuilder = builder.serviceBuilder();
        serviceBuilder.convertServiceFileName(entityName -> entityName+"Service");
        return builder.build();
    }

    /**
     * 全局包配置
     * @author Innocence
     * @date 2021/1/4
     * @return com.baomidou.mybatisplus.generator.config.PackageConfig
     */
    protected PackageConfig setPackage(){
        PackageConfig.Builder builder = new PackageConfig.Builder();
        //模块名
        builder.moduleName(MODULE_NAME);
        //包名
        builder.parent(PARENT);
        builder.entity(ENTITY);
        return builder.build();
    }

    /**
     * 全局自定义配置
     * @author Innocence
     * @date 2021/1/4
     * @return com.baomidou.mybatisplus.generator.InjectionConfig
     * 如果模板引擎是 freemarker
     * String templatePath = "/templates/mapper.xml.ftl";
     * 如果模板引擎是 velocity
     * String templatePath = "/templates/mapper.xml.vm";
     */
    protected InjectionConfig setInjection(){
        return new InjectionConfig.Builder().build();
    }

    /**
     * 全局模板配置
     * @author Innocence
     * @date 2021/1/4
     * @return com.baomidou.mybatisplus.generator.config.TemplateConfig
     */
    protected TemplateConfig setTemplate(){
        return new TemplateConfig.Builder().build();
    }
    /**
     * 代码生成器主要方法，子类根据不同的数据库实现该方法
     * @author Innocence
     * @date 2020/10/28
     */
    public abstract void generator();
}
