package com.cqvip.innocence.tests;

import com.baomidou.mybatisplus.annotation.DbType;
import com.cqvip.generator.factory.GeneratorFactory;
import com.cqvip.generator.service.GeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @ClassName FactoryTest
 * @Description TODO
 * @Author Innocence
 * @Date 2020/10/28 13:32
 * @Version 1.0
 */
@SpringBootTest
public class FactoryTest {

    @Test
    public void test(){
        GeneratorFactory factory = new GeneratorFactory();
        GeneratorService generator = factory.getGenerator(DbType.DM);
        generator.generator();
    }

    public static void main(String[] args) {
        GeneratorFactory factory = new GeneratorFactory();
        GeneratorService generator = factory.getGenerator(DbType.DM);
        generator.generator();
    }
}
