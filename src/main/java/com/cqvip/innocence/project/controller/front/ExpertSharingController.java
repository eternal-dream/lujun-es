package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmExpertSharing;
import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmExpertSharingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author eternal
 * @Date 2021/9/29
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/expertSharing")
public class ExpertSharingController extends AbstractController {

 @Autowired
 private ArmExpertSharingService expertSharingService;

 @Autowired
 private RedisTemplate redisTemplate;

 @PostMapping("share")
 @Log(title = "分享资源",operateType = VipEnums.OperateType.ADD)
 public JsonResult share(String title, String url, HttpServletRequest request){
  if(StringUtils.isAnyBlank(title,url)){
   return JsonResult.Get(false,"参数错误");
  }
  String sessionId = request.getSession().getId();
  ArmUserInfo info = (ArmUserInfo) redisTemplate.opsForValue().get(sessionId);
  LambdaQueryWrapper<ArmExpertSharing> wrapper = new LambdaQueryWrapper<>();
  wrapper.eq(ArmExpertSharing::getUserId,info.getId())
   .eq(ArmExpertSharing::getUrl,url);
  int count = expertSharingService.count(wrapper);
  if(count != 0){
   return JsonResult.Get(false,"该文章已被分享，请勿重复操作");
  }
  ArmExpertSharing expertSharing = new ArmExpertSharing();
  expertSharing.setTitle(title);
  expertSharing.setUrl(url);
  expertSharing.setUserId(info.getId());
  expertSharing.insert();

  return JsonResult.Get();
 }
}