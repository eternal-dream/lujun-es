package com.cqvip.innocence.project.esservice.impl;

import cn.hutool.core.util.StrUtil;
import com.cqvip.innocence.project.esservice.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;

/**
 * @ClassName IndexServiceImpl
 * @Description  抽象
 * @Author Innocence
 * @Date 2021/3/1 14:24
 * @Version 1.0
 */
@Service
public class IndexServiceImpl<T> implements IndexService<T> {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Override
    public String getIndexName(Class<T> clazz){
        Annotation documentAnnotation = clazz.getDeclaredAnnotation(Document.class);
        if(documentAnnotation==null){
            return null;
        }
        String indexName = ((Document) documentAnnotation).indexName();
        if (StrUtil.isNotBlank(indexName)){
            return indexName;
        }
        return null;
    }

    @Override
    public Boolean createIndexByClass(Class<T> clazz) {
        Boolean indexExist = isIndexExist(getIndexName(clazz));
        if (indexExist){
            return false;
        }
        IndexOperations indexOps = restTemplate.indexOps(clazz);
        boolean result1 = indexOps.create();
        boolean result2 = indexOps.putMapping(indexOps.createMapping(clazz));
        return result1&result2;
    }

    @Override
    public Boolean createIndexByName(String indexName) {
        Boolean indexExist = isIndexExist(indexName);
        if (indexExist){
            return false;
        }
        IndexOperations indexOps = restTemplate.indexOps(IndexCoordinates.of(indexName));
        return indexOps.create();

    }

    @Override
    public Boolean isIndexExist(String indexName) {
        IndexOperations indexOps = restTemplate.indexOps(IndexCoordinates.of(indexName));
        return indexOps.exists();
    }

    @Override
    public Boolean deleteIndexByName(String indexName) {
        IndexOperations indexOps = restTemplate.indexOps(IndexCoordinates.of(indexName));
        return indexOps.delete();

    }
}
