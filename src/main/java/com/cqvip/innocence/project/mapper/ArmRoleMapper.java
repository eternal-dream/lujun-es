package com.cqvip.innocence.project.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.ArmRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
public interface ArmRoleMapper extends BaseMapper<ArmRole> {

    /**
     * 根据管理员获取角色列表
     *
     * @param adminUser
     * @return java.util.List<com.cqvip.innocence.project.model.entity.ArmRole>
     * @author Innocence
     * @date 2021/8/26
     */
    List<ArmRole> getRolesByAdmin(ArmAdminUser adminUser);

    List<ArmRole> getRoles(@Param("name") String name,@Param("page") Page pageParams);
}
