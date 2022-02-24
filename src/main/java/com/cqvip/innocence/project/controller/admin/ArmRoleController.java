package com.cqvip.innocence.project.controller.admin;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAdminUserRole;
import com.cqvip.innocence.project.model.entity.ArmRole;
import com.cqvip.innocence.project.model.entity.ArmRoleResource;
import com.cqvip.innocence.project.service.ArmAdminUserRoleService;
import com.cqvip.innocence.project.service.ArmResourceService;
import com.cqvip.innocence.project.service.ArmRoleResourceService;
import com.cqvip.innocence.project.service.ArmRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/${base-url.manager}/armRole")
public class ArmRoleController extends AbstractController {

    @Autowired
    private ArmRoleService roleService;

    @Autowired
    private ArmResourceService resourceService;

    @Autowired
    private ArmRoleResourceService roleResourceService;

    @Autowired
    private ArmAdminUserRoleService adminUserRoleService;


    /**
     * 获取全部角色
     *
     * @return
     */
    @GetMapping("getTotalRoles")
    public JsonResult getRoles() {
        return JsonResult.Get("totalRoles", roleService.list());
    }

    /**
     * 分页获取角色列表
     *
     * @param name
     * @return
     */
    @GetMapping("getRoles")
    public JsonResult getRoles(String name) {
        QueryWrapper<ArmRole> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), "NAME", name);
        wrapper.orderByDesc("CREATE_TIME");
        List<ArmRole> roles = roleService.getRoles(name,getPageParams());
        roles.forEach(role-> {
            if(role.getResourceStr()==null){
                return;
            }
            String[] split = role.getResourceStr().split(",");
            List<Long> ids = new ArrayList<>();
            for (String id : split) {
                ids.add(Long.parseLong(id));
            }
            role.setResourceIds(ids);
        });
        int count = roleService.count(wrapper);
        return JsonResult.Get("roles", roles).put("total",count);
    }

    @PostMapping("addOrModifyRole")
    public JsonResult addOrModifyRole(@RequestBody Object object) {
        JSONObject jsonObject = JSONObject.parseObject(object.toString());
        String name = jsonObject.getString("name");
        Long roleId = jsonObject.getLong("id");
        String description = jsonObject.getString("description");
        JSONArray resourceIds = jsonObject.getJSONArray("resourceIds");
        if (StringUtils.isBlank(name)) {
            return JsonResult.Get(false, "角色名不能为空!");
        }
        ArmRole role;
        if (roleId == null) {//新增
            role = new ArmRole();
        } else {//编辑
            role = roleService.getById(roleId);
        }
        role.setName(name);
        if (!StringUtils.isBlank(description)) {
            role.setDescription(description);
        }
        roleService.saveOrUpdate(role);
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < resourceIds.size(); i++) {
            list.add(resourceIds.getLong(i));
        }
        if (!list.isEmpty() && list.size() > 0) {
            roleResourceService.removeById(role.getId());
            List<ArmRoleResource> roleResources = new ArrayList<>();
            for (Long id : list) {
                ArmRoleResource roleResource = new ArmRoleResource();
                roleResource.setRoleId(role.getId());
                roleResource.setResourceId(id);
                roleResources.add(roleResource);
            }
            roleResourceService.saveBatch(roleResources);
        } else {
            QueryWrapper<ArmRoleResource> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role_id", roleId);
            roleResourceService.remove(queryWrapper);
        }
        return JsonResult.Get(true);
    }

    @PostMapping("deleteRole")
    public JsonResult deleteRole(@RequestBody Long[] deleteIds) {
        if (deleteIds == null || deleteIds.length == 0) {
            return JsonResult.Get(false, "请提交要删除的角色id");
        }
        QueryWrapper<ArmRoleResource> roleResourceWrapper = new QueryWrapper<>();
        QueryWrapper<ArmAdminUserRole> adminRoleWrapper = new QueryWrapper<>();
        List<Long> list = new ArrayList<>();
        roleResourceWrapper.in("ROLE_ID", deleteIds);
        adminRoleWrapper.in("ROLE_ID", deleteIds);
        roleResourceService.remove(roleResourceWrapper);
        adminUserRoleService.remove(adminRoleWrapper);
        for (int i = 0; i < deleteIds.length; i++) {
            list.add(deleteIds[i]);
        }
        roleService.removeByIds(list);
        return JsonResult.Get();

    }

    /**
     * 根据用户id获取其角色
     * @param id
     * @return
     */
    @GetMapping("getAdminRoles")
    public JsonResult getAdminRoles(Long id){
        if(id == null ){
            return JsonResult.Get();
        }
        LambdaQueryWrapper<ArmAdminUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArmAdminUserRole::getAdminUserId,id);
        List<ArmAdminUserRole> list = adminUserRoleService.list(wrapper);
        if(list.size() == 0){
            return JsonResult.Get("roles",null);
        }
        List<Long> roleIds = new ArrayList<>();
        list.forEach(item->roleIds.add(item.getRoleId()));
        List<ArmRole> armRoles = roleService.listByIds(roleIds);
        return JsonResult.Get("roles",armRoles);
    }

}

