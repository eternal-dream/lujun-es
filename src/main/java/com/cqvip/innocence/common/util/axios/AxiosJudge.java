package com.cqvip.innocence.common.util.axios;


import com.cqvip.innocence.common.util.BaseUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName AxiosJudge
 * @Description 判定是否是axios和ajax请求
 * @Author Innocence
 * @Date 2020/9/11 9:25
 * @Version 1.0
 */
public class AxiosJudge extends BaseUtil {

    public static AxiosJudge newInstance() {
        return (AxiosJudge) BaseUtil.instance(AxiosJudge.class.getName());
    }

    /**
     * 判断是否是ajax和axios请求
     * axios需要自己在前端给请求头添加headers['X-Requested-With']='XMLHttpRequest';
     * @author Innocence
     * @date 2020/9/11
     * @param request
     * @return boolean
     */
    public boolean isAjax(HttpServletRequest request){
        return  (request.getHeader("X-Requested-With") != null &&
                "XMLHttpRequest".equals( request.getHeader("X-Requested-With").toString())) ;
    }
}
