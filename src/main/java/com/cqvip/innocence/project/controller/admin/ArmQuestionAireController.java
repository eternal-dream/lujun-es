package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmQuestionAire;
import com.cqvip.innocence.project.service.ArmQuestionAireService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 问卷调查表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/${base-url.manager}/armQuestionAire")
public class ArmQuestionAireController extends AbstractController {

    @Autowired
    ArmQuestionAireService armQuestionAireService;

    /**
     * 获取 问卷调查表 列表（分页）
     *
     * @param armQuestionAire
     * @param page
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 16:22
     */
    @GetMapping("/getArmQuestionAireByPage")
    @ApiOperation(" 获取 问卷调查表 列表（分页）")
    public JsonResult getArmQuestionAireByPage(ArmQuestionAire armQuestionAire, Page page) {
        IPage<Map<String, Object>> pageParams = new Page<>(page.getCurrent(), page.getSize());
        //TODO  查询条件待定
        QueryWrapper<ArmQuestionAire> queryWrapper = new QueryWrapper<>();
        if (armQuestionAire != null && armQuestionAire.getQuestiontype() != null) {
            queryWrapper.eq("type", armQuestionAire.getQuestiontype());
        }
        IPage<Map<String, Object>> data = armQuestionAireService.pageMaps(pageParams, queryWrapper.orderByAsc("create_time"));
        return JsonResult.Get().put("data", data);
    }

    /**
     * 通过id 问卷调查表信息
     *
     * @param id 新闻id
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 16:33
     */
    @GetMapping("/getArmQuestionAireById/{id}")
    @ApiOperation("通过id 问卷调查表信息")
    public JsonResult getArmQuestionAireById(@PathVariable Long id) {
        if (id == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        ArmQuestionAire armQuestionAire = armQuestionAireService.getById(id);
        return JsonResult.Get().put("data", armQuestionAire);
    }

    /**
     * 添加或修改 问卷调查表信息
     *
     * @param armQuestionAire 问卷调查表信息
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 14:00
     */
    @PostMapping("/saveOrUpdateNews")
    @ApiOperation("添加或修改“问卷调查表”")
    public JsonResult saveOrUpdateArmQuestionAire(ArmQuestionAire armQuestionAire) {
        if (armQuestionAire == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        try {
            armQuestionAireService.saveOrUpdate(armQuestionAire);
        } catch (Exception ex) {
            log.info("修改或新增“问卷调查表”操作失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get().setMsg("操作成功");
    }

    /**
     * 通过id逻辑删除问卷调查表信息
     *
     * @param id News.id
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 16:01
     */
    @PostMapping("/deleteArmQuestionAireById")
    @ApiOperation("通过id逻辑删除问卷调查表信息")
    public JsonResult deleteArmQuestionAireById(@RequestParam(name = "id", required = true) Long id) {
        if (id == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        try {
            armQuestionAireService.removeById(id);
        } catch (Exception ex) {
            log.info("通过id逻辑删除问卷调查表信息失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }

        return JsonResult.Get().setMsg("操作成功");
    }
}

