package com.cqvip.innocence.project.controller.front;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.annotation.NoLogin;
import com.cqvip.innocence.common.annotation.XssExclusion;
import com.cqvip.innocence.common.util.file.FileUtil;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.*;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.*;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * @Author eternal
 * @Date 2021/9/29
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/netdisk")
public class NetdiskController extends AbstractController {

    @Autowired
    private ArmNetdiskService netdiskService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArmAnnexService annexService;

    @Autowired
    private ArmUserInfoService userInfoService;

    @Autowired
    private ArmNetdiskClassService classService;

    @Autowired
    private ArmNetdiskThemeService themeService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    FileUtil fileUtil;

    @GetMapping("getNetdiskPage")
    @ApiOperation("根据用户，资源分类（云书包，个人网盘），自定义分类获取资源分页")
    public JsonResult getNetdiskPage(Long classId, String title, HttpServletRequest request, VipEnums.DiskType diskType) {
        String sessionId = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo) redisUtil.get(sessionId);
        LambdaQueryWrapper<ArmNetdisk> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(classId != null, ArmNetdisk::getClassId, classId)
                .eq(ArmNetdisk::getDiskType, diskType)
                .eq(StringUtils.isNotBlank(title), ArmNetdisk::getTitle, title)
                .eq(ArmNetdisk::getCreatUserId, user.getId());
        Page page = netdiskService.page(getPageParams(), wrapper);
        return JsonResult.Get().putPage(page);
    }

    @GetMapping("getSharedResources")
    @ApiOperation("根据用户获取分享的所有资源分页")
    @Log(title = "获取自己共享的资源")
    public JsonResult getSharedResources(Long classId, String title, HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo) redisTemplate.opsForValue().get(sessionId);
        LambdaQueryWrapper<ArmNetdisk> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(classId != null, ArmNetdisk::getClassId, classId)
                .eq(StringUtils.isNotBlank(title), ArmNetdisk::getTitle, title)
                .eq(ArmNetdisk::getCreatUserId, user.getId());
        Page page = netdiskService.page(getPageParams(), wrapper);
        return JsonResult.Get("pageData", page);
    }

    @PostMapping("addOrModify")
    @XssExclusion
    @Log(title = "添加/修改共享资源",operateType = VipEnums.OperateType.SAVE_OR_UPDATE)
    public JsonResult addOrModify(ArmNetdisk resource,
                                  String fileIds,
                                  MultipartFile[] files,
                                  VipEnums.AnnexSrc annexSrc,
                                  HttpServletRequest request) throws Exception {
        String sessionId = request.getSession().getId();
        ArmUserInfo user = (ArmUserInfo) redisTemplate.opsForValue().get(sessionId);
        resource.setCreatUserId(user.getId());
        resource.insertOrUpdate();
        //删除不在ids里的附件
        LambdaQueryWrapper<ArmAnnex> annexWrapper = new LambdaQueryWrapper<>();
        annexWrapper.eq(ArmAnnex::getRealId, resource.getId());
        if (StringUtils.isNotBlank(fileIds)) {
            annexWrapper.notIn(BaseModel::getId, Arrays.asList(fileIds.split(";")));
        }
        boolean remove = annexService.remove(annexWrapper);
        //获取新上传的附件
        if (files != null) {
            for (MultipartFile file : files) {
                Long annexId = fileUtil.saveFile(file, annexSrc, resource.getId());
            }
        }
        return JsonResult.Get();
    }

    @PostMapping("deleteByIds")
    @Log(title = "删除共享资源",operateType = VipEnums.OperateType.DELETE)
    public JsonResult deleteByIds(@RequestBody Long[] ids) {
        return JsonResult.Get(netdiskService.deleteByIdsTransactional(Arrays.asList(ids)));
    }

    @GetMapping("getAllResourcesByCondition")
    @ApiOperation("资源共享平台获取资源分页")
    @Log(title = "获取共享资源")
    public JsonResult getAllResourcesByCondition(String title, Long classId, Long themeId, String order) {
        Page params = getPageParams();
        QueryWrapper<ArmNetdisk> wrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(order)) {
            wrapper.orderByDesc(order);
        }
        LambdaQueryWrapper<ArmNetdisk> lambda = wrapper.lambda();
        lambda.like(StrUtil.isNotBlank(title), ArmNetdisk::getTitle, title)
                .eq(ArmNetdisk::getDiskType, VipEnums.DiskType.SHAREARTICLE)
                .eq(classId != null, ArmNetdisk::getClassId, classId)
                .eq(themeId != null, ArmNetdisk::getThemeId, themeId);
        Page<ArmNetdisk> page = netdiskService.page(params, lambda);
        List<ArmNetdisk> records = page.getRecords();
        records.forEach(item -> {
            ArmUserInfo user = userInfoService.getById(item.getCreatUserId());
            item.setCreatUserName(user.getReaderName());
            if (item.getClassId() != null) {
                ArmNetdiskClass clazz = classService.getById(item.getClassId());
                item.setClassName(clazz.getTitle());
            }
            if (item.getThemeId() != null) {
                ArmNetdiskTheme theme = themeService.getById(item.getThemeId());
                item.setThemeName(theme.getTitle());
            }
        });
        return JsonResult.Get().putPage(page);
    }

    @NoLogin
    @GetMapping("getRecomUserAndResources")
    @ApiOperation("获取优质作者推荐的作者信息和其分享的资源")
    //SELECT COUNT(*),CREAT_USER_ID FROM ARMYINFANTRY.ARM_NETDISK GROUP BY CREAT_USER_ID ORDER BY COUNT(*) DESC LIMIT 2;
    public JsonResult getRecomUserAndResources() {
        QueryWrapper<ArmNetdisk> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("DISK_TYPE",VipEnums.DiskType.SHAREARTICLE)
                .select("COUNT(CREAT_USER_ID)", "CREAT_USER_ID")
                .orderByDesc("COUNT(CREAT_USER_ID)")
                .groupBy("CREAT_USER_ID")
                .last("limit 2");
        List<Map<String, Object>> maps = netdiskService.listMaps(queryWrapper);
        List<Map<String, Object>> userList = new ArrayList<>(maps.size());
        List<Long> userIdList = new ArrayList<>(maps.size());
        maps.forEach(item ->{
            QueryWrapper<ArmNetdisk> query = new QueryWrapper<>();
            Long count = (Long)item.get("COUNT(CREAT_USER_ID)");
            Long userId = (Long)item.get("CREAT_USER_ID");
            userIdList.add(userId);
            ArmUserInfo user = userInfoService.getById(userId);
            query.select("SUM(DOWN_TOTAL)").eq("CREAT_USER_ID",userId).eq("DISK_TYPE",VipEnums.DiskType.SHAREARTICLE);
            Map<String, Object> serviceMap = netdiskService.getMap(query);
            Map<String, Object> map = new HashMap<>();
            map.put("userInfo",user);
            map.put("count",count);
            map.put("downTotal",serviceMap.get("SUM(DOWN_TOTAL)"));
            userList.add(map);
        });
        LambdaQueryWrapper<ArmNetdisk> in = new LambdaQueryWrapper<ArmNetdisk>()
                .in(ArmNetdisk::getCreatUserId, userIdList)
                .eq(ArmNetdisk::getDiskType,VipEnums.DiskType.SHAREARTICLE)
                .last("limit 7");
        List<ArmNetdisk> list = netdiskService.list(in);
        return JsonResult.Get().putList(list).put("userList",userList);
    }

    @GetMapping("getInfoById")
    @ApiOperation("根据id获取资源详情")
    public JsonResult getInfoById(Long id){
        ArmNetdisk byId = netdiskService.getById(id);

        if (byId != null){
            byId.setReadTotal(byId.getReadTotal()+1);
            netdiskService.saveOrUpdate(byId);
            if (byId.getClassId() != null) {
                ArmNetdiskClass clazz = classService.getById(byId.getClassId());
                byId.setClassName(clazz.getTitle());
            }
            if (byId.getThemeId() != null) {
                ArmNetdiskTheme theme = themeService.getById(byId.getThemeId());
                byId.setThemeName(theme.getTitle());
            }
        }
        return JsonResult.Get().putRes(byId);
    }

    @PostMapping("downloadById")
    @ApiOperation("根据id和路径下载附件")
    @Log(title = "下载共享资源附件")
    public void downloadById(String id, HttpServletRequest request, HttpServletResponse response){
        ArmNetdisk byId = netdiskService.getById(id);
        if (byId != null){
            byId.setDownTotal(byId.getDownTotal()+1);
            netdiskService.saveOrUpdate(byId);
        }
        LambdaQueryWrapper<ArmAnnex> eq = new LambdaQueryWrapper<ArmAnnex>().eq(ArmAnnex::getRealId, id);
        ArmAnnex one = annexService.getOne(eq);
        String path = one.getFilePath();
        String name = path.substring(path.lastIndexOf(File.separator)+1,path.lastIndexOf("."));
        path = fileUtil.getPath("") + File.separator+path;
        try {
            fileUtil.download(request,response,path,name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("getRecomByUserId")
    @ApiOperation("根据创建者获取其推荐资源")
    public JsonResult getRecomByUserId(Long id){
        LambdaQueryWrapper<ArmNetdisk> last = new LambdaQueryWrapper<ArmNetdisk>()
                .eq(ArmNetdisk::getDiskType, VipEnums.DiskType.SHAREARTICLE)
                .eq(ArmNetdisk::getCreatUserId, id)
                .orderByDesc(ArmNetdisk::getReadTotal, ArmNetdisk::getDownTotal)
                .last("limit 6 ");
        return JsonResult.Get().putList(netdiskService.list(last));
    }
}
