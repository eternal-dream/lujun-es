package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.ArmRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
public interface ArmRoleService extends IService<ArmRole> {

 List<ArmRole> getRolesByAdmin(ArmAdminUser adminUser);

 List<ArmRole> getRoles(String name, Page pageParams);
}
