package com.cqvip.innocence.tests;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cqvip.innocence.common.util.encryption.SM2Util;
import com.cqvip.innocence.project.esservice.DocumentService;
import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import com.cqvip.innocence.project.model.entity.ArticleDoc;
import com.cqvip.innocence.project.service.ArmUserInfoService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.Map;

/**
 * @ClassName Test111
 * @Description TODO
 * @Author Innocence
 * @Date 2021/8/31 16:26
 * @Version 1.0
 */
@SpringBootTest
public class Test111 {

    @Autowired
    private DocumentService<ArticleDoc> service;

    @Autowired
    private ArmUserInfoService userInfoService;

    @Test
    public void test(){
        String essql= "repeat:true";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.queryStringQuery(essql));
        NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).withPageable(PageRequest.of(0, 10)).build();

        Map<String, Object> pageList = service.getPageList(build, PageRequest.of(0, 10), ArticleDoc.class);
        System.out.println("pageList = " + pageList);
    }


    @Test
    public void test2(){
        SM2Util sm2Util = new SM2Util();
        String encrypt = sm2Util.encrypt("Abc123", SM2Util.PUBLICKEY);
        LambdaUpdateWrapper<ArmUserInfo> test2 = new UpdateWrapper<ArmUserInfo>().lambda().eq(ArmUserInfo::getLoginName, "test2").set(ArmUserInfo::getLoginPassword, encrypt);
        boolean update = userInfoService.update(test2);
        System.out.println("update = " + update);
    }
}
