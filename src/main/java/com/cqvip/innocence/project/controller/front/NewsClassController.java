package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmNewsClass;
import com.cqvip.innocence.project.service.ArmNewsClassService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.expression.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 栏目新闻类型（前台）
 *
 * @author 01
 * @date 2021-10-26 13:10
 */
@RestController
@RequestMapping("/${base-url.front}/newsClass")
public class NewsClassController extends AbstractController {

    @Autowired
    ArmNewsClassService armNewsClassService;

    @ApiOperation("通过新闻栏目名获取对应分类")
    @GetMapping("/getSoftwareClassByColumnName")
    public JsonResult getSoftwareClassByColumnName(String ColumnName) {
        if (StringUtils.isBlank(ColumnName)) {
            return JsonResult.Get(false).setMsg("参数错误");
        }
        List<ArmNewsClass> newsClassList = armNewsClassService.getSoftwareClassByColumnName(ColumnName);
        return JsonResult.Get().put("data",newsClassList);
    }

}
