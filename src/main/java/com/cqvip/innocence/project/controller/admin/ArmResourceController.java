package com.cqvip.innocence.project.controller.admin;


import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.MenuNode;
import com.cqvip.innocence.project.model.dto.ResourceNode;
import com.cqvip.innocence.project.model.dto.router.Meta;
import com.cqvip.innocence.project.model.dto.router.Router;
import com.cqvip.innocence.project.model.entity.ArmAdminUser;
import com.cqvip.innocence.project.model.entity.ArmResource;
import com.cqvip.innocence.project.model.entity.ArmRole;
import com.cqvip.innocence.project.model.enums.BusinessType;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmResourceService;
import com.cqvip.innocence.project.service.ArmRoleService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.action.search.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cqvip.innocence.project.controller.AbstractController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 资源表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/${base-url.manager}/armResource")
public class ArmResourceController extends AbstractController {

    @Autowired
    private ArmResourceService resourceService;

    @Autowired
    private ArmRoleService roleService;
    /**
     * resource tree-data
     *
     * @param
     * @return {@link JsonResult}
     * @throws
     * @author 01
     * @date 2021/9/3 9:00
     */
    @GetMapping("/getResourcesTreeData")
    @ApiOperation("以tree结构，获取resource")
    public JsonResult getResourcesTreeData() {
        List<ResourceNode> treeData = resourceService.getResourceTreeData();
        return JsonResult.Get().put("data", treeData);
    }


    /**
     * 获取所有菜单资源
     *
     * @return
     */
    @GetMapping("getAllResources")
    public JsonResult getTotalResources() {
        List<ArmResource> resources = resourceService.list().stream().sorted(Comparator.comparing(ArmResource::getSort)).collect(Collectors.toList());
        List<Router> routers = processRouter(resources);
//  List<Router> routers = new ArrayList<>();
//  Router router = new Router();
//  router.setTitle("测试");
//  router.setName("test");
//  router.setPath("/demo/components/container/full");
//  router.setIcon("");
//  router.setId(111L);
//
//  Router router1 = new Router();
//  router1.setTitle("测试2");
//  router1.setPath("/demo/test1");
//  router1.setIcon("");
//  router1.setComponent("demo/test1.vue");
//  Meta meta = new Meta();
//  meta.setNoCache(false);
//  meta.setTitle("测试1111");
//  router1.setMeta(meta);
//  router1.setName("test1");
//  router1.setParentId(111L);
//
//  List<Router> children = new ArrayList<>();
//  children.add(router1);
//  router.setChildren(children);
//  routers.add(router);

        return JsonResult.Get("resources", routers);
    }

    /**
     * 根据当前登录用户获取菜单
     */
    @GetMapping("getUserResource")
    public JsonResult getParentResource() {
        Subject subject = SecurityUtils.getSubject();
        ArmAdminUser loginAdmin = (ArmAdminUser) subject.getPrincipal();
        if (loginAdmin == null) {
            return JsonResult.Get();
        }

        List<ArmRole> roles = roleService.getRolesByAdmin(loginAdmin);
        List<ArmResource> resources = new ArrayList<>();
        for (ArmRole role : roles) {
            List<ArmResource> roleResources = resourceService.getRoleResources(role);
            roleResources.forEach(resource -> {
                if (!resource.getMenuType().equals("F")) {
                    resources.add(resource);
                }
            });
        }

        List<Router> routers = processRouter(resources);
        return JsonResult.Get("resources", routers);

    }

    //组装路由对象集合
    private List<Router> processRouter(List<ArmResource> resources) {
        //去重+排序
        List<ArmResource> resourceList = resources.stream()
                .sorted(Comparator.comparing(ArmResource::getSort))
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(r -> r.getId()))), ArrayList::new));

        List<Router> routers = new ArrayList<>();
        //定义一个最外层的容器
        Router out = new Router();
        out.setComponent("Layout");
        out.setPath("/");
        defineRouter(out, resourceList);

        return out.getChildren();
    }

    private void defineRouter(Router parent, List<ArmResource> resources) {
        Long id = parent.getId();
        List<ArmResource> subResources = resources.stream().filter(r -> {
            return (id == null && r.getParentId() == null) || (id != null && id.equals(r.getParentId()));
        }).collect(Collectors.toList());
//  if(subResources.size()==0){
//   return;
//  }
        List<Router> children = new ArrayList<>();
        subResources.forEach(resource -> {
            Router router = new Router();
            //组装固定meta和其他固定信息
            Meta meta = new Meta();
            meta.setIcon(resource.getMenuIcon());
            meta.setNoCache(false);
            meta.setTitle(resource.getName());
            router.setMeta(meta);
            router.setIcon(resource.getMenuIcon());
            router.setTitle(resource.getName());
            router.setHidden(resource.getIsHidden());
            router.setId(resource.getId());
            router.setMenuType(resource.getMenuType());
            router.setParentId(resource.getParentId());
            router.setAlwaysShow(false);
            String s = resource.getMenuUrl().substring(resource.getMenuUrl().lastIndexOf("/") + 1);
            String name = s.substring(0, 1).toUpperCase() + s.substring(1);
            router.setName(name);

            if (StrUtil.isBlank(resource.getComponent())) {
                router.setComponent(null);
            } else {
                router.setComponent(resource.getComponent());
            }
            router.setPath(StringUtils.isNotBlank(resource.getMenuUrl()) ? resource.getMenuUrl() : "");
            if ("C".equals(resource.getMenuType())) {
                defineRouter(router, resources);
//    if (resource.getParentId()==null){
//     router.setComponent("Layout");
//     router.setPath("/");
//    }else {
//     router.setPath(resource.getMenuUrl());
//     if (StrUtil.isBlank(resource.getComponent())){
//      router.setComponent(null);
//     }else {
//      router.setComponent(resource.getComponent());
//     }
//    }
//    Router inner = new Router();
//    inner.setName(resource.getName());
//    inner.setPath("/"+resource.getMenuUrl().replace("/",""));
//    inner.setHidden(false);
//    if (StrUtil.isBlank(resource.getComponent())){
//     inner.setComponent(null);
//    }else {
//     inner.setComponent(resource.getComponent());
//    }
//    inner.setMeta(meta);
//    List<Router> list = new ArrayList<>();
//    list.add(inner);
//    router.setChildren(list);
            } else if ("M".equals(resource.getMenuType())) {
//    router.setComponent("Layout");
                //递归获取此菜单下的子菜单
                defineRouter(router, resources);
            }
            children.add(router);

        });
        parent.setChildren(children);

    }


    @PostMapping("saveOrEditResource")
    @ApiOperation("新增或者编辑resource")
    //@RequiresPermissions("admin:resource:addOrEdit")
    public JsonResult saveOrEditResource(ArmResource resource) {
        try {
            resourceService.saveOrUpdate(resource);
        } catch (Exception ex) {
            return JsonResult.Get(false, "操作失败");
        }
        return JsonResult.Get(true, "操作成功");
    }

    @PostMapping("deleteResourceByIds")
    @ApiOperation("删除一个或多个resource")
    //@RequiresPermissions("admin:resource:delete")
    public JsonResult deleteResourceByIds(@RequestBody Long[] ids) {
        try {
            resourceService.removeByIds(Arrays.asList(ids));
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsonResult.Get(false, "删除失败");
        }
        return JsonResult.Get(true, "删除成功");
    }

}

