package com.cqvip.innocence.project.controller.front;

import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.MenuNode;
import com.cqvip.innocence.project.service.ArmMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author eternal
 * @Date 2021/9/18
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/menu")
public class MenuController extends AbstractController {

 @Autowired
 private ArmMenuService menuService;

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
 public JsonResult getMenuTreeData(@RequestParam(required = false, defaultValue = "0") Integer level) {
  List<MenuNode> menuTreeData = menuService.getMenuTreeDataByLevel(level);
  return JsonResult.Get().put("data", menuTreeData);
 }

 @GetMapping("/getParentMenuByUrl")
 public JsonResult getParentMenuByUrl(String url){
   return JsonResult.Get("menu",menuService.getParentMenuByUrl(url));
 }
}