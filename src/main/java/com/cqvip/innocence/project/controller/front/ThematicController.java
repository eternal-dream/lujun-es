package com.cqvip.innocence.project.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.Log;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.esservice.DocumentService;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmClcClassInfo;
import com.cqvip.innocence.project.model.entity.ArmThematicDatabase;
import com.cqvip.innocence.project.model.entity.ArticleDoc;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmClcClassInfoService;
import com.cqvip.innocence.project.service.ArmThematicDatabaseService;
import com.cqvip.innocence.project.service.ArmThematicTypesService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 热点专题
 * @Author eternal
 * @Date 2021/9/17
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/thematic")
public class ThematicController extends AbstractController {

 @Autowired
 private ArmThematicDatabaseService thematicDatabaseService;

 @Autowired
 private ArmThematicTypesService thematicTypesService;

 @Autowired
 private ArmClcClassInfoService clcClassInfoService;

 /**
  * (分页)获取专题
  * @return
  */
 @GetMapping("getThematics")
 @Log(title = "查看专题")
 public JsonResult getThematics(){
  LambdaQueryWrapper<ArmThematicDatabase> wrapper = new LambdaQueryWrapper<>();
  wrapper.ne(ArmThematicDatabase::getThemeStatus, VipEnums.ThemeStatus.SOLD_OUT);
  wrapper.orderByDesc(ArmThematicDatabase::getThemeStatus);
  Page<ArmThematicDatabase> page = thematicDatabaseService.page(getPageParams(), wrapper);
  page.getRecords().forEach(thematic->{
   Map<String, Object> pageData = thematicDatabaseService.getThematicArticles(thematic, new Page(1, 1), null);
   Map<String, List<? extends Terms.Bucket>> agg = (Map<String, List<? extends Terms.Bucket>>)pageData.get("ag");
   List<? extends Terms.Bucket> articleTypeAgg = agg.get("articleTypeAgg");
   thematic.setTypeAggData(articleTypeAgg);

   ArmClcClassInfo clcClassInfo = clcClassInfoService.getById(thematic.getClcClassId());
   thematic.setClcClassName(clcClassInfo.getClassName());
  });
  return JsonResult.Get("thematicsPage",page);
 }

 private int getScore(VipEnums.ThemeStatus status){
  switch (status){
   case TOP:
    return 100;
   case NORMAL:
    return 50;
   case SOLD_OUT:
    return 0;
   default:
    return 0;
  }

 }

 /**
  *
  * @return
  */
 @GetMapping("getThematicArticles")
 public JsonResult getThematicArticles(Long id, Integer articleType){
  ArmThematicDatabase thematicDatabase = thematicDatabaseService.getById(id);
  if(thematicDatabase == null){
   return JsonResult.Get(false,"参数错误!");
  }
  Page pageParams = getPageParams();
  Map<String, Object> pageList = thematicDatabaseService.getThematicArticles(thematicDatabase, pageParams, articleType);
  Object page = pageList.get("page");
  return JsonResult.Get("pageData",page);
 }

 //热门推荐
 @GetMapping("recommendation")
 public JsonResult recommendation(Long id, VipEnums.ArticleType articleType){
  //查询逻辑未定
  ArmThematicDatabase thematicDatabase = thematicDatabaseService.getById(id);
  if(thematicDatabase == null){
   return JsonResult.Get(false,"参数错误!");
  }
  List list = thematicDatabaseService.recommendation(thematicDatabase);

  return JsonResult.Get("recommendationList",list);
 }

 @GetMapping("getAllThematicTypes")
 public JsonResult getAllThematicTypes(){
  return JsonResult.Get("thematicTypes",thematicTypesService.list());
 }
}