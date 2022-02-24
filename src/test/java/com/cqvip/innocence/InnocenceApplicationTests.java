package com.cqvip.innocence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootTest
class InnocenceApplicationTests {
    @Autowired
    DataSource dataSource;
    @Test
    void contextLoads() throws Exception{
        System.out.println("连接成功");
        System.out.println("dataSource.getClass()内容***"+dataSource.getClass());

        Connection connection = dataSource.getConnection();
        System.out.println("connection内容***"+connection);
        connection.close();
    }

}
