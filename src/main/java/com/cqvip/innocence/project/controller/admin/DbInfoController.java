package com.cqvip.innocence.project.controller.admin;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.common.annotation.XssExclusion;
import com.cqvip.innocence.common.util.file.FileUtil;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.DbInfo;
import com.cqvip.innocence.project.model.entity.DbInfoLog;
import com.cqvip.innocence.project.model.entity.DbInfoResource;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.DbInfoLogService;
import com.cqvip.innocence.project.service.DbInfoResourceService;
import com.cqvip.innocence.project.service.DbInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.NativeUint8Array;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据库基础信息表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/${base-url.manager}/db-info")
@Api(tags="管理端数据库管理")
@XssExclusion
public class DbInfoController extends AbstractController {

    @Autowired
    private DbInfoService dbInfoService;

    @Autowired
    private DbInfoResourceService resourceService;

    @Autowired
    private DbInfoLogService logService;

    @Autowired
    FileUtil util;

    @PostMapping("addOrEdit")
    @ApiOperation("新增编辑数据库")
    @XssExclusion
    @Log(title = "新增或编辑数据库",operateType = VipEnums.OperateType.SAVE_OR_UPDATE)
    public JsonResult addOrEdit(DbInfo info){
        if (StrUtil.isBlank(info.getTitle())){
            return JsonResult.Get(false,"数据库名不能为空");
        }
        DbInfoLog log = new DbInfoLog();
        log.setContent(info.getId() != null ? "编辑数据库信息":"新增数据库");
        boolean b = dbInfoService.saveOrUpdate(info);
        String msg = null;
        if (b){
            log.setDbId(info.getId());
            Subject subject = SecurityUtils.getSubject();
            ArmAdminUser principal =(ArmAdminUser) subject.getPrincipal();
            log.setUserId(principal.getId());
            boolean insert = log.insert();
            if (!insert){
                msg="数据库日志更新失败";
            }
        }
        return JsonResult.Get(b,msg);
    }

    @GetMapping("getPageList")
    @ApiOperation("获取数据库分页列表")
    public JsonResult getPageList(DbInfo info){
        List<DbInfoResource> list = resourceService.list();
        Page<DbInfo> pageList = dbInfoService.getPageList(getPageParams(), info);
        pageList.getRecords().forEach(item->{
            for (DbInfoResource resource:list) {
                if (item.getTypeId() != null && item.getTypeId().equals(resource.getId())){
                    item.setTypeTitle(resource.getTitle());
                }
                if (item.getContentId() != null && item.getContentId().equals(resource.getId())){
                    item.setContentTitle(resource.getTitle());
                }
                if (item.getSystemId() != null && item.getSystemId().equals(resource.getId())){
                    item.setSystemTitle(resource.getTitle());
                }
            }
        });
        return JsonResult.Get().putPage(pageList);
    }

    @GetMapping("getList")
    @ApiOperation("获取数据库列表，不分页")
    public JsonResult getList(){
        LambdaQueryWrapper<DbInfo> wrapper = new QueryWrapper<DbInfo>().lambda().orderByDesc(DbInfo::getSortId);
        List<DbInfo> list = dbInfoService.list(wrapper);
        return JsonResult.Get().putList(list);
    }

    @PostMapping("deleteById")
    @ApiOperation("根据id删除数据库资源，支持批量删除")
    @Log(title = "删除数据库资源",operateType = VipEnums.OperateType.DELETE)
    public JsonResult deleteById(@RequestBody List<Long> ids){
        boolean b = dbInfoService.removeByIds(ids);
        if (b){
            List arrayList = new ArrayList<DbInfoLog>();
            ids.forEach(item->{
                DbInfoLog log = new DbInfoLog();
                log.setDbId(item);
                log.setUserId(((ArmAdminUser)SecurityUtils.getSubject().getPrincipal()).getId());
                log.setContent("删除数据库");
                arrayList.add(log);
            });
            logService.saveBatch(arrayList);
        }
        return JsonResult.Get(b);
    }

    @PostMapping("uploadDbLogo")
    @ApiOperation("上传数据库标识logo")
    public JsonResult uploadDbLogo(@RequestParam("file") MultipartFile file ,@RequestParam("oldSource") String oldSource){
        if (StrUtil.isNotBlank(oldSource)){
            util.deleteFile(oldSource);
        }
        return (util.uploadUtil(file, "dbLogo"));
    }
}

