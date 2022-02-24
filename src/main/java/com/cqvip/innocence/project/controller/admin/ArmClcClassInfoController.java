package com.cqvip.innocence.project.controller.admin;


import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmClcClassInfo;
import com.cqvip.innocence.project.service.ArmClcClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.cqvip.innocence.project.controller.AbstractController;

import java.util.List;

/**
 * <p>
 * 中图分类表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-09-08
 */
@RestController
@RequestMapping("/${base-url.manager}/armClcClassInfo")
public class ArmClcClassInfoController extends AbstractController {

 @Autowired
 private ArmClcClassInfoService clcClassInfoService;

 @GetMapping("getAllClcClass")
 public JsonResult getAllClcClass(){
  List<ArmClcClassInfo> treeData = clcClassInfoService.getTreeData();

  return JsonResult.Get("allClcClass",treeData);
 }

}

