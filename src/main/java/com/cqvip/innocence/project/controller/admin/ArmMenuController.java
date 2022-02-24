package com.cqvip.innocence.project.controller.admin;


import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.MenuNode;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.ArmMenu;
import com.cqvip.innocence.project.service.ArmMenuService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.util.Arrays;
import java.util.List;
import org.apache.shiro.SecurityUtils;

/**
 * <p>
 * 菜单（前台）管理表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-25
 */
@RestController
@RequestMapping("/${base-url.manager}/armMenu")
public class ArmMenuController extends AbstractController {
    @Autowired
    ArmMenuService armMenuService;

    /**
     * 获取菜单（前台）管理treeData
     *
     * @param
     * @return {@link JsonResult}
     * @throws
     * @author 01
     * @date 2021/8/25 13:43
     */
    @GetMapping("/getMenuTreeData")
    @ApiOperation("获取前台菜单TreeData”")
    public JsonResult getMenuTreeData(@RequestParam(required = false,defaultValue = "0") Integer level) {
        List<MenuNode> menuTreeData = armMenuService.getMenuTreeDataByLevel(level);
        return JsonResult.Get().put("data",menuTreeData);
    }

    @PostMapping("/saveOrUpdateMenu")
    @ApiOperation("添加或修改“前台菜单”")
    public JsonResult saveOrUpdateMenu(ArmMenu armMenu) {
        if (armMenu == null) {
            return JsonResult.Get(false).setCode("500").setMsg("参数错误");
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            ArmAdminUser armAdminUser =(ArmAdminUser) subject.getPrincipal();
            armMenu.setFounderId(armAdminUser.getId());
            armMenuService.saveOrUpdate(armMenu);
        } catch (Exception ex) {
            log.info("添加或修改“前台菜单”操作失败，原因：{}", ex.getMessage());
            return JsonResult.Get(false).setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get().setMsg("操作成功");
    }

    @PostMapping("/deleteMenuById")
    @ApiOperation("通过id删除“前台菜单”")
    public JsonResult deleteNewsClassById(@RequestBody Long[] ids) {
        if (ids == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        try {
            armMenuService.removeByIds(Arrays.asList(ids));
        } catch (Exception ex) {
            log.info("通过id删除“前台菜单”信息失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }
        return JsonResult.Get();
    }
}

