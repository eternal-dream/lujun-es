package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmFavourite;
import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmFavouriteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 收藏夹
 * @Author eternal
 * @Date 2021/9/28
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/collection")
public class CollectionController extends AbstractController {

 @Autowired
 private ArmFavouriteService favouriteService;

 @Autowired
 private RedisTemplate redisTemplate;

 @GetMapping("getCollections")
 @Log(title="获取收藏文章信息")
 public JsonResult getCollections(Long classId, String title, HttpServletRequest request){
  String sessionId = request.getSession().getId();
  ArmUserInfo info = (ArmUserInfo) redisTemplate.opsForValue().get(sessionId);
  LambdaQueryWrapper<ArmFavourite> wrapper = new LambdaQueryWrapper<>();
  wrapper.eq(StringUtils.isNotBlank(title),ArmFavourite::getTitle,title)
   .eq(ArmFavourite::getUserId,info.getId())
   .eq(classId != null,ArmFavourite::getClassId,classId);
  Page page = favouriteService.page(getPageParams(), wrapper);
  return JsonResult.Get("pageData",page);
 }

 @PostMapping("deleteCollections")
 @Log(title = "取消收藏",operateType = VipEnums.OperateType.DELETE)
 public JsonResult deleteCollections(@RequestBody List<Long> ids){
  favouriteService.removeByIds(ids);
  return  JsonResult.Get();
 }

 @PostMapping("addOrEdit")
 public JsonResult addOrEdit(ArmFavourite favourite){
  favouriteService.saveOrUpdate(favourite);
  return JsonResult.Get();
 }
}