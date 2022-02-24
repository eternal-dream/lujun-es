package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.ArmNewJournalClass;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 新刊简目分类 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-14
 */
public interface ArmNewJournalClassService extends IService<ArmNewJournalClass> {

    /**
     * 根据ids删除新刊分类，会删掉分类下的所有新刊和新刊下的所有文献（如果有）
     * @author Innocence
     * @date 2021/9/14
     * @param ids
     * @return java.lang.Boolean
     */
    Boolean deleteByIds(List<Serializable> ids);

}
