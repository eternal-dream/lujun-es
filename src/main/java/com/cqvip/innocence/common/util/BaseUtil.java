package com.cqvip.innocence.common.util;


import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BaseUtil
 * @Description 工具类基类
 * @Author Innocence
 * @Date 2020/7/21 15:59
 * @Version 1.0
 */
public class BaseUtil{

    private static Map<String, Object> instance = new HashMap<>();

    public static Object instance(String objName)  {
        if (instance.get(objName) == null || !(instance.get(objName) instanceof BaseUtil)) {
            try {
                instance.put(objName, Class.forName(objName).newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance.get(objName);
    }


}
