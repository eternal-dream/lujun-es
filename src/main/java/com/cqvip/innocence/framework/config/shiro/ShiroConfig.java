package com.cqvip.innocence.framework.config.shiro;

import com.cqvip.innocence.common.util.yml.YmlReadUtil;
import com.cqvip.innocence.framework.filter.MyFormAuthoricationFilter;
import com.cqvip.innocence.common.sessions.MySessionManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

import static com.cqvip.innocence.common.constant.ConfigConstant.SHIRO_CONFIG_FILE;

/**
 * @ClassName ShiroConfig
 * @Description shiro配置
 * @Author Innocence
 * @Date 2020/7/14 10:35
 * @Version 1.0
 */
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.shiro.host}")
    private String host;

    @Value("${spring.redis.shiro.port}")
    private int port;

    @Value("${spring.redis.shiro.timeout}")
    private int timeout;

    private String excludesUrl= YmlReadUtil.newInstance().getValueToString(SHIRO_CONFIG_FILE,"shiro.excludes");

    private List<String> excludesList;

    {
        excludesList = Arrays.asList(excludesUrl.split(","));
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 创建ShrioFilterFactoryBean
     * @param securityManager
     * @return
     */
    @Bean(name = "shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边
        Map<String, String> filterMap = new LinkedHashMap<>();
        /**
         * Shiro内置过滤器，可以实现权限相关的拦截器，常用的有：
         *   anon：无需认证（登录）即可访问
         *   authc：必须认证才可以访问
         *   user：如果使用rememberme的功能可以直接访问
         *   perms：该资源必须得到资源权限才能访问
         *   roles ：该资源必须得到角色资源才能访问
         */
//        filterMap.put("/admin/**", "authc,roles[超级管理员]");
        //放过一些不需要登录的请求，后续根据实际需求进行添加
        for (String item:excludesList) {
            filterMap.put(item.trim(),"anon");
        }
        //其他资源全部拦截
        filterMap.put("/**",  "authc");
        //shiro的未认证，未授权的信息以json返回给ajax
        shiroFilterFactoryBean.getFilters().put("authc",new MyFormAuthoricationFilter());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);

//        如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
//        配置shiro默认登录地址，前后端分离应该由前端控制页面跳转
        shiroFilterFactoryBean.setLoginUrl("/unauth");
        return shiroFilterFactoryBean;
    }

    /**
     * 创建DefaultWebSecurityManager
     * SecurityManager 是 Shiro 架构的核心，通过它来链接Realm和用户(文档中称之为Subject.)
     * @return DefaultWebSecurityManager
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //注入rememberme对象
        securityManager.setRememberMeManager(cookieRememberMeManager());
        //注入session
        securityManager.setSessionManager(sessionManager());
        //注入缓存管理对象
        securityManager.setCacheManager(redisCacheManager());
        //将Realm注入SecurityManager
        securityManager.setRealm(userRealm());
        return securityManager;
    }

    /**
     * 创建Realm
     * @return  AdminRealm
     */
    @Bean(name = "userRealm")
    public AdminRealm userRealm(){
        AdminRealm adminRealm = new AdminRealm();
        //设置解密规则
        adminRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        adminRealm.setCachingEnabled(false);
        return adminRealm;
    }

    /**
     * 因为密码是加过密的，所以，如果要Shiro验证用户身份的话，需要告诉它我们用的是md5加密的，并且是加密了两次。
     * 同时我们在自己的Realm中也通过SimpleAuthenticationInfo返回了加密时使用的盐。
     * 这样Shiro就能顺利的解密密码并验证用户名和密码是否正确了。
     * @return  HashedCredentialsMatcher
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //使用MD5散列算法
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        // storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * 自定义sessionmanager
     * @return SessionManager
     */
    @Bean(name = "sessionManager")
    public SessionManager sessionManager(){
        MySessionManager mySessionManager = new MySessionManager();
        //取消登陆跳转URL后面的jsessionid参数
        mySessionManager.setSessionIdUrlRewritingEnabled(false);
        mySessionManager.setSessionDAO(redisSessionDAO());
        //不过期设置-1，不设置则为默认的180000毫秒
//        mySessionManager.setGlobalSessionTimeout(1000*60*30);
        return mySessionManager;
    }

    /**
     * 配置shiro redisManager
     * 使用shiro-redis开源插件
     * @return RedisManager
     */
    @Bean(name = "redisManager")
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host+":"+port);
        redisManager.setTimeout(timeout);
//        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * cacheManager的Redis实现
     * @return RedisCacheManager
     */
    @Bean(name = "redisCacheManager")
    public RedisCacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用shiro-redis开源插件
     * @return RedisSessionDAO
     */
    @Bean(name = "redisSessionDAO")
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * 启shiro 的aop注解支持
     * @param securityManager
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * cookie对象
     * @return SimpleCookie
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        logger.info("ShiroConfiguration.rememberMeCookie()=============");
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    /**
     * cookie管理对象
     * @return CookieRememberMeManager
     */
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        logger.info("ShiroConfiguration.rememberMeManager()========");
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCookie(rememberMeCookie());
        return manager;
    }

    /**
     * 加入下面两个bean，shiro才会执行授权逻辑
     * @return DefaultAdvisorAutoProxyCreator
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}
