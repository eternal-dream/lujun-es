package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.ArmNewsClass;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 发布内容分类表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
public interface ArmNewsClassService extends IService<ArmNewsClass> {
    /**
     * 通过栏目名获取栏目对应分类
     * @param columnName 栏目名
     * @return
     */
    List<ArmNewsClass> getSoftwareClassByColumnName(String columnName);
}
