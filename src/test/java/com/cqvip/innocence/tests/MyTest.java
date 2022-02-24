package com.cqvip.innocence.tests;

import com.alibaba.fastjson.JSON;
import com.cqvip.innocence.project.model.entity.ArmThematicDatabase;
import com.cqvip.innocence.project.model.entity.ArmUserInfo;
import com.cqvip.innocence.project.model.enums.SearchField;
import com.cqvip.innocence.project.model.enums.SearchSeparator;
import com.cqvip.innocence.project.model.enums.SearchType;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmThematicDatabaseService;
import com.cqvip.innocence.project.service.ArmUserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author eternal
 * @Date 2021/9/1
 * @Version 1.0
 */
@SpringBootTest
public class MyTest {
 @Autowired
 private ArmUserInfoService userService;

 @Autowired
 private ArmThematicDatabaseService thematicDatabaseService;

 @Test
 public void insertUserData(){
  List userList = new ArrayList();
  for (int i=0;i<100;i++){
   ArmUserInfo user = new ArmUserInfo();
   user.setShowWriter(VipEnums.WriterStatus.UNAUTHORIZED);
   user.setActiveStatus(SearchType.ActiveStatus.Activation);
   user.setAddress("重庆市");
   user.setCardId(UUID.randomUUID().toString());
   user.setDegree(VipEnums.Degree.COLLEGE);
   user.setLoginName("test"+i);
   user.setLoginPassword("1");
   user.setReaderName("张三"+i+"号");
   user.setJobTitle(SearchType.JobTitle.Student);
   user.setReviewStatus(SearchType.ReviewStatus.Review);
   user.setReaderSrc(SearchType.ReaderSrc.CampusUser);
   user.setCreateTime(new Date());
//   user.setVisitCount(0);
//   user.setIsAnalysis(false);
//   user.setFine(new BigDecimal(0));
//   user.setDiskSize(0);
//   user.setNetBagSize(0);
//   user.setDeleted(0);
   userList.add(user);
  }
  userService.saveBatch(userList);
 }

 @Test
 public void insertThematicData(){

  ArmThematicDatabase thematicDatabase = new ArmThematicDatabase();
  thematicDatabase.setName("新冠肺炎防治");
  thematicDatabase.setKeyword("新冠肺炎;病毒;疫情");
  thematicDatabase.setClcClassId(76L);
  thematicDatabase.setUserId(1432622423169060865L);
  thematicDatabase.setThematicTypeId(1437704704080035842L);
  thematicDatabase.setRemark("关于新冠肺炎防治的最新消息。");
  thematicDatabase.setExpression("");
  thematicDatabase.insert();
 }

 @Test
 public void test1(){
  ArmThematicDatabase thematicDatabase = thematicDatabaseService.getById(1437705712495579137L);
  List<Map> list = new ArrayList<>();
  Map first = new HashMap();
  first.put("separator", SearchSeparator.AND);
  first.put("key", SearchField.AUTHOR.name());
  first.put("value","张三");
  first.put("type","精确");
  Map third = new HashMap();
  third.put("separator", SearchSeparator.OR);
  third.put("key", SearchField.FIRST_AUTHOR.name());
  third.put("value","李四");
  third.put("type","精确");
  list.add(first);
  list.add(third);
  String exp = JSON.toJSONString(list);
  thematicDatabase.setExpression(exp);
  thematicDatabase.updateById();
 }

}