package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAdminGroup;
import com.cqvip.innocence.project.service.ArmAdminGroupService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.cqvip.innocence.project.controller.AbstractController;

import java.util.List;

/**
 * <p>
 * 管理员分类表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-20
 */
@RestController
@RequestMapping("/${base-url.manager}/armAdminGroup")
public class ArmAdminGroupController extends AbstractController {

    @Autowired
    private ArmAdminGroupService adminGroupService;

    @GetMapping("getAllAdminGroup")
    public JsonResult getAllAdminGroup() {
        List<ArmAdminGroup> list = adminGroupService.list();
        return JsonResult.Get("allAdminGroup", list);
    }

    @GetMapping("getAdminGroup")
    public JsonResult getAdminGroup(ArmAdminGroup group, Integer pageNum, Integer pageSize) {
        QueryWrapper<ArmAdminGroup> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(group.getName()), "NAME", group.getName());
        Page<ArmAdminGroup> groupPage = adminGroupService.page(new Page<>(pageNum, pageSize), wrapper);
        return JsonResult.Get("adminGroup", groupPage);
    }

    @PostMapping("addOrEditAdminGroup")
    public JsonResult addAdminGroup(ArmAdminGroup adminGroup) {
        adminGroup.insertOrUpdate();
        return JsonResult.Get();
    }


}

