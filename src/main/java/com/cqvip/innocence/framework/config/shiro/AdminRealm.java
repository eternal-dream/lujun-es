package com.cqvip.innocence.framework.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.common.util.encryption.Md5Util;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.ArmResource;
import com.cqvip.innocence.project.model.entity.ArmRole;
import com.cqvip.innocence.project.service.ArmAdminUserService;
import com.cqvip.innocence.project.service.ArmResourceService;
import com.cqvip.innocence.project.service.ArmRoleService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

import static com.cqvip.innocence.project.model.enums.VipEnums.LoginStatus.DISABLE;

/**
 * @ClassName AdminRealm
 * @Description 自定义shrio的Realm
 * @Author Innocence
 * @Date 2020/7/14 11:00
 * @Version 1.0
 */
public class AdminRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Lazy
    @Autowired
    private ArmRoleService roleService;

    @Lazy
    @Autowired
    private ArmResourceService resourceService;

    @Lazy
    @Autowired
    private ArmAdminUserService adminService;

    /**
     * 执行授权逻辑
     * @author Innocence
     * @date 2020/7/14
     * @param principalCollection
     * @return org.apache.shiro.authz.AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        ArmAdminUser admin = (ArmAdminUser)principalCollection.getPrimaryPrincipal();
        if (null != admin){
            List<String> roleList = new ArrayList<>();
            List<String> permissionLists = new ArrayList<>();
            List<ArmRole> roles = roleService.getRolesByAdmin(admin);
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            for (ArmRole role:roles) {
                roleList.add(role.getName());
                List<ArmResource> roleResources = resourceService.getRoleResources(role);
                for (ArmResource resource:roleResources) {
                    permissionLists.add(resource.getPermission());
                }
            }
            info.addRoles(roleList);
            info.addStringPermissions(permissionLists);
            return info;
        }
        return null;
    }

    /**
     * 执行认证逻辑
     * @author Innocence
     * @date 2020/7/14
     * @param authenticationToken
     * @return org.apache.shiro.authc.AuthenticationInfo
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String adminName = token.getUsername();
        String password =String.valueOf(token.getPassword());
        ArmAdminUser admin = new ArmAdminUser();
        admin.setLoginName(adminName);
        Md5Util md5Util = Md5Util.newInstance();
        admin.setLoginPassword(md5Util.encodePassword(adminName,password));
        LambdaQueryWrapper<ArmAdminUser> lambda = new QueryWrapper<ArmAdminUser>().lambda();
        lambda.eq(ArmAdminUser::getLoginName,adminName).eq(ArmAdminUser::getLoginPassword,
                md5Util.encodePassword(adminName,password));
        ArmAdminUser one = adminService.getOne(lambda);
        if (null == one){
            //没有返回登录用户名对应的SimpleAuthenticationInfo对象时,就会在LoginController中抛出UnknownAccountException异常
            return null;
        }else if (one.getLoginStatus().equals(DISABLE.name())){
            throw new LockedAccountException();
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                //这里根据实际需要，可以放对象，也可以放用户名
                one,
                one.getLoginPassword(),
                ByteSource.Util.bytes(one.getLoginName() + "salt"),
                getName()
        );
        return authenticationInfo;
    }
}
