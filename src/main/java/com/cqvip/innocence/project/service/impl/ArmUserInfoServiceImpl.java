package com.cqvip.innocence.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.common.util.encryption.SM2Util;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import com.cqvip.innocence.project.mapper.ArmUserInfoMapper;
import com.cqvip.innocence.project.service.ArmAdminUserService;
import com.cqvip.innocence.project.service.ArmUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
@Service
@Transactional
public class ArmUserInfoServiceImpl extends ServiceImpl<ArmUserInfoMapper, ArmUserInfo> implements ArmUserInfoService {

 @Autowired
 private ArmAdminUserService adminUserService;


 @Override
 public boolean addOrModifyUserInfo(ArmUserInfo user) {
  String loginPassword = user.getLoginPassword();
  SM2Util sm2Util = new SM2Util();
  if(StringUtils.isNotBlank(loginPassword)){
   String decryptPassword = sm2Util.decrypt(loginPassword, SM2Util.PRIAVTEKEY);
   user.setLoginPassword(sm2Util.encrypt(decryptPassword,SM2Util.PUBLICKEY));
  }else{
   user.setLoginPassword(null);
  }
  boolean saveOrUpdate = this.saveOrUpdate(user);
  if(user.getAdminUserId()==null){
   return saveOrUpdate;
  }
  // 修改关联账号信息
  ArmAdminUser adminuser = adminUserService.getById(user.getAdminUserId());
  if(adminuser == null){
   return saveOrUpdate;
  }
  adminuser.setLoginName(user.getLoginName());
  adminuser.setLoginPassword(user.getLoginPassword());
  adminuser.setRealName(user.getReaderName());
  adminuser.setPhoneNumber(user.getPhoneNumber());
  adminuser.setAvatar(user.getAvatar());
  adminuser.setEmail(user.getEmail());
  boolean updateById = adminUserService.updateById(adminuser);
  return saveOrUpdate && updateById;
 }
}
