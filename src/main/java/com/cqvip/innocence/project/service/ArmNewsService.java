package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.entity.ArmNews;
import com.cqvip.innocence.project.model.entity.ArmNewsLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新闻表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
public interface ArmNewsService extends IService<ArmNews> {
    /**
     * 获取News分页列表数据
     * @param armNews
     * @param page
     * @param beginTime
     * @param endTime
     * @param order 排序方式 {synthetical:综合，createTime创建时间倒序，hot：下载量与浏览量倒序}
     * @return
     */
    Page<ArmNews> getNewsByPage(ArmNews armNews, Date beginTime, Date endTime, Page page,String order);

    /**
     * 根据id彻底删除新闻（物理删除）
     * @author Innocence
     * @date 2021/8/25
     * @param newsId
     * @param logId
     * @return int
     */
    int deleteByIdWithXml( Serializable newsId,Serializable logId);

    /**
     * 根据id恢复新闻（从回收站恢复）
     * @author Innocence
     * @date 2021/8/25
     * @param log
     * @return boolean
     */
    boolean restoreNews(ArmNewsLog log);

    boolean deleteByIdsWithTransaction(Long[] ids, HttpServletRequest request);

    /**
     * 获取首页所需的新闻公告显示
     * @author Innocence
     * @date 2021/10/19
     * @return java.util.List<com.cqvip.innocence.project.model.entity.ArmNews>
     */
    List<Map<String,Object>> getNewsListToIndex();

    /**
     * 前台获取新闻公告分页
     * @author Innocence
     * @date 2021/10/20
     * @param news
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.cqvip.innocence.project.model.entity.ArmNews>
     */
    Page<ArmNews> getNewsPageList(Page page);

    /**
     * 软件下载及其他相关逻辑操作
     * @param id 软件资源id
     * @param request
     * @param response
     * @throws UnsupportedEncodingException
     */
    void downloadAnnexAndOtherToDo(Long id, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException;
}
