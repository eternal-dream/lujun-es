package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.ArmClcClassInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 中图分类表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-09-08
 */
public interface ArmClcClassInfoService extends IService<ArmClcClassInfo> {

    /**
     * 获取中图分类的树形菜单
     * @author Innocence
     * @date 2021/9/22
     * @return java.util.List<com.cqvip.innocence.project.model.entity.ArmClcClassInfo>
     */
    List<ArmClcClassInfo> getTreeData();

}
