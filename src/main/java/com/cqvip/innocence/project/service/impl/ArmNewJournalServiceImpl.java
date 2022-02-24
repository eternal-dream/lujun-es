package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.project.mapper.ArmNewJournalArticleMapper;
import com.cqvip.innocence.project.model.entity.ArmNewJournal;
import com.cqvip.innocence.project.mapper.ArmNewJournalMapper;
import com.cqvip.innocence.project.model.entity.ArmNewJournalArticle;
import com.cqvip.innocence.project.service.ArmNewJournalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 新刊表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-14
 */
@Service
public class ArmNewJournalServiceImpl extends ServiceImpl<ArmNewJournalMapper, ArmNewJournal> implements ArmNewJournalService {

    @Autowired
    private ArmNewJournalArticleMapper articleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteByIds(List<Serializable> ids) {
        if (ids.size()>0){
            ids.forEach(item->{
                LambdaQueryWrapper<ArmNewJournalArticle> lambda = new QueryWrapper<ArmNewJournalArticle>().lambda()
                        .eq(ArmNewJournalArticle::getJournalId,item);
                articleMapper.delete(lambda);
            });
            int i = baseMapper.deleteBatchIds(ids);
            return i >= 1;
        }
        return false;
    }
}
