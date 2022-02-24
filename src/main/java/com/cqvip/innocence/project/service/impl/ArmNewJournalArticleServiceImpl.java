package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmNewJournalArticle;
import com.cqvip.innocence.project.mapper.ArmNewJournalArticleMapper;
import com.cqvip.innocence.project.service.ArmNewJournalArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新刊发布内容表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-14
 */
@Service
public class ArmNewJournalArticleServiceImpl extends ServiceImpl<ArmNewJournalArticleMapper, ArmNewJournalArticle> implements ArmNewJournalArticleService {

    @Override
    public Page<ArmNewJournalArticle> getPageList(Page page, Long journalId, String title) {
        return baseMapper.getPageList(page, journalId, title);
    }

    @Override
    public ArmNewJournalArticle getDetailById(Long id) {
        return baseMapper.getDetailById(id);
    }

    @Override
    public List<Map> getNewJournalArticles(Page paeParams) {
        return baseMapper.getNewJournalArticles(paeParams);
    }
}
