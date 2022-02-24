package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.mapper.ArmAdminUserLogMapper;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.mapper.ArmAdminUserMapper;
import com.cqvip.innocence.project.model.entity.ArmAdminUserLog;
import com.cqvip.innocence.project.model.entity.ArmAdminUserRole;
import com.cqvip.innocence.project.service.ArmAdminUserRoleService;
import com.cqvip.innocence.project.service.ArmAdminUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 管理用户表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
@Service
@Transactional
public class ArmAdminUserServiceImpl extends ServiceImpl<ArmAdminUserMapper, ArmAdminUser> implements ArmAdminUserService {

    @Autowired
    private ArmAdminUserLogMapper logMapper;

    @Autowired
    private ArmAdminUserRoleService adminUserRoleService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteByIdWithXml(Serializable logId,Serializable userId) {
        int i = logMapper.deleteByIdToXml(logId);
        int i1 = baseMapper.deleteByIdToXml(userId);
        if (i == i1 && i == 1){
            return 1;
        }else {
            return 0;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean restore(ArmAdminUserLog log) {
        int i = baseMapper.restoreAdminUser(log.getObjId());
        int i1 = logMapper.deleteByIdToXml(log.getId());
        if (i == i1 && i == 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean addOrModifyAdminUser(ArmAdminUser adminUser) {
        this.saveOrUpdate(adminUser);
        QueryWrapper<ArmAdminUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("ADMIN_USER_ID",adminUser.getId());
        adminUserRoleService.remove(wrapper);
        String roleIds = adminUser.getRoleIds();
        if(StringUtils.isNotBlank(roleIds)){
            String[] roleIdArr = roleIds.split(",");
            for (String roleId : roleIdArr) {
                ArmAdminUserRole adminUserRole = new ArmAdminUserRole();
                adminUserRole.setRoleId(Long.valueOf(roleId));
                adminUserRole.setAdminUserId(adminUser.getId());
                adminUserRoleService.save(adminUserRole);
            }
        }
        return true;
    }

    @Override
    public List<ArmAdminUser> getAdminUserList(ArmAdminUser adminUser, Page pageParams) {
        return baseMapper.getAdminUserList(adminUser,pageParams);
    }

}
