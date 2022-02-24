package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.ArmRole;
import com.cqvip.innocence.project.mapper.ArmRoleMapper;
import com.cqvip.innocence.project.service.ArmRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@Service
public class ArmRoleServiceImpl extends ServiceImpl<ArmRoleMapper, ArmRole> implements ArmRoleService {

 @Override
 public List<ArmRole> getRolesByAdmin(ArmAdminUser adminUser) {
  return baseMapper.getRolesByAdmin(adminUser);
 }

 @Override
 public List<ArmRole> getRoles(String name, Page pageParams) {
  return baseMapper.getRoles(name,pageParams);
 }
}
