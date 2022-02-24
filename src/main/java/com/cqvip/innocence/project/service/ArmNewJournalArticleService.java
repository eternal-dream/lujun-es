package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmNewJournalArticle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新刊发布内容表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-14
 */
public interface ArmNewJournalArticleService extends IService<ArmNewJournalArticle> {

    /**
     * 获取新闻发布内容分页
     * @author Innocence
     * @date 2021/9/14
     * @param page
     * @param journalId
     * @param title
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.cqvip.innocence.project.model.entity.ArmNewJournalArticle>
     */
    Page<ArmNewJournalArticle> getPageList(Page page,Long journalId, String title);

    /**
     * 根据id获取文献详情
     * @author Innocence
     * @date 2021/10/20
     * @param id
     * @return com.cqvip.innocence.project.model.entity.ArmNewJournalArticle
     */
    ArmNewJournalArticle getDetailById(Long id);

 /**
  * 新刊推荐
  * @param pageParams
  * @return
  */
 List<Map> getNewJournalArticles(Page pageParams);
}
