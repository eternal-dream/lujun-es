package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.XssExclusion;
import com.cqvip.innocence.common.sessions.SessionKeys;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.ArmThematicDatabase;
import com.cqvip.innocence.project.model.entity.base.BaseModel;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmThematicDatabaseService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 * 专题数据库 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
@RestController
@RequestMapping("/${base-url.manager}/armThematicDatabase")
public class ArmThematicDatabaseController extends AbstractController {
    @Autowired
    ArmThematicDatabaseService armThematicDatabaseService;

    @GetMapping("/getThematicDatabaseByPage")
    @ApiOperation("获取 热点专题 列表（分页）")
    public JsonResult getThematicDatabaseByPage(ArmThematicDatabase armThematicDatabase, Page page) {
        IPage<ArmThematicDatabase> armThematicDatabaseList = null;
        armThematicDatabaseList = armThematicDatabaseService.getThematicTypesByPage(armThematicDatabase, page);
        return JsonResult.Get().put("data", armThematicDatabaseList);
    }

    @GetMapping("/getThematicDatabaseById/{id}")
    @ApiOperation("通过id查询 热点专题")
    public JsonResult getThematicDatabaseById(@PathVariable Long id ){
        if (id == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        ArmThematicDatabase armThematicDatabase = null;
        try {
            armThematicDatabase = armThematicDatabaseService.getById(id);
        } catch (Exception ex) {
            return JsonResult.Get(false).put("data", null);
        }
        return JsonResult.Get().put("data", armThematicDatabase);
    }

    @PostMapping("/deleteThematicDatabase")
    @ApiOperation("通过id删除 热点专题 ")
    public JsonResult deleteThematicDatabase(@RequestBody Long[] ids) {
        return JsonResult.Get(armThematicDatabaseService.removeByIds(Arrays.asList(ids)));
    }

    @PostMapping("/offThematicDatabase")
    @ApiOperation("通过id下架专题 ")
    public JsonResult offThematicDatabase(@RequestBody Long[] ids) {
        LambdaUpdateWrapper<ArmThematicDatabase> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(ArmThematicDatabase::getThemeStatus, VipEnums.ThemeStatus.SOLD_OUT)
         .in(BaseModel::getId,ids);

        return JsonResult.Get(armThematicDatabaseService.update(wrapper));
    }

    @PostMapping("/saveOrUpdateThematicDatabase")
    @ApiOperation("添加或修改“热点专题”")
    @XssExclusion
    public JsonResult saveOrUpdateThematicDatabase(ArmThematicDatabase armThematicTypes) {
        if (armThematicTypes == null) {
            return JsonResult.Get(false).setCode("500").setMsg("参数错误");
        }
        ArmAdminUser loginAdmin = (ArmAdminUser) SecurityUtils.getSubject().getSession().getAttribute(SessionKeys.LOGIN_ADMIN_KEY);
        armThematicTypes.setUserId(loginAdmin.getId());
        try {
            //TODO 根据逻辑来处理
            armThematicDatabaseService.saveOrUpdate(armThematicTypes);
        } catch (Exception ex) {
            log.info("修改或新增“热点专题”操作失败，原因：{}", ex.getMessage());
            return JsonResult.Get(false).setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get().setMsg("操作成功");
    }

}

