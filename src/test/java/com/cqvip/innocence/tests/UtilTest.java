package com.cqvip.innocence.tests;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.cqvip.innocence.common.util.file.FileUtil;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * @ClassName UtilTest
 * @Description
 * @Author Innocence
 * @Date 2020/8/5
 * @Version 1.0
 */
public class UtilTest {

    public static void main(String[] args) throws IOException {
//        ClassPathResource resource = new ClassPathResource("application.yml");
//        Properties properties = new Properties();
//        properties.load(resource.getStream());
//        System.out.println("properties = " + properties);

//        String s  = "我,是你,爸爸";
//        List<String> list = StrUtil.splitTrim(s, ",");
//        System.out.println("list = " + list);

//        File path = new File(ResourceUtils.getURL("classpath:").getPath());
//        String absolutePath = path.getAbsolutePath();
//        System.out.println("absolutePath = " + absolutePath);

        FileUtil util = FileUtil.newInstance();
        String s = "E:\\projects\\昆明医科大学\\KunmingMedical\\system\\static\\upload\\sqlyog_2982.zip";
        boolean b = util.deleteFile(s);
        System.out.println("b = " + b);
    }

}
