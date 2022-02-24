package com.cqvip.innocence.project.controller.admin;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmQuestionAireOpt;
import com.cqvip.innocence.project.service.ArmQuestionAireOptService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 问卷详细表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/${base-url.manager}/armQuestionAireOpt")
public class ArmQuestionAireOptController extends AbstractController {
    @Autowired
    ArmQuestionAireOptService armQuestionAireOptService;

    @GetMapping("/getQuestionAireOptByPage")
    @ApiOperation("获取“问卷调查表选项”分页列表")
    public JsonResult getQuestionAireOptByPage(ArmQuestionAireOpt armQuestionAireOpt, Page page){
        IPage<Map<String, Object>> pageParams = new Page<>(page.getCurrent(), page.getSize());
        //TODO  查询条件待定
        QueryWrapper<ArmQuestionAireOpt> queryWrapper = new QueryWrapper<>();
        if (armQuestionAireOpt != null && armQuestionAireOpt.getId() != null) {
            queryWrapper.eq("QUESTION_AIRE_ID", armQuestionAireOpt.getId());
        }
        if(armQuestionAireOpt != null && armQuestionAireOpt.getTitle() != null){
            queryWrapper.eq("TITLE", armQuestionAireOpt.getTitle());
        }
        IPage<Map<String, Object>> data = armQuestionAireOptService.pageMaps(pageParams, queryWrapper.orderByAsc("create_time"));
        return JsonResult.Get().put("data", data);
    }

    @PostMapping("/deleteQuestionAireOptById")
    @ApiOperation("通过id删除“问卷调查表选项”")
    public JsonResult deleteQuestionAireOptById(Long id){
        if (id == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        try{
            boolean removeById = armQuestionAireOptService.removeById(id);
        }catch(Exception ex){
            log.info("通过id逻辑删除问卷调查表信息失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get();
    }

    @PostMapping("/saveOrUpdateQuestionAireOpt")
    @ApiOperation("添加或修改“问卷调查表选项”")
    public JsonResult saveOrUpdateQuestionAireOpt(ArmQuestionAireOpt armQuestionAireOpt) {
        if (armQuestionAireOpt == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        try {
            armQuestionAireOptService.saveOrUpdate(armQuestionAireOpt);
        } catch (Exception ex) {
            log.info("修改或新增“问卷调查表选项”操作失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get().setMsg("操作成功");
    }

    @PostMapping("/importAndSaveQuestionAireOpt")
    @ApiOperation("添加或修改“问卷调查表选项”")
    public JsonResult importAndSaveQuestionAireOpt(MultipartFile file, String title){
        if (StringUtils.isBlank(title)) {
            return JsonResult.Get(false).setMsg("参数异常");
        }
        try {
            //TODO
            //通过文件导入问卷调查表选项
            InputStream inputStream = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(inputStream);
            List<List<Object>> list = reader.read();
            List<ArmQuestionAireOpt> questionAireOptList = new ArrayList<>();
            for (int i = 2; i < list.size(); i++) {

            }

        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.Get(false, "导入异常，请用指定模板导入，并严格按照文档中的说明来填写！");
        }
        return JsonResult.Get();
    }
}

