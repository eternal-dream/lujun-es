package com.cqvip.innocence.project.mongoservice;


/**
 * @ClassName MongoService
 * @Description MongoDB操作接口,操作不同的库，暂无统一实体接收返回值
 * @Author Innocence
 * @Date 2021/8/24 14:04
 * @Version 1.0
 */
public interface MongoService<T extends Object> {

    /**
     * 根据id查询
     * @author Innocence
     * @date 2021/8/24
     * @param id
     * @param objName 对象名称
     * @return java.lang.Object
     */
    T getEntityById(String id,Class<T> clazz,String objName);

}
