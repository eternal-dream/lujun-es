package com.cqvip.innocence.project.esservice;

/**
 * @ClassName IndexService
 * @Description 索引的增删改查
 * @Author Innocence
 * @Date 2021/3/1 14:06
 * @Version 1.0
 */
public interface IndexService<T>{

    /**
     * 根据类创建索引
     * @author Innocence
     * @date 2021/6/16
     * @param clazz
     * @return java.lang.Boolean
     */
    Boolean createIndexByClass(Class<T> clazz);

    /**
     * 根据名称创建索引
     * 不建议使用，因为很多设置需要自己写配置文件
     * @author Innocence
     * @date 2021/3/1
     * @param indexName
     * @return java.lang.Boolean
     */
    Boolean createIndexByName(String indexName);

    /**
     * 根据索引名判断索引是否存在
     * @author Innocence
     * @date 2021/6/16
     * @param indexName 索引名
     * @return java.lang.Boolean
     */
    Boolean isIndexExist(String indexName);

    /**
     * 根据索引名删除索引
     * @author Innocence
     * @date 2021/6/16
     * @param indexName 索引名
     * @return java.lang.Boolean
     */
    Boolean deleteIndexByName(String indexName);

    /**
     * 获取被标记的索引名
     * @author Innocence
     * @date 2021/6/16
     * @param clazz
     * @return java.lang.String
     */
    String getIndexName(Class<T> clazz);
}
