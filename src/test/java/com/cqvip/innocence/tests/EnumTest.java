package com.cqvip.innocence.tests;

import com.cqvip.innocence.project.model.entity.DbInfoResource;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.DbInfoResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName EnumTest
 * @Description TODO
 * @Author Innocence
 * @Date 2021/8/19 15:34
 * @Version 1.0
 */
@SpringBootTest
public class EnumTest {
    @Autowired
    private DbInfoResourceService resourceService;

    @Test
    public void test(){
        DbInfoResource byId = resourceService.getById(1428272559115042817L);
        System.out.println("list = " + byId);
    }

    @Test
    public void test1(){
        DbInfoResource dbInfoResource = new DbInfoResource();
        dbInfoResource.setTitle("test222");
        dbInfoResource.setType(VipEnums.ResourceType.SYSTEM);
        boolean save = resourceService.save(dbInfoResource);
        System.out.println("save = " + save);
    }
}
