package com.cqvip.innocence.common.util.encryption;

import com.cqvip.innocence.common.util.BaseUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @ClassName TestUtil
 * @Description TODO
 * @Author Innocence
 * @Date 2020/7/21 16:34
 * @Version 1.0
 */
public class Md5Util {

    public static Md5Util newInstance() {
        return (Md5Util)BaseUtil.instance(Md5Util.class.getName());
    }

    /**
     * 用户名密码MD5加密方法
     * @author Innocence
     * @date 2020/7/21
     * @param userName 用户名
     * @param passWord 密码
     * @return java.lang.String
     */
    public String encodePassword(String userName,String passWord){
        String hashAlgorithName = "MD5";
        int hashIterations = 1024;
        String password =passWord;
        ByteSource credentialsSalt = ByteSource.Util.bytes(userName+"salt");
        String obj =String.valueOf(new SimpleHash(hashAlgorithName, password, credentialsSalt, hashIterations));
        return obj;
    }


}
