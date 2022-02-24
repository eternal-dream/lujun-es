package com.cqvip.innocence.project.controller.front;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.NoLogin;
import com.cqvip.innocence.common.util.file.FileUploadUtils;
import com.cqvip.innocence.common.util.yml.YmlReadUtil;
import com.cqvip.innocence.common.util.zip.ZipUtil;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAnnex;
import com.cqvip.innocence.project.model.entity.ArmNews;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmAnnexService;
import com.cqvip.innocence.project.service.ArmNewsService;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.cqvip.innocence.common.constant.ConfigConstant.YML_FILE_NAME;

/**
 * @Author eternal
 * @Date 2021/9/18
 * @Version 1.0
 */
@RestController
@RequestMapping("/${base-url.front}/news")
public class NewsController extends AbstractController {


    @Autowired
    private ArmNewsService newsService;

    @Autowired
    private ArmAnnexService annexService;

    /**
     * 新闻公告(栏目必须添加新闻公告栏目,设计不合理)
     *
     * @return
     */
    @GetMapping("getNotices")
    @NoLogin
    public JsonResult getNotices() {
        LambdaQueryWrapper<ArmNews> wrapper = new LambdaQueryWrapper<>();
        //此处1439134747339603969L是当前新闻公告栏目的id，注意跟随数据修改
        wrapper.eq(ArmNews::getColumnId, 1439134747339603969L);
        wrapper.orderByAsc(ArmNews::getNumericalOrder);
        Page page = newsService.page(getPageParams(), wrapper);
        return JsonResult.Get("noticePage", page);
    }

    /**
     * 获取栏目内容
     *
     * @param id 内容id
     * @return
     */
    @GetMapping("getNewsContent")
    public JsonResult getNewsContent(Long id) {
        ArmNews news = newsService.getById(id);
        news.setVisitCount(news.getVisitCount() + 1);
        news.updateById();
        LambdaQueryWrapper<ArmAnnex> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArmAnnex::getRealId, id);
        List<ArmAnnex> list = annexService.list(wrapper);
        news.setArmAnnexList(list);
        return JsonResult.Get("newsContent", news);
    }


    @GetMapping("/getNewsList")
    @ApiOperation("获取新闻公告分页")
    public JsonResult getNewsList() {
        Page page = getPageParams();
        Page<ArmNews> pageList = newsService.getNewsPageList(page);
        return JsonResult.Get().putPage(pageList);
    }

    @GetMapping("/getDetailById")
    @ApiOperation("根据id获取新闻公告详情")
    public JsonResult getDetailById(Long id){
        return JsonResult.Get().putRes(newsService.getById(id));
    }

    @GetMapping("/getNewsByClassId")
    @ApiOperation("通过classId获取news并分页")
    public JsonResult getNewsByClassId(Long classId,Long columnId,Page page,@RequestParam(defaultValue = "") String order){
        Page<ArmNews> newsList = null;
        if(columnId != null){
            ArmNews armNews = new ArmNews();
            if (classId!=null) {
                armNews.setClassId(classId);
            }
            armNews.setColumnId(columnId);
            armNews.setAuditStatus(VipEnums.AuditStatus.SHOW);
            newsList = newsService.getNewsByPage(armNews,null,null, page,order);
            return JsonResult.Get().put("data", newsList);
        }
        return JsonResult.Get(false).setMsg("参数错误！");
    }

    @GetMapping("/downloadAnnexById")
    @ApiOperation("通过id下载附件（压缩后下载）")
    public void downloadAnnexById(Long id, HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {

        newsService.downloadAnnexAndOtherToDo(id,request,response);

    }
}
