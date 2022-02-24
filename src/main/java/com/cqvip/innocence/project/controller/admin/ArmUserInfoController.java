package com.cqvip.innocence.project.controller.admin;


import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.annotation.XssExclusion;
import com.cqvip.innocence.common.sessions.SessionKeys;
import com.cqvip.innocence.common.util.file.FileUtil;
import com.cqvip.innocence.common.util.html.IpUtils;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.*;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmAdminUserRoleService;
import com.cqvip.innocence.project.service.ArmAdminUserService;
import com.cqvip.innocence.project.service.ArmUserInfoLogService;
import com.cqvip.innocence.project.service.ArmUserInfoService;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器(用户管理/读者列表)
 * </p>
 *
 * @author eternal
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/${base-url.manager}/armUserInfo")
public class ArmUserInfoController extends AbstractController {

    @Autowired
    private ArmUserInfoService armUserInfoService;

    @Autowired
    private ArmAdminUserService adminUserService;

    @Autowired
    private ArmAdminUserRoleService adminUserRoleService;

    @Autowired
    private ArmUserInfoLogService userInfoLogService;

    @Autowired
    FileUtil fileUtil;

    /**
     * 查询用户表数据
     *
     * @param userInfo
     * @return
     */
    @GetMapping("getUserInfoPage")
    @Log(title = "查询用户列表", operateType = VipEnums.OperateType.SEARCH)
    public JsonResult getUserInfoPage(ArmUserInfo userInfo) {
        QueryWrapper<ArmUserInfo> wrapper = defineQueryWrapper(userInfo);
        Page<ArmUserInfo> pageData = armUserInfoService.page(getPageParams(), wrapper);
        pageData.getRecords().forEach(item -> item.setLoginPassword(null));
        return JsonResult.Get("userInfoPage", pageData);
    }

    /**
     * 添加/修改
     *
     * @param user
     * @return
     */
    @PostMapping("addOrModifyUserInfo")
    @Log(title = "添加或修改用户数据", operateType = VipEnums.OperateType.SAVE_OR_UPDATE)
    @XssExclusion
    public JsonResult addUserInfo(ArmUserInfo user,HttpServletRequest request) {
        if (user.getId() == null) {
            user.setShowWriter(VipEnums.WriterStatus.UNAUTHORIZED);
        }else{
            addUserLog(request,user.getId(), VipEnums.OperateType.EDITE);
        }

        armUserInfoService.addOrModifyUserInfo(user);
        return JsonResult.Get("user", user);
    }

    /**
     * 导出读者列表
     *
     * @param userInfo
     * @param response
     */
    @GetMapping("export")
    public void export(ArmUserInfo userInfo, HttpServletResponse response) throws UnsupportedEncodingException {
        QueryWrapper<ArmUserInfo> wrapper = defineQueryWrapper(userInfo);

        List<ArmUserInfo> list = armUserInfoService.list(wrapper);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("readerName", "姓名");
        writer.addHeaderAlias("loginName", "登录号");
        writer.addHeaderAlias("phoneNumber", "手机号");
        writer.addHeaderAlias("readerId", "借书证号");
        writer.addHeaderAlias("readerType", "读者类型");
        writer.addHeaderAlias("readerUnit", "读者单位");
        writer.addHeaderAlias("readerSrc", "读者来源");
        writer.addHeaderAlias("showWriter", "专家认证");
        writer.addHeaderAlias("certificationTime", "发证日期");
        writer.addHeaderAlias("recentTime", "最近登录日期");
        writer.write(list);
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        String fileName = URLEncoder.encode("读者列表.xls", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        ServletOutputStream out = null;

        try {
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.close();
        }
        IoUtil.close(out);

    }

    private QueryWrapper<ArmUserInfo> defineQueryWrapper(ArmUserInfo userInfo) {
        QueryWrapper<ArmUserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(userInfo.getReaderType()), "READER_TYPE", userInfo.getReaderType())
                .eq(userInfo.getCardStatus() != null, "CARD_STATUS", userInfo.getCardStatus())
                .eq(userInfo.getReviewStatus() != null, "REVIEW_STATUS", userInfo.getReviewStatus())
                .eq(StringUtils.isNotBlank(userInfo.getReaderUnit()), "READER_UNIT", userInfo.getReaderUnit())
                .eq(userInfo.getReaderSrc() != null, "READER_SRC", userInfo.getReaderSrc())
                .eq(userInfo.getShowWriter() != null, "SHOW_WRITER", userInfo.getShowWriter())
                .like(StringUtils.isNotBlank(userInfo.getLoginName()), "LOGIN_NAME", userInfo.getLoginName())
                .eq(StringUtils.isNotBlank(userInfo.getReaderId()), "READER_ID", userInfo.getReaderId())
                .like(StringUtils.isNotBlank(userInfo.getReaderName()), "READER_NAME", userInfo.getReaderName())
                .eq(StringUtils.isNotBlank(userInfo.getPhoneNumber()), "PHONE_NUMBER", userInfo.getPhoneNumber())
                .orderByDesc("RECENT_TIME");
        return wrapper;
    }


    /**
     * 获取现存的所有读者单位
     *
     * @return
     */
    @GetMapping("getAllReaderUnit")
    public JsonResult getAllReaderUnit() {

        return JsonResult.Get();
    }

    /**
     * 从学校同步用户数据
     *
     * @return
     */
    @GetMapping("transferUserInfo")
    public JsonResult transferUserInfo() {

        return JsonResult.Get();
    }

    /**
     * 是否开启用户审核
     *
     * @param flag
     * @return
     */
    @GetMapping("switchUserAudit")
    public JsonResult switchUserAudit(Boolean flag) {

        return JsonResult.Get();
    }

    /**
     * 用户授权
     *
     * @param userId
     * @param roleId 授权角色(为空表示取消授权)
     * @return
     */
    @PostMapping("userAuthorize")
    public JsonResult userAuthorize(@NotNull Long userId, Long roleId,HttpServletRequest request) {
        ArmUserInfo userinfo = armUserInfoService.getById(userId);
        if (userinfo == null) {
            return JsonResult.Get(false, "用户不存在!");
        }
        if (roleId == null) {
            addAdminLog(request,"取消用户授权",userinfo.getAdminUserId(), VipEnums.AdminUserLogType.ADD);
            userinfo.setAdminUserId(null);
            userinfo.updateById();
            return JsonResult.Get(true, "取消授权成功!");
        }
        addAdminLog(request,"用户授权",userinfo.getAdminUserId(), VipEnums.AdminUserLogType.ADD);
        //查询是否已存在用户名相同的管理员账户
        QueryWrapper<ArmAdminUser> adminUserQueryWrapper = new QueryWrapper<>();
        adminUserQueryWrapper.eq("LOGIN_NAME", userinfo.getLoginName());
        ArmAdminUser adminUser = adminUserService.getOne(adminUserQueryWrapper);
        if (adminUser == null) {
            adminUser = new ArmAdminUser();
        }
        //设置管理员账户的数据
//        adminUser.setAdminGroupId(groupId);
        adminUser.setLoginPassword(userinfo.getLoginPassword());
        adminUser.setAvatar(userinfo.getAvatar());
        adminUser.setEmail(userinfo.getEmail());
        adminUser.setPhoneNumber(userinfo.getPhoneNumber());
        adminUser.setRealName(userinfo.getReaderName());
        adminUser.setLoginName(userinfo.getLoginName());
        adminUser.setStatus(VipEnums.Status.ENABLE);
        adminUser.insertOrUpdate();
        userinfo.setAdminUserId(adminUser.getId());
        userinfo.updateById();
        //管理员-角色中间表添加数据
        ArmAdminUserRole adminUserRole = new ArmAdminUserRole();
        adminUserRole.setAdminUserId(adminUser.getId());
        adminUserRole.setRoleId(roleId);
        adminUserRoleService.save(adminUserRole);
        return JsonResult.Get(true,"授权成功!");
    }

    @PostMapping("uploadAvatar")
    public JsonResult uploadAvatar(MultipartFile file){
        JsonResult jsonResult = fileUtil.uploadUtil(file, "/user/avatar");


        return JsonResult.Get();
    }

    @PostMapping("deleteUser")
    public JsonResult deleteUser(@RequestBody Long[] ids,HttpServletRequest request){
        List<Long> list = new ArrayList<>();
        for (Long id : ids) {
            list.add(id);
            addUserLog(request,id, VipEnums.OperateType.DELETE);
        }
        armUserInfoService.removeByIds(list);
        return JsonResult.Get();
    }

    private void addAdminLog(HttpServletRequest request, String title, Long objId, VipEnums.AdminUserLogType operateType){
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

    public void addUserLog(HttpServletRequest request, Long userId, VipEnums.OperateType operateType){
        ArmAdminUser loginAdmin = (ArmAdminUser) SecurityUtils.getSubject().getSession().getAttribute(SessionKeys.LOGIN_ADMIN_KEY);
        ArmUserInfoLog userInfoLog = new ArmUserInfoLog();
        userInfoLog.setAdminUserId(loginAdmin.getId());
        userInfoLog.setUserInfoId(userId);
        userInfoLog.setIpAddress(IpUtils.getIpAddr(request));
        userInfoLog.setOperateType(operateType);
        ArmUserInfo userInfo = armUserInfoService.getById(userId);
        userInfoLog.setContent(JSON.toJSONString(userInfo));
        userInfoLog.insert();
    }

}

