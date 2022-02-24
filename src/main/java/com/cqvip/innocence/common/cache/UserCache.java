package com.cqvip.innocence.common.cache;


import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName UserCache
 * @Description 缓存当前登录的前台用户,后台用户直接用shiro的缓存进行管理
 * @Author Innocence
 * @Date 2020/7/25
 * @Version 1.0
 */
public class UserCache {

    public static Map<Object, HttpSession> userCache = new ConcurrentHashMap<Object, HttpSession>();

    public static HttpSession getSession(Object sessionKey){
        return userCache.get(sessionKey);
    }

    public static Boolean removeSession(Object sessionKey){
        HttpSession remove = userCache.remove(sessionKey);
        if (remove!=null){
            return true;
        }
        return false;
    }
}

