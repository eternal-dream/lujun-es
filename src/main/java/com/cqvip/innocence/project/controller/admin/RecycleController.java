package com.cqvip.innocence.project.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAdminUserLog;
import com.cqvip.innocence.project.model.entity.ArmNewsLog;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmAdminUserLogService;
import com.cqvip.innocence.project.service.ArmAdminUserService;
import com.cqvip.innocence.project.service.ArmNewsLogService;
import com.cqvip.innocence.project.service.ArmNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * @ClassName RecycleController
 * @Description 回收站相关业务接口，主要是对管理员账户和新闻的回收或彻底删除
 * @Author Innocence
 * @Date 2021/8/25 9:09
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.manager}/recycle")
@Api(tags = "管理端回收站功能接口")
public class RecycleController extends AbstractController {

    @Autowired
    private ArmAdminUserLogService adminUserLogService;

    @Autowired
    private ArmAdminUserService adminUserService;

    @Autowired
    private ArmNewsLogService newsLogService;

    @Autowired
    private ArmNewsService newsService;

    @GetMapping("getPageListByType")
    @ApiOperation("根据回收类型查询回收的资源信息,默认查询新闻")
    public JsonResult getPageListByType(VipEnums.SysRecycleType type ,String searchKey){
        Page pageParams = getPageParams();
        if (type.equals(VipEnums.SysRecycleType.ADMIN.name())){
            LambdaQueryWrapper<ArmAdminUserLog> wrapper = new QueryWrapper<ArmAdminUserLog>().lambda()
                    .orderByDesc(ArmAdminUserLog::getModifyTime);
            if (StrUtil.isNotBlank(searchKey)){
                wrapper.like(ArmAdminUserLog::getTitle,searchKey);
            }
            return JsonResult.Get().putPage(adminUserLogService.page(pageParams,wrapper));
        }else {
            LambdaQueryWrapper<ArmNewsLog> wrapper = new QueryWrapper<ArmNewsLog>().lambda()
                    .orderByDesc(ArmNewsLog::getModifyTime);
            if (StrUtil.isNotBlank(searchKey)){
                wrapper.like(ArmNewsLog::getTitle,searchKey);
            }
            return JsonResult.Get().putPage(newsLogService.page(pageParams,wrapper));
        }
    }

    @PostMapping("deleteOrRestore")
    @ApiOperation("彻底删除还是恢复操作")
    public JsonResult deleteOrRestore(VipEnums.SysRecycleType type , Serializable logId, VipEnums.OperateType operateType){
        //管理员操作
        if (type.equals(VipEnums.SysRecycleType.ADMIN.name())){
            ArmAdminUserLog byId = adminUserLogService.getById(logId);
            //彻底删除
            if (operateType.equals(VipEnums.OperateType.DELETE)){
                int i = adminUserService.deleteByIdWithXml(byId.getId(), byId.getObjId());
                if (i != 0){
                    return JsonResult.Get().setMsg("删除管理员成功");
                }else{
                    return JsonResult.Get().setMsg("删除管理员失败");
                }
            }else {
                boolean restore = adminUserService.restore(byId);
                return JsonResult.Get(restore);
            }
        }else{//新闻操作
            ArmNewsLog newsLog = newsLogService.getById(logId);
            if (operateType.equals(VipEnums.OperateType.DELETE)){
                int i = newsService.deleteByIdWithXml(newsLog.getObjId(),newsLog.getId());
                if (i != 0){
                    return JsonResult.Get().setMsg("删除新闻成功");
                }else{
                    return JsonResult.Get().setMsg("删除新闻失败");
                }
            }else{
                boolean restore = newsService.restoreNews(newsLog);
                return JsonResult.Get(restore);
            }
        }
    }
}
