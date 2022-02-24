package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAnnex;
import com.cqvip.innocence.project.service.ArmAnnexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author eternal
 * @Date 2021/9/30
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/annex")
public class AnnexController extends AbstractController {

 @Autowired
 private ArmAnnexService annexService;

 @GetMapping("getAnnexes")
 public JsonResult getAnnexes(Long realId){
  LambdaQueryWrapper<ArmAnnex> wrapper = new LambdaQueryWrapper<>();
  wrapper.eq(ArmAnnex::getRealId,realId);
  List<ArmAnnex> annexes = annexService.list(wrapper);
  return JsonResult.Get("annexes",annexes);
 }
}