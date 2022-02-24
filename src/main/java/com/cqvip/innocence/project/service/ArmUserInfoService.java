package com.cqvip.innocence.project.service;

import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
public interface ArmUserInfoService extends IService<ArmUserInfo> {

 boolean addOrModifyUserInfo(ArmUserInfo user);

}
