package com.cqvip.innocence.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqvip.innocence.project.model.entity.ArmAdminUserLog;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 管理用户表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
public interface ArmAdminUserService extends IService<ArmAdminUser> {

    /**
     * 根据id彻底删除管理员账户（物理删除）
     * @author Innocence
     * @date 2021/8/25
     * @param logId 日志主键id
     * @param userId 用户主键id
     * @return int
     */
    int deleteByIdWithXml(Serializable logId,Serializable userId);

    /**
     * 恢复管理员账户（从回收站收回）
     * @author Innocence
     * @date 2021/8/25
     * @param log 日志实体
     * @return boolean
     */
    boolean restore(ArmAdminUserLog log);

 /**
  * 添加或修改管理员操作
  * @param adminUser
  * @return
  */
 boolean addOrModifyAdminUser(ArmAdminUser adminUser);

 /**
  * 查询管理员列表
  * @param adminUser
  * @param pageParams
  * @return
  */
 List<ArmAdminUser> getAdminUserList(ArmAdminUser adminUser, Page pageParams);

}
