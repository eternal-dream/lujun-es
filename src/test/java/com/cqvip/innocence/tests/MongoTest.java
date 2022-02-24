package com.cqvip.innocence.tests;

import com.cqvip.innocence.project.mongoservice.MongoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName MongoTest
 * @Description TODO
 * @Author Innocence
 * @Date 2021/8/24 14:13
 * @Version 1.0
 */
@SpringBootTest
public class MongoTest {

    @Autowired
    private MongoService<Object> mongoService;

    @Test
    public void test(){
        Object objectById = mongoService.getEntityById("5ea8115d48148405bc041b17",Object.class,"ArticleTitle");
        System.out.println("objectById = " + objectById);
    }
}
