package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmQuestion;
import com.cqvip.innocence.project.model.entity.ArmQuestionClass;
import com.cqvip.innocence.project.service.ArmQuestionClassService;
import com.cqvip.innocence.project.service.ArmQuestionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问卷分类表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/${base-url.manager}/armQuestionClass")
public class ArmQuestionClassController extends AbstractController {

    @Autowired
    ArmQuestionClassService armQuestionClassService;

    @Autowired
    private ArmQuestionService questionService;

    /**
     * 获取 问题分类 列表（分页）
     *
     * @param armQuestionClass 查询条件
     * @param page          分页参数
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 13:51
     */
    @GetMapping("/getQuestionClassByPage")
    @ApiOperation("获取 问题分类 列表（分页）")
    public JsonResult getQuestionClassByPage(ArmQuestionClass armQuestionClass, Page page) {
        IPage<ArmQuestionClass> pageData = new Page<>(page.getCurrent(), page.getSize());
        //TODO  查询条件待定
        QueryWrapper<ArmQuestionClass> queryWrapper = new QueryWrapper<>();
        armQuestionClassService.page(pageData, queryWrapper);
        return JsonResult.Get().put("data", pageData);
    }

    /**
     * 通过id查询QuestionClass信息
     *
     * @param id QuestionClass.Id
     * @return {@link com.cqvip.innocence.project.model.dto.JsonResult}
     * @author 01
     * @date 2021/8/20 15:00
     */
    @GetMapping("/getQuestionClassById/{id}")
    @ApiOperation("通过id查询QuestionClass信息")
    public JsonResult getQuestionClassById(@PathVariable Long id) {
        if (id == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        ArmQuestionClass armQuestionClass = armQuestionClassService.getById(id);
        return JsonResult.Get().put("data", armQuestionClass);
    }

    /**
     * 添加或修改 问题分类
     *
     * @param armQuestionClass QuestionClass信息
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 14:00
     */
    @PostMapping("/addOrModifyQuestionClass")
    @ApiOperation("添加或修改“问题分类”")
    public JsonResult addOrModifyQuestionClass(ArmQuestionClass armQuestionClass) {
        if (armQuestionClass == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        try {
            armQuestionClassService.saveOrUpdate(armQuestionClass);
        } catch (Exception ex) {
            log.info("修改或新增“问题分类”操作失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get().setMsg("操作成功");
    }

    /**
     * 通过QuestionClass id逻辑删除
     *
     * @param ids QuestionClass.id
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 16:01
     */
    @PostMapping("/deleteQuestionClass")
    @ApiOperation("通过QuestionClass.id逻辑删除")
    public JsonResult deleteQuestionClass(@RequestBody Long[] ids) {
        if (ids == null || ids.length == 0) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        LambdaQueryWrapper<ArmQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ArmQuestion::getClassId,ids);
        List<ArmQuestion> questions = questionService.list(wrapper);
        if(questions != null && questions.size()>0){
            return JsonResult.Get(false,"删除失败,存在引用此分类的问题!");
        }
        try {
            armQuestionClassService.removeByIds(Arrays.asList(ids));
        } catch (Exception ex) {
            log.info("通过id删除QuestionClass失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }

        return JsonResult.Get().setMsg("操作成功");
    }

    //获取所有问题分类
    @GetMapping("getAllQuestionClass")
    public JsonResult getAllQuestionClass(){
        return JsonResult.Get("questionClasses",armQuestionClassService.list());
    }

}

