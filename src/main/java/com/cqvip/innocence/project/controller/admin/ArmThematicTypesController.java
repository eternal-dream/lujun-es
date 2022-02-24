package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.sessions.SessionKeys;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.ArmThematicDatabase;
import com.cqvip.innocence.project.model.entity.ArmThematicTypes;
import com.cqvip.innocence.project.service.ArmThematicDatabaseService;
import com.cqvip.innocence.project.service.ArmThematicTypesService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 专题库类型表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-24
 */
@RestController
@RequestMapping("/${base-url.manager}/armThematicTypes")
public class ArmThematicTypesController extends AbstractController {
    @Autowired
    ArmThematicTypesService armThematicTypesService;

    @Autowired
    private ArmThematicDatabaseService thematicDatabaseService;

    /**
     * 获取 专题分类 列表（分页）
     *
     * @param armThematicTypes 查询条件
     * @param page             分页参数
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 13:51
     */
    @GetMapping("/getThematicTypesByPage")
    @ApiOperation("获取 专题分类 列表（分页）")
    public JsonResult getThematicTypesByPage(ArmThematicTypes armThematicTypes, Page page) {
        IPage<ArmThematicTypes> armThematicTypesDTOList = null;
        try {
            armThematicTypesDTOList = armThematicTypesService.getThematicTypesByPage(armThematicTypes, page);
        } catch (Exception ex) {
            return JsonResult.Get(false).put("data", null);
        }
        return JsonResult.Get().put("data", armThematicTypesDTOList);
    }

    /**
     * 通过id查询专题分类信息
     *
     * @param id ThematicTypes.Id
     * @return {@link com.cqvip.innocence.project.model.dto.JsonResult}
     * @author 01
     * @date 2021/8/20 15:00
     */
    @GetMapping("/getThematicTypesById/{id}")
    @ApiOperation("通过id查询QuestionClass信息")
    public JsonResult getThematicTypesById(@PathVariable Long id) {
        if (id == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        ArmThematicTypes armThematicTypes = armThematicTypesService.getById(id);
        return JsonResult.Get().put("data", armThematicTypes);
    }

    /**
     * 添加或修改 专题分类
     *
     * @param armThematicTypes 专题分类信息
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 14:00
     */
    @PostMapping("/saveOrUpdateThematicTypes")
    @ApiOperation("添加或修改“专题分类”")
    public JsonResult saveOrUpdateThematicTypes(ArmThematicTypes armThematicTypes) {
        if (armThematicTypes == null) {
            return JsonResult.Get(false).setCode("500").setMsg("参数错误");
        }
        try {
            ArmAdminUser loginAdmin = (ArmAdminUser) SecurityUtils.getSubject().getSession().getAttribute(SessionKeys.LOGIN_ADMIN_KEY);
            armThematicTypes.setUserId(loginAdmin.getId());
            armThematicTypesService.saveOrUpdate(armThematicTypes);
        } catch (Exception ex) {
            log.info("修改或新增“专题分类”操作失败，原因：{}", ex.getMessage());
            return JsonResult.Get(false).setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get().setMsg("操作成功");
    }

    /**
     * 通过专题分类 id逻辑删除
     *
     * @param ids 专题分类.id
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 16:01
     */
    @PostMapping("/deleteThematicType")
    @ApiOperation("通过QuestionClass.id逻辑删除")
    public JsonResult deleteThematicType(@RequestBody Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        LambdaQueryWrapper<ArmThematicDatabase> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ArmThematicDatabase::getThematicTypeId,idList);
        int count = thematicDatabaseService.count(wrapper);
        if(count>0){
            return JsonResult.Get(false,"删除失败,存在引用此分类的专题库!");
        }
        return JsonResult.Get(armThematicTypesService.removeByIds(idList));
    }

    @GetMapping("getAllThematicTypes")
    public JsonResult getAllThematicTypes(){
        return JsonResult.Get("thematicTypes",armThematicTypesService.list());
    }
}

