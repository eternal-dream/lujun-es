package com.cqvip.innocence.common.sessions;

/**
 * @ClassName SessionKeys
 * @Description 一些常用的sessionkey值
 * @Author Innocence
 * @Date 2020/7/25
 * @Version 1.0
 */
public final class SessionKeys {

    /**
     * 当前登录的管理员
     */
    public static final String LOGIN_ADMIN_KEY="LOGIN_ADMIN_KEY";

    /**
     * 当前登录的前台用户
     */
    public static final String LOGIN_USER_KEY="LOGIN_USER_KEY";

    /**
     * 验证码使用的keys，
     * 配合SessionId使用
     */
    public static class VcodeKeys{

        /**
         * 验证码创建时间key
         */
        public static final String CREAT_TIME="CREAT_TIME";

        /**
         * 当前验证码
         */
        public static final String CODE="CODE";

        /**
         * 验证码有效时间（毫秒）
         */
        public static final int EXPIRETION_TIME=5*1000*60;
    }
}
