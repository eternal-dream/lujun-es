package com.cqvip.innocence.project.model.enums;

import com.alibaba.druid.wall.Violation;
import com.cqvip.innocence.common.annotation.EnumAlias;
import net.sf.jsqlparser.statement.select.Wait;
import sun.rmi.server.Activation;

/**
 * @Author eternal
 * @Date 2021/8/20
 * @Version 1.0
 */
public class SearchType
{
 /// 借书证状态
 public enum CardStatus
 {
  @EnumAlias("正常")
  Normal,
  @EnumAlias("停用")
  Disable ,
  @EnumAlias("违规")
  Violation ,
  @EnumAlias("挂失")
  ReportLoss,
  @EnumAlias("注销")
  Cancel,
  @EnumAlias("验证")
  Check,
  @EnumAlias("无证")
  NoCard

 }
 /// 激活状态
 public enum ActiveStatus
 {
  @EnumAlias("激活")
  Activation,
  @EnumAlias("未激活")
  Inactivated
 }
 /// 账号状态
 public enum ReviewStatus
 {
  @EnumAlias("已通过")
  Review,
  @EnumAlias("未审核")
  Unreviewed,
  @EnumAlias("已拒绝")
  Refuse,
  @EnumAlias("待审核")
  Wait,
 }

 /// 读者来源
 public enum ReaderSrc
 {
  @EnumAlias("院内用户")
  CampusUser ,
  @EnumAlias("离院用户")
  LeaveCampusUser,
  @EnumAlias("院外用户")
  OffCampusUser,
 }




 /// 职称
 public enum JobTitle
 {
  @EnumAlias("学员")
  Student,
  @EnumAlias("教职工")
  Faculty ,
  @EnumAlias("其他")
  Other,
 }
}