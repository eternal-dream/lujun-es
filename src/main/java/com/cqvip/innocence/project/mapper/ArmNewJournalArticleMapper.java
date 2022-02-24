package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmNewJournalArticle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新刊发布内容表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-09-14
 */
public interface ArmNewJournalArticleMapper extends BaseMapper<ArmNewJournalArticle> {

    Page<ArmNewJournalArticle> getPageList(Page page, @Param("id") Long journalId,@Param("title") String title);

    ArmNewJournalArticle getDetailById(Long id);

    List<Map> getNewJournalArticles(@Param("page") Page paeParams);
}
