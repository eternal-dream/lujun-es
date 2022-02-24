package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.ArmNewJournal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 新刊表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-14
 */
public interface ArmNewJournalService extends IService<ArmNewJournal> {

    /**
     * 功能描述
     * @author Innocence
     * @date 2021/9/14
     * @param ids
     * @return java.lang.Boolean
     */
    Boolean deleteByIds(List<Serializable> ids);
}
