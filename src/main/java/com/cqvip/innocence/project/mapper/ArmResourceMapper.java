package com.cqvip.innocence.project.mapper;

import com.cqvip.innocence.project.model.dto.ResourceNode;
import com.cqvip.innocence.project.model.entity.ArmResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cqvip.innocence.project.model.entity.ArmRole;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
public interface ArmResourceMapper extends BaseMapper<ArmResource> {

    List<ArmResource> getRoleResources(ArmRole role);

    /**
     * 获取一级资源菜单
     *
     * @param
     * @return {@link List< ResourceNode>}
     * @throws
     * @author 01
     * @date 2021/9/2 18:24
     */
    List<ResourceNode> getSuperResources();

    /**
     * 获取非一级资源菜单
     *
     * @param
     * @return {@link List< ResourceNode>}
     * @throws
     * @author 01
     * @date 2021/9/2 18:25
     */
    List<ResourceNode> getNotSuperResources();
}
