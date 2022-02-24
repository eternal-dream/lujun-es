package com.cqvip.innocence.project.model.enums;

import com.cqvip.innocence.common.annotation.EnumAlias;

/**
 * @author Administrator
 */
public class VipEnums {

    public enum LoginStatus {
        @EnumAlias("禁用")
        DISABLE,
        @EnumAlias("启用")
        ENABLE
    }

    public enum Status {

        @EnumAlias("启用")
        ENABLE,

        @EnumAlias("禁用")
        DISABLE
    }


    /**
     * 权限类型
     *
     * @date 2021/8/19 14:52
     */
    public enum PowerType {

        @EnumAlias("页面")
        PAGE,

        @EnumAlias("按钮")
        BUTTON;
    }

    public enum WriterStatus {
        @EnumAlias("未认证")
        UNAUTHORIZED,
        @EnumAlias("已申请")
        APPLIED,
        @EnumAlias("已拒绝")
        REFUSED,
        @EnumAlias("已认证")
        AUTHENTICATED;
    }


    /**
     * 收藏来源
     */
    public enum FavouriteSrc {
        @EnumAlias("站内发布")
        WEBSITE,
        @EnumAlias("文献仓库")
        ARTICLESTORE,
        @EnumAlias("共享文献")
        WRITERARTICLE,
    }

    public enum ColumnType {
        @EnumAlias("列表展示")
        LIST,
        @EnumAlias("详情展示")
        DETAIL,
    }


    public enum AuditStatus {
        @EnumAlias("不限")
        ALL,

        @EnumAlias("显示")
        SHOW,

        @EnumAlias("屏蔽")
        SHIELD,

        @EnumAlias("未审核")
        UNAUDITED,

        @EnumAlias("驳回")
        REFUSE
    }

    public enum ArticleState {

        @EnumAlias("显示")
        SHOW,

        @EnumAlias("屏蔽")
        SHIELD,
    }


    public enum OrganType {
        @EnumAlias("试用")
        TRIAL,
        @EnumAlias("授权")
        NORMAL
    }

    public enum SMSType {
        @EnumAlias("验证码")
        VERIFICATION_CODE,
        @EnumAlias("找回密码")
        RETRIEVE_PASSWORD,
        @EnumAlias("登录")
        LOGIN
    }

    public enum DBType {
        SQLSERVER,
        MYSQL,
        SQLITE,
        ORACLE,
        ODBC,
        OLEDB,
        FIREBIRD,
        POSTGRESQL,
        DB2,
        INFORMIX,
        SQLSERVERCE
    }


    /**
     * 任务状态枚举
     */
    public enum TaskState {
        @EnumAlias("待处理")
        PENDING,
        @EnumAlias("进行中")
        ONGOING,
        @EnumAlias("已完成")
        FINISH,
        @EnumAlias("已中止")
        ABORT
    }


    /**
     * 数据库来源
     */
    public enum DBSrcType {
        DB,
        COLLECT
    }

    public enum MenuType {

        @EnumAlias("管理菜单")
        SYSTEM,

        @EnumAlias("导航菜单")
        WEB,

        @EnumAlias("快捷菜单")
        FAST
    }

    /**
     * 文献类型
     */
    public enum ArticleType {
        @EnumAlias("电子图书{esTag:1}")
        BOOK,

        @EnumAlias("期刊文献{esTag:3}")
        ARTICLE,

        @EnumAlias("学位论文{esTag:4}")
        THESIS,

        @EnumAlias("标准{esTag:5}")
        STANDARD,

        @EnumAlias("会议论文{esTag:6}")
        MEETING,

        @EnumAlias("视频{esTag:10}")
        VEDIO,

        @EnumAlias("纸本图书{esTag:14}")
        PAPERBOOK,

        @EnumAlias("图书{esTag:15}")
        ALLBOOK,

        @EnumAlias("综合文献{esTag:16}")
        NEWS,

        @EnumAlias("科技报告{esTag:12}")
        REPORT
    }

    /**
     * 数据类型
     */
    public enum ArticleDataType {
        @EnumAlias("不限")
        ALL,

        @EnumAlias("重复")
        REPEAT,

        @EnumAlias("唯一")
        ONLY
    }


    /**
     * 短消息类型
     */
    public enum MessageType {

        @EnumAlias("系统消息")
        SYSTEM,


        @EnumAlias("用户消息")
        USER
    }

    /**
     * 短消息发送类型
     */
    public enum MessageSendType {

        @EnumAlias("单条发送")
        SINGLESEND,

        @EnumAlias("群发")
        GROUPSEND
    }

    /**
     * 短消息接收类型
     */
    public enum MessageReceiveType {

        @EnumAlias("用户接收")
        USERRECEIVE,

        @EnumAlias("机构接收")
        ORGANRECEIVE,

        @EnumAlias("所有用户")
        ALLRECEVIE
    }


    public enum Degree {

        @EnumAlias("无")
        NONE,

        @EnumAlias("大专")
        COLLEGE,

        @EnumAlias("本科")
        UNDERGRADUATE,

        @EnumAlias("硕士")
        MASTER,

        @EnumAlias("博士")
        DOCTOR
    }

//    /**
//     * 共享资源公开类型
//     */
//    public enum OpenType {
//
//        @EnumAlias("不分享到专家图书馆")
//        NO,
//
//        @EnumAlias("分享到专家图书馆")
//        YES
//    }


    public enum DiskType {

        @EnumAlias("云书包")
        NETBAG,

        @EnumAlias("个人网盘")
        USERDISK,

        @EnumAlias("共享文献")
        SHAREARTICLE

    }

    /**
     * 附件来源
     */
    public enum AnnexSrc {

        @EnumAlias("云书包")
        NETBAG,

        @EnumAlias("个人网盘")
        USERDISK,

        @EnumAlias("站内新闻发布")
        WEBPUBLISH,

        @EnumAlias("共享文献")
        SHAREARTICLE,

        @EnumAlias("新刊文献")
        JOURNALARTICLE,

        @EnumAlias("其他来源")
        OTHER

    }

    /**
     * 语言类型
     */
    public enum LanguageType {
        @EnumAlias("未选择")
        UNSELECTED,
        @EnumAlias("中文期刊")
        CHINESE_PERIODICALS,
        @EnumAlias("外文期刊")
        ENGLISH_JOURNALS,
        @EnumAlias("机构指标")
        INSTITUTIONAL_INDICATORS
    }


    /**
     * 领域分类
     */
    public enum DomainType {
        @EnumAlias("中图分类")
        ZTFL,
        @EnumAlias("教育部学科")
        JYBXK,
        //@EnumAlias("CAS")
        //中科院 = 3,
        //@EnumAlias("CSCD")
        //CSCD = 4,
        //@EnumAlias("CSSCI")
        //CSSCI = 5,
        //@EnumAlias("SCIE")
        //SCIE = 6,

        //@EnumAlias("JCR")
        //JCR = 7,

        //@EnumAlias("SCOPUS")
        //SCOPUS = 8,

        //@EnumAlias("EI")
        //EI = 9,
        //@EnumAlias("ESI")
        //ESI = 10,
        //@EnumAlias("A&HCI")
        //AHCI = 11,
        //@EnumAlias("MEDLINE")
        //MEDLINE = 12,
        //@EnumAlias("SSCI")
        //SSCI = 13


    }

    /**
     * 是否枚举
     */
    public enum IsType {
        @EnumAlias("是")
        Y,
        @EnumAlias("否")
        N
    }


    public enum SearchSort {
        @EnumAlias("相关度")
        SCORE,
        @EnumAlias("时效性")
        PUB_YEAR,
    }


    public enum Order {
        @EnumAlias("升序")
        ASC,
        @EnumAlias("降序")
        DESC
    }


    public enum ShieldType {
        @EnumAlias("文献屏蔽")
        TITLE,

        @EnumAlias("期刊屏蔽")
        JOURNAL
    }


    /**
     * 用户操作类型（积分日志）
     */
    public enum ScoreType {
        @EnumAlias("上传共享文献")
        UPLOADING_SHARE_FILE,
        @EnumAlias("下载共享文献")
        DOWNLOADING_SHARE_FILE,
        @EnumAlias("文献被下载")
        DOWNLOADED,
        @EnumAlias("登录")
        LOGIN,
        @EnumAlias("收藏")
        COLLECTED,
        @EnumAlias("下载图书馆资源")
        DOWNLOADING_BOOK_RESOURCES,
        @EnumAlias("借书")
        BORROW_BOOKS,
        @EnumAlias("借书超期")
        BORROW_BOOKS_OVERDUE
    }


    /**
     * 管理员操作类型
     */
    public enum OperateType {
        @EnumAlias("新增或编辑")
        SAVE_OR_UPDATE,
        @EnumAlias("新增")
        ADD,
        @EnumAlias("编辑")
        EDITE,
        @EnumAlias("删除")
        DELETE,
        @EnumAlias("通过")
        PASS,
        @EnumAlias("驳回")
        REJECT,
        @EnumAlias("屏蔽")
        SHIELD,
        @EnumAlias("待审核")
        UNAUDITED,
        @EnumAlias("还原")
        RESTORE,
        @EnumAlias("查询")
        SEARCH,
    }

    /**
     * 管理员日志类型枚举
     */
    public enum AdminUserLogType {
        @EnumAlias("新增")
        ADD,
        @EnumAlias("编辑")
        EDITE,
        @EnumAlias("禁用")
        DELETE,
        @EnumAlias("还原")
        RESTORE,
    }

    /**
     * 系统回收站数据类型
     */
    public enum SysRecycleType {
        @EnumAlias("管理员")
        ADMIN,
        @EnumAlias("新闻")
        NEWS,
    }

    public enum UploadType {
        FILE,
        IMAGE
    }


    public enum CookieType {
        @EnumAlias("前台")
        FRONT,
        @EnumAlias("后台")
        BACK
    }


    /**
     * 检索字段枚举
     */
    public enum FiledName {
        @EnumAlias("标题")
        T,
        @EnumAlias("关键词")
        K,
        //@EnumAlias("标题或关键词", ShortName = "keywords")
        //M = 1,
        @EnumAlias("任意字段")
        U,
        @EnumAlias("作者")
        A,
        @EnumAlias("第一作者")
        F,
        @EnumAlias("刊名")
        J,
        @EnumAlias("出版社")
        P,
        @EnumAlias("机构")
        S,
        @EnumAlias("分类号")
        C,
        @EnumAlias("文摘")
        R

    }

    /**
     * 聚类字段
     */
    public enum ClusterFiledName {
        @EnumAlias("默认")
        NO,
        @EnumAlias("文献类型")
        SOURCE_TYPE,
        @EnumAlias("发布年")
        PUB_YEAR,
        @EnumAlias("关键词")
        KEYWORD,
        @EnumAlias("作者")
        AUTHOR,
        @EnumAlias("机构")
        ORGAN,
        @EnumAlias("分类号")
        CLC_NO,
        @EnumAlias("期刊")
        JOURNAL_NAME,
        @EnumAlias("数据库")
        PROVIDER,
        @EnumAlias("分类号首字母")
        FIRSTCLASS,
    }


    /**
     * 检索连接符
     */
    public enum LogicCode {
        @EnumAlias("与")
        AND,
        @EnumAlias("或")
        OR,
        @EnumAlias("非")
        NOT,
    }

    public enum LogDateType {
        @EnumAlias("最近7天")
        WEEK,
        @EnumAlias("最近1月")
        MONTH,
        @EnumAlias("最近1年")
        YEAR,
        @EnumAlias("自定义")
        CUSTOMIZE
    }

    /**
     * 专题状态枚举
     */
    public enum ThemeStatus {
        @EnumAlias("置顶")
        TOP,
        @EnumAlias("正常")
        NORMAL,
        @EnumAlias("下架")
        SOLD_OUT,
    }

    public enum ShareArticleStatus {
        @EnumAlias("待审核")
        CHECK_PENDING,
        @EnumAlias("正常")
        NORMAL,
        @EnumAlias("屏蔽")
        SHIELD,
    }

    /**
     * 问题类型
     */
    public enum QuestionTypeEnum {
        @EnumAlias("常见问题")
        QUESTION,
        @EnumAlias("用户提交问题")
        USERQUESTION,
    }

    public enum ResourceUseType {
        @EnumAlias("全部")
        ALL,
        @EnumAlias("浏览")
        VISIT,
        @EnumAlias("下载")
        DOWN,
        @EnumAlias("检索")
        SEARCH,
        @EnumAlias("收藏")
        FAVOURITE
    }

    /**
     * 研究方向类型
     */
    public enum ThemeType {
        @EnumAlias("官方热点")
        SYSTEMHOT,
        @EnumAlias("用户发布")
        USERPUBLISH
    }

    public enum CustomizeType {
        @EnumAlias("图书推荐")
        TSTJ,
        @EnumAlias("新刊推荐")
        XLTJ,
        @EnumAlias("更新通知")
        UPDATE,
        @EnumAlias("新闻公告")
        NEWS,
        @EnumAlias("资源推送")
        PUSH
    }

    public enum ResourceType {
        @EnumAlias("按类型")
        TYPE,
        @EnumAlias("按内容")
        CONTENT,
        @EnumAlias("按系统")
        SYSTEM
    }


}
