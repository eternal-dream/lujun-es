package com.cqvip.innocence.project.controller.admin;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.sessions.SessionKeys;
import com.cqvip.innocence.common.util.encryption.Md5Util;
import com.cqvip.innocence.common.util.encryption.SM2Util;
import com.cqvip.innocence.common.util.file.FileUtil;
import com.cqvip.innocence.common.util.html.IpUtils;
import com.cqvip.innocence.common.util.iverifycode.IverifyCodeGenUtil;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.*;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.*;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 管理用户表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
@RestController
@RequestMapping("/${base-url.manager}/armAdminUser")
@Api(tags = "后端管理员接口")
public class ArmAdminUserController extends AbstractController {

    @Autowired
    private ArmAdminUserService adminUserService;

    @Autowired
    private ArmRoleService roleService;

    @Autowired
    private ArmResourceService resourceService;

    @Autowired
    private ArmAdminUserLogService adminUserLogService;

    @Autowired
    private ArmAdminUserRoleService adminUserRoleService;

    @PostMapping("login")
    @ApiOperation("管理员登录")
    @Log(title = "管理员登录系统", operateType = VipEnums.OperateType.SEARCH)
    public JsonResult adminLogin(String name, String passWord, String code,
                                 HttpServletRequest request) {
        //验证码
        IverifyCodeGenUtil codeGenUtil = IverifyCodeGenUtil.newInstance();
        String codeErrorMsg = codeGenUtil.checkCode(request.getSession(), code);
        if (StringUtils.isNoneBlank(codeErrorMsg)) {
            return JsonResult.Get(false, codeErrorMsg);
        }
        if (StringUtils.isBlank(name) || StringUtils.isBlank(passWord)) {
            return JsonResult.Get(false, "用户名或密码不能为空");
        }
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return JsonResult.Get(false, "已登录，请先退出登录！");
        }
        SM2Util sm2Util = new SM2Util();
        String decrypt = sm2Util.decrypt(passWord, SM2Util.PRIAVTEKEY);
        UsernamePasswordToken passwordToken = new UsernamePasswordToken(name, decrypt);
        JsonResult res = JsonResult.Get();
        try {
            subject.login(passwordToken);
            //从AdminRealm里返回的SimpleAuthenticationInfo获取到认证成功的用户名，
            //subject.getPrincipal()获取的是SimpleAuthenticationInfo设置的第一个参数
            ArmAdminUser loginAdmin = (ArmAdminUser) subject.getPrincipal();
            Session session = subject.getSession();
            loginAdmin.setLoginPassword("");
            session.setAttribute(SessionKeys.LOGIN_ADMIN_KEY, loginAdmin);
            //登陆成功后把当前session传递至前端，用作token。同时获取当前用户的权限和角色列表返回

            res.put("token", session.getId());
            res.put("admin", loginAdmin);
        } catch (LockedAccountException e) {
            res.set(false, "账户被冻结！");
        } catch (UnknownAccountException e) {
            res.set(false, "用户名或密码错误！");
        }
        return res;
    }

    @GetMapping("getAdminInfo")
    @ApiOperation("获取当前登录管理员的信息（账户，角色，权限）")
    public JsonResult getAdminInfo(){
        Subject subject = SecurityUtils.getSubject();
        ArmAdminUser principal =(ArmAdminUser) subject.getPrincipal();
        //重新从数据库获取，保证数据的实时性
        ArmAdminUser byId = adminUserService.getById(principal.getId());
        byId.setLoginPassword(null);
        //获取当前用户的角色和所拥有的的权限返回
        List<ArmRole> rolesByAdmin = roleService.getRolesByAdmin(byId);
        HashSet<String> roleSet = new HashSet<>();
        HashSet<String> permissionSet = new HashSet<>();
        rolesByAdmin.forEach(item -> {
            roleSet.add(item.getName());
            List<ArmResource> list = resourceService.getRoleResources(item);
            list.forEach(reduce -> permissionSet.add(reduce.getPermission()));
        });
        JsonResult res = JsonResult.Get();
        res.put("admin",byId);
        res.put("roles",roleSet);
        res.put("per",permissionSet);
        return res;
    }

    @PostMapping("logout")
    @ApiOperation("管理员退出登录")
    public JsonResult adminLogout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        subject.getSession().removeAttribute(SessionKeys.LOGIN_ADMIN_KEY);
        return JsonResult.Get();
    }

    @GetMapping("getAdminUserList")
    @ApiOperation("获取管理员分页列表")
    public JsonResult getAdminUserList(ArmAdminUser adminUser,String test) {
        QueryWrapper<ArmAdminUser> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(adminUser.getPhoneNumber()), "PHONE_NUMBER", adminUser.getPhoneNumber());
        wrapper.eq(StringUtils.isNotBlank(adminUser.getLoginName()), "LOGIN_NAME", adminUser.getLoginName());
        wrapper.eq((StringUtils.isNotBlank(adminUser.getRealName())), "REAL_NAME", adminUser.getRealName());
        wrapper.orderByAsc("SORT");
        Page<ArmAdminUser> userPage = adminUserService.page(getPageParams(), wrapper);
//        List<ArmAdminUser> userList = adminUserService.getAdminUserList(adminUser,getPageParams());
//        Integer count = adminUserService.count(wrapper);
        userPage.getRecords().forEach(user -> user.setLoginPassword(null));
        return JsonResult.Get("userPage", userPage);
    }

    @PostMapping("disableAdminUser")
    @ApiOperation("根据id禁用管理员账号，支持批量操作")
    public JsonResult disableAdminUser(@RequestBody Long[] ids,HttpServletRequest request) {
        if (ids.length == 0) {
            return JsonResult.Get(false, "参数错误!");
        }
        //添加日志
        for (Long id : ids) {
            addLog(request,"账号禁用",id, VipEnums.AdminUserLogType.DELETE);
        }
        UpdateWrapper<ArmAdminUser> wrapper = new UpdateWrapper<>();
        wrapper.in("ID", ids);
        wrapper.set("LOGIN_STATUS", VipEnums.LoginStatus.DISABLE);
        adminUserService.update(wrapper);
        return JsonResult.Get();
    }

    @PostMapping("enableAdminUser")
    @ApiOperation("根据id启用管理员账号，支持批量操作")
    public JsonResult enableAdminUser(@RequestBody Long[] ids,HttpServletRequest request) {
        if (ids.length == 0) {
            return JsonResult.Get(false, "参数错误!");
        }
        //添加日志
        for (Long id : ids) {
            addLog(request,"启用账号",id, VipEnums.AdminUserLogType.RESTORE);
        }
        UpdateWrapper<ArmAdminUser> wrapper = new UpdateWrapper<>();
        wrapper.in("ID", ids);
        wrapper.set("LOGIN_STATUS", VipEnums.LoginStatus.ENABLE);
        adminUserService.update(wrapper);
        return JsonResult.Get();
    }

    @PostMapping("addOrModifyAdminUser")
    @ApiOperation("新增编辑管理员账户")
    @Transactional
    public JsonResult addOrModifyAdminUser(ArmAdminUser adminUser,HttpServletRequest request) {
        boolean isAdd = adminUser.getId() != null;
        if(StrUtil.isNotBlank(adminUser.getLoginPassword())){
            SM2Util sm2Util = new SM2Util();
            String decryptPassword = sm2Util.decrypt(adminUser.getLoginPassword(), SM2Util.PRIAVTEKEY);
            Md5Util md5Util = Md5Util.newInstance();
            adminUser.setLoginPassword(md5Util.encodePassword(adminUser.getLoginName(), decryptPassword));
        }
        adminUserService.addOrModifyAdminUser(adminUser);
        if(isAdd){
            addLog(request,"修改账号信息",adminUser.getId(), VipEnums.AdminUserLogType.EDITE);
        }else{
            addLog(request,"新增管理员账号",adminUser.getId(), VipEnums.AdminUserLogType.ADD);
        }
        return JsonResult.Get();
    }

    @PostMapping("deleteAdminUser")
    @ApiOperation("根据id删除管理员账户")
    public JsonResult deleteAdminUser(@RequestBody Long[] ids,HttpServletRequest request) {
        for (Long id : ids) {
            addLog(request,"删除管理员账号",id, VipEnums.AdminUserLogType.DELETE);
        }
        LambdaQueryWrapper<ArmAdminUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ArmAdminUserRole::getAdminUserId,ids);
        adminUserRoleService.remove(wrapper);
        List<Long> idList = Arrays.asList(ids);
        adminUserService.removeByIds(idList);
        return JsonResult.Get();
    }

    private void addLog(HttpServletRequest request,String title,Long objId,VipEnums.AdminUserLogType operateType){
        ArmAdminUserLog log = new ArmAdminUserLog();
        log.setTitle(title);
        log.setIpAddress(IpUtils.getIpAddr(request));
        log.setObjId(objId);
        ArmAdminUser loginAdmin = (ArmAdminUser) SecurityUtils.getSubject().getSession().getAttribute(SessionKeys.LOGIN_ADMIN_KEY);
        log.setOperatAt(loginAdmin.getId());
        log.setOperateType(operateType);
        log.setOperatName(loginAdmin.getLoginName());
        log.insert();
    }

    @PostMapping("updateMyselfPassword")
    @ApiOperation("管理员修改自己的密码")
    public JsonResult updateMyselfPassword(String oldPassword, String newPassword,HttpServletRequest request){
        ArmAdminUser loginAdmin = (ArmAdminUser) SecurityUtils.getSubject().getPrincipal();
        ArmAdminUser admin = adminUserService.getById(loginAdmin.getId());
        Md5Util md5Util = Md5Util.newInstance();
        String password = md5Util.encodePassword(admin.getLoginName(), oldPassword);
        if (!password.equals(admin.getLoginPassword())){
            return JsonResult.Get(false,"原密码错误，校验失败！");
        }
        String newPass = md5Util.encodePassword(admin.getLoginName(), newPassword);
        LambdaUpdateWrapper<ArmAdminUser> set = new UpdateWrapper<ArmAdminUser>()
                .lambda()
                .eq(ArmAdminUser::getId, admin.getId())
                .set(ArmAdminUser::getLoginPassword, newPass);
        boolean update = adminUserService.update(set);
        addLog(request,"管理员修改自己的密码",admin.getId(), VipEnums.AdminUserLogType.EDITE);
        return JsonResult.Get(update);
    }

    @PostMapping("updateInfoWithOutOther")
    @ApiOperation("更新自身基础信息，不处理密码和权限关系")
    public JsonResult updateInfoWithOutRole(ArmAdminUser user){
        ArmAdminUser admin = adminUserService.getById(user.getId());
        user.setLoginPassword(admin.getLoginPassword());
        boolean update = adminUserService.saveOrUpdate(user);
        return JsonResult.Get(update);
    }

}

