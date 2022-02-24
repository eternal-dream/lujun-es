package com.cqvip.innocence.project.controller.front;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.annotation.NoLogin;
import com.cqvip.innocence.common.annotation.SensitiveTag;
import com.cqvip.innocence.common.sessions.SessionKeys;
import com.cqvip.innocence.common.util.encryption.SM2Util;
import com.cqvip.innocence.common.util.enums.EnumUtil;
import com.cqvip.innocence.common.util.html.IpUtils;
import com.cqvip.innocence.common.util.iverifycode.IverifyCodeGenUtil;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.UserRegisDto;
import com.cqvip.innocence.project.model.entity.ArmLoginLog;
import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import com.cqvip.innocence.project.model.entity.ArmWebsiteConfig;
import com.cqvip.innocence.project.model.enums.SearchType;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmLoginLogService;
import com.cqvip.innocence.project.service.ArmUserInfoService;
import com.cqvip.innocence.project.service.ArmWebsiteConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserInfoController
 * @Description 前台用户API
 * @Author Innocence
 * @Date 2021/9/13 19:48
 * @Version 1.0
 */
@RequestMapping("/${base-url.front}/userInfo/")
@RestController
@Api(tags = "前台用户的API" )
public class UserInfoController extends AbstractController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ArmUserInfoService userService;

    @Autowired
    private ArmLoginLogService loginLogService;

    @Autowired
    private ArmWebsiteConfigService websiteConfigService;

    @PostMapping("login")
    @NoLogin
    @ApiOperation("前台用户登录接口，支持一卡通、手机、账户登录(密码通过sm2传输和加密保存)")
    public JsonResult userLogin(ArmUserInfo userInfo,String code,HttpServletRequest request){
//        IverifyCodeGenUtil codeGenUtil = IverifyCodeGenUtil.newInstance();
//        String codeErrorMsg = codeGenUtil.checkCode(request.getSession(),code);
//        if (StringUtils.isNoneBlank(codeErrorMsg)){
//            return JsonResult.Get(false,codeErrorMsg);
//        }
        LambdaQueryWrapper<ArmUserInfo> eqLoginName = new QueryWrapper<ArmUserInfo>().lambda()
                .eq(ArmUserInfo::getLoginName,userInfo.getLoginName());
        LambdaQueryWrapper<ArmUserInfo> eqCardId = new QueryWrapper<ArmUserInfo>().lambda()
                .eq(ArmUserInfo::getCardId,userInfo.getLoginName());
        LambdaQueryWrapper<ArmUserInfo> eqPhoneNumber = new QueryWrapper<ArmUserInfo>().lambda()
                .eq(ArmUserInfo::getPhoneNumber,userInfo.getLoginName());
        ArmUserInfo one = userService.getOne(eqLoginName);
        ArmUserInfo two = userService.getOne(eqCardId);
        ArmUserInfo three = userService.getOne(eqPhoneNumber);
        ArmUserInfo user;
        if (null != one){
            user = one;
        }else if (null != two){
            user = two;
        }else if (null != three){
            user = three;
        } else {
            return JsonResult.Get(false,"账户不存在！");
        }
        ArmLoginLog armLoginLog = new ArmLoginLog();
        armLoginLog.setIpAddress(IpUtils.getIpAddr(request));
        armLoginLog.setUserId(user.getId());
        SM2Util sm2Util = new SM2Util();
        String decrypt = sm2Util.decrypt(userInfo.getLoginPassword(), SM2Util.PRIAVTEKEY);
        String savedPass = sm2Util.decrypt(user.getLoginPassword(), SM2Util.PRIAVTEKEY);
        if (!decrypt.equals(savedPass)){
            armLoginLog.setStatus(false);
            armLoginLog.insert();
            return JsonResult.Get(false,"用户名或密码错误！");
        }
        List<ArmWebsiteConfig> list = websiteConfigService.list();
        if (!list.isEmpty()){
            Boolean checkUser = list.get(0).getCheckUser();
            if (checkUser){
                SearchType.ReviewStatus reviewStatus = user.getReviewStatus();
                if (!reviewStatus.equals(SearchType.ReviewStatus.Review)){
                    armLoginLog.setStatus(false);
                    armLoginLog.insert();
                    return JsonResult.Get(false,"您的账号处于："+ EnumUtil.getAlias(reviewStatus)+"状态，请联系管理员！");
                }
            }

        }
        armLoginLog.setStatus(true);
        LambdaUpdateWrapper<ArmUserInfo> updateWrapper = new UpdateWrapper<ArmUserInfo>()
                .lambda().eq(ArmUserInfo::getId, user.getId())
                .set(ArmUserInfo::getRecentTime, new Date());
        userService.update(updateWrapper);
        armLoginLog.insert();
        user.setLoginPassword(null);
        redisUtil.set(request.getSession().getId(),user,60*60*24L);
        return JsonResult.Get("user",user);
    }

    @PostMapping("regis")
    @ApiOperation("前台用户注册接口")
    @NoLogin
    public JsonResult regis(UserRegisDto user, HttpServletRequest request) {
        IverifyCodeGenUtil codeGenUtil = IverifyCodeGenUtil.newInstance();
        String codeErrorMsg = codeGenUtil.checkCode(request.getSession(),user.getCode());
        if (StringUtils.isNoneBlank(codeErrorMsg)){
            return JsonResult.Get(false,codeErrorMsg);
        }
        if (StrUtil.isBlank(user.getLoginName()) || StrUtil.isBlank(user.getLoginName())){
            return JsonResult.Get(false,"用户名或密码不能为空");
        }
        LambdaQueryWrapper<ArmUserInfo> eq = new QueryWrapper<ArmUserInfo>().lambda()
                .eq(ArmUserInfo::getLoginName, user.getLoginName());
        ArmUserInfo one = userService.getOne(eq);
        if (null != one){
            return JsonResult.Get(false,"用户名已存在！");
        }else {
            one = new ArmUserInfo();
        }
        BeanUtils.copyProperties(user,one);
        List<ArmWebsiteConfig> list = websiteConfigService.list();
        Boolean reviewFlag = false;
        if (!list.isEmpty()){
            Boolean checkUser = list.get(0).getCheckUser();
            if (checkUser){
                one.setActiveStatus(SearchType.ActiveStatus.Inactivated);
                one.setReviewStatus(SearchType.ReviewStatus.Wait);
                reviewFlag = true;
            }

        }else {
            one.setActiveStatus(SearchType.ActiveStatus.Activation);
            one.setReviewStatus(SearchType.ReviewStatus.Review);
        }
        SM2Util sm2Util = new SM2Util();
        String encrypt = sm2Util.encrypt(sm2Util.decrypt(user.getLoginPassword(),SM2Util.PRIAVTEKEY), SM2Util.PUBLICKEY);
        user.setLoginPassword(encrypt);
        {//设置user非空字段
            one.setActiveStatus(SearchType.ActiveStatus.Inactivated);
            one.setReaderSrc(SearchType.ReaderSrc.OffCampusUser);
            one.setReviewStatus(SearchType.ReviewStatus.Wait);
            one.setShowWriter(VipEnums.WriterStatus.UNAUTHORIZED);
        }
        boolean b = userService.saveOrUpdate(one);
        if (b && reviewFlag){
            return JsonResult.Get(true,"注册信息已提交，请等待管理员审核",SearchType.ReviewStatus.Wait.name());
        }else if (b && !reviewFlag){
            one.setLoginPassword(null);
            redisUtil.set(request.getSession().getId(),user,60*60*24L);
            return JsonResult.Get(true,"注册成功！").putRes(one);
        }
        return JsonResult.Get(b);
    }

    @PostMapping("forgetPassword")
    @ApiOperation("忘记密码后通过密保问题找回密码")
    @NoLogin
    public JsonResult forgetPassword(@NotNull String loginName, @NotNull String password, @NotNull String ask,
                                     @NotNull String answer, @NotNull String code, HttpServletRequest request){
        IverifyCodeGenUtil codeGenUtil = IverifyCodeGenUtil.newInstance();
        String codeErrorMsg = codeGenUtil.checkCode(request.getSession(),code);
        if (StringUtils.isNoneBlank(codeErrorMsg)){
            return JsonResult.Get(false,codeErrorMsg);
        }
        LambdaQueryWrapper<ArmUserInfo> eq = new QueryWrapper<ArmUserInfo>().lambda().eq(ArmUserInfo::getLoginName, loginName);
        ArmUserInfo one = userService.getOne(eq);
        if (null == one){
            return JsonResult.Get(false,"账号不存在！");
        }
        if (StrUtil.isBlank(one.getAsk()) || StrUtil.isBlank(one.getAnswer())){
            return JsonResult.Get(false,"当前账号未设置密保问题或答案，请联系管理员！");
        }
        if (!ask.equals(one.getAsk()) || !answer.equals(one.getAnswer())){
            return JsonResult.Get(false,"密保问题（或答案）不一致，请联系管理员！");
        }
        SM2Util sm2Util = new SM2Util();
        String encrypt = sm2Util.encrypt(password, SM2Util.PUBLICKEY);
        LambdaUpdateWrapper<ArmUserInfo> set = new UpdateWrapper<ArmUserInfo>().lambda()
                .eq(ArmUserInfo::getId, one.getId()).set(ArmUserInfo::getLoginPassword, encrypt);
        boolean update = userService.update(set);
        return JsonResult.Get(update);
    }

    @GetMapping("getLoginUser")
    @ApiOperation("获取当前登录的读者（用户）信息")
    public JsonResult getLoginUser(HttpServletRequest request){
        String id = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo)redisUtil.get(id);
        if (null != user){
            ArmUserInfo byId = userService.getById(user.getId());
            redisUtil.set(id,byId,60*60*24L);
            return JsonResult.Get().put("loginUser",byId);
        }else {
            return JsonResult.Get(false);
        }
    }

    @PostMapping("logOut")
    @ApiOperation("前台用户退出")
    @NoLogin
    public JsonResult logOut(HttpServletRequest request){
        String id = request.getSession().getId();
        redisUtil.del(id);
        return JsonResult.Get();
    }

    @SensitiveTag
    @PostMapping("editUser")
    @ApiOperation("修改自己的信息")
    @Log(title = "修改个人信息",operateType = VipEnums.OperateType.EDITE)
    public JsonResult editUser(ArmUserInfo user, HttpServletRequest req){
        String sessionId = req.getSession().getId();
        ArmUserInfo info = (ArmUserInfo) redisUtil.get(sessionId);
        if (!info.getId().equals(user.getId())){
            return JsonResult.Get(false,"当前用户和登录用户不一致！");
        }
        ArmUserInfo userById = userService.getById(user.getId());
        String userPassword = user.getLoginPassword();
        if (StrUtil.isNotBlank(userPassword)){
            SM2Util sm2Util = new SM2Util();
            String decrypt = sm2Util.decrypt(userPassword, SM2Util.PRIAVTEKEY);
            String encrypt = sm2Util.encrypt(decrypt, SM2Util.PUBLICKEY);
            user.setLoginPassword(encrypt);
        }else {
            user.setLoginPassword(userById.getLoginPassword());
        }
        userService.saveOrUpdate(user);
        ArmUserInfo byId = userService.getById(user.getId());
        byId.setLoginPassword(null);
        redisUtil.set(req.getSession().getId(),byId);
        return JsonResult.Get();
    }

    @PostMapping("resetPassword")
    @NoLogin
    public JsonResult resetPassword(ArmUserInfo userInfo,HttpServletRequest request,String verifyCode){
        try {
            //验证码
            IverifyCodeGenUtil codeGenUtil = IverifyCodeGenUtil.newInstance();
            String codeErrorMsg = codeGenUtil.checkCode(request.getSession(), verifyCode);
            if (StringUtils.isNoneBlank(codeErrorMsg)) {
                return JsonResult.Get(false, codeErrorMsg);
            }
            String sessionId = request.getSession().getId();
            LambdaQueryWrapper<ArmUserInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ArmUserInfo::getLoginName,userInfo.getLoginName())
             .eq(ArmUserInfo::getAsk,userInfo.getAsk())
             .eq(ArmUserInfo::getAnswer,userInfo.getAnswer());
            List<ArmUserInfo> list = userService.list(wrapper);
            if(list.size()==0){
                return JsonResult.Get(false,"账号/密保问题或答案有误，请确认信息！");
            }
            ArmUserInfo user = list.get(0);
            SM2Util sm2Util = new SM2Util();
            String decryptPassword = sm2Util.decrypt(userInfo.getLoginPassword(), SM2Util.PRIAVTEKEY);
            user.setLoginPassword(sm2Util.encrypt(decryptPassword,SM2Util.PUBLICKEY));
            user.updateById();
            return JsonResult.Get();
        }finally {
            request.getSession().removeAttribute(SessionKeys.VcodeKeys.CODE);
        }
    }

    @GetMapping("getLoginTimes")
    public JsonResult getLoginTimes(HttpServletRequest request){
        String sessionId = request.getSession().getId();
        ArmUserInfo info = (ArmUserInfo) redisUtil.get(sessionId);
        LambdaQueryWrapper<ArmLoginLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArmLoginLog::getUserId,info.getId());
        int count = loginLogService.count(wrapper);
        return JsonResult.Get("loginTimes",count);
    }

    @PostMapping("editPassword")
    public JsonResult editPassword(String oldPassword,String newPassword,HttpServletRequest request){
        String sessionId = request.getSession().getId();
        ArmUserInfo info = (ArmUserInfo) redisUtil.get(sessionId);
        ArmUserInfo userInfo = userService.getById(info.getId());
        SM2Util sm2Util = new SM2Util();
        String decryptOldPassword = sm2Util.decrypt(oldPassword, SM2Util.PRIAVTEKEY);
        String decryptPassword = sm2Util.decrypt(userInfo.getLoginPassword(), SM2Util.PRIAVTEKEY);
        String decryptNewPassword = sm2Util.decrypt(newPassword, SM2Util.PRIAVTEKEY);
        if(!decryptOldPassword.equals(decryptPassword)){
            return JsonResult.Get(false,"原密码错误!");
        }
        userInfo.setLoginPassword(sm2Util.encrypt(decryptNewPassword,SM2Util.PUBLICKEY));
        userInfo.updateById();
        return JsonResult.Get();
    }

    @PostMapping("modifyPasswordIssue")
    @Log(title = "修改密保问题",operateType = VipEnums.OperateType.EDITE)
    public JsonResult modifyPasswordIssue(String oldAnswer,String newAsk,String newAnswer,HttpServletRequest request){
        if(StringUtils.isAnyBlank(oldAnswer,newAnswer,newAsk)){
            return JsonResult.Get(false,"参数错误");
        }
        String sessionId = request.getSession().getId();
        ArmUserInfo info = (ArmUserInfo) redisUtil.get(sessionId);
        if(!oldAnswer.equals(info.getAnswer())){
            return JsonResult.Get(false,"原密保答案不正确");
        }
        ArmUserInfo userinfo = userService.getById(info.getId());
        userinfo.setAsk(newAsk);
        userinfo.setAnswer(newAnswer);
        boolean b = userinfo.updateById();
        return JsonResult.Get();
    }
}
