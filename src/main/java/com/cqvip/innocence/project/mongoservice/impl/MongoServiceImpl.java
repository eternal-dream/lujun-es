package com.cqvip.innocence.project.mongoservice.impl;

import com.cqvip.innocence.project.mongoservice.MongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;


/**
 * @ClassName MongoServiceImpl
 * @Description TODO
 * @Author Innocence
 * @Date 2021/8/24 14:23
 * @Version 1.0
 */
@Service
public class MongoServiceImpl<T> implements MongoService<T> {

    @Autowired
    private MongoTemplate template;

    /**
     *  ARTICLE_TITLE 是晓菲部署好的MongoDB里面的成品数据表
     *  ORGIN_TITLE 是未处理的原始数据表数据表
     * @author Innocence
     * @date 2021/8/24
     */
    public static final String ORGIN_TITLE = "OrginTitle";
    public static final String ARTICLE_TITLE = "ArticleTitle";

    @Override
    public T getEntityById(String id,Class<T> clazz,String objName) {
        Query query = new Query(Criteria.where("_id").is(id));
        return template.findOne(query,clazz,objName);
    }
}
