package com.cqvip.innocence.project.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;


/**
 * @ClassName AbstractController
 * @Description TODO
 * @Author Innocence
 * @Date 2020/9/22 11:19
 * @Version 1.0
 */
public abstract class AbstractController {

    public final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final Long DEFAULT_PAGE_NUM = 1L;
    private static final Long DEFAULT_PAGE_SIZE = 10L;
    private static final String PAGE_NUM_KEY = "pageNum";
    private static final String PAGE_SIZE_KEY = "pageSize";
    private ThreadLocal<Page> pageParams = new ThreadLocal();
    private ThreadLocal<PageRequest> pageRequest = new ThreadLocal();
    private ThreadLocal<Boolean> initedParamsVariable = new ThreadLocal();
    private ThreadLocal<HttpServletRequest> request = new ThreadLocal();
    private ThreadLocal<HttpServletResponse> response = new ThreadLocal();

    protected void initParamsVariable() {
        Boolean aBoolean = this.initedParamsVariable.get();
        if (aBoolean == null || !aBoolean) {
            this.initedParamsVariable.set(true);
            Long page ;
            Long size ;
            String param = this.getParam(PAGE_NUM_KEY);
            if (StrUtil.isNotBlank(param)){
                page = Long.valueOf(param);
            }else{
                page = DEFAULT_PAGE_NUM;
            }
            String sizeStr = this.getParam(PAGE_SIZE_KEY);
            if (StrUtil.isNotBlank(sizeStr) ) {
                size = Long.valueOf(sizeStr);
            }else{
                size = DEFAULT_PAGE_SIZE;
            }
            this.pageParams.set(new Page(page, size));
            this.initedParamsVariable.remove();
        }
    }

    protected String getParam(String key) {
        return this.getRequest().getParameter(key);
    }

    protected HttpServletRequest getRequest() {
        HttpServletRequest request = this.request.get();
        if (request == null) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            request = ((ServletRequestAttributes)requestAttributes).getRequest();
        }
        return request;
    }

    /**
     * 获取mybatis-plus分页参数
     * @author Innocence
     * @date 2021/8/23
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page
     */
    protected Page getPageParams() {
        this.initParamsVariable();
        return this.pageParams.get();
    }

}
