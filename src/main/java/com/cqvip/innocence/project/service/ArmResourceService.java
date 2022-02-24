package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.dto.ResourceNode;
import com.cqvip.innocence.project.model.entity.ArmResource;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.entity.ArmRole;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
public interface ArmResourceService extends IService<ArmResource> {

    List<ArmResource> getRoleResources(ArmRole role);

    /**
     * 获取资源TreeData
     *
     * @return {@link List< ResourceNode>}
     * @author 01
     * @date 2021/9/2 18:22
     */
    List<ResourceNode> getResourceTreeData();
}
