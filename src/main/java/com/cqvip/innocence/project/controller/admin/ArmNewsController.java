package com.cqvip.innocence.project.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cqvip.innocence.common.annotation.XssExclusion;
import com.cqvip.innocence.common.util.file.FileUploadUtils;
import com.cqvip.innocence.common.util.html.IpUtils;
import com.cqvip.innocence.project.controller.AbstractController;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.*;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmAnnexService;
import com.cqvip.innocence.project.service.ArmNewsClassService;
import com.cqvip.innocence.project.service.ArmNewsLogService;
import com.cqvip.innocence.project.service.ArmNewsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 新闻表 前端控制器
 * </p>
 *
 * @author Innocence
 * @since 2021-08-19
 */
@RestController
@RequestMapping("/${base-url.manager}/armNews")
public class ArmNewsController extends AbstractController {
    @Autowired
    ArmNewsLogService armNewsLogService;

    @Autowired
    ArmNewsService armNewsService;

    @Autowired
    ArmAnnexService armAnnexService;

    @Autowired
    ArmNewsClassService armNewsClassService;

    /**
     * 获取 新闻 列表（分页）
     *
     * @param armNews
     * @param page
     * @return {@link JsonResult}
     * @throws
     * @author 01
     * @date 2021/8/20 16:22
     */
    @GetMapping("/getNewsByPage")
    @ApiOperation(" 获取 新闻 列表（分页）")
    public JsonResult getNewsByPage(ArmNews armNews, Date beginTime, Date endTime, Page page) {
        Page<ArmNews> newsList = null;
        try {
            newsList = armNewsService.getNewsByPage(armNews, beginTime, endTime, page,"createTime");
            newsList.getRecords().stream().peek(item->{
                QueryWrapper<ArmNewsClass> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("COLUMN_ID", item.getColumnId());
                List<ArmNewsClass> list = armNewsClassService.list(queryWrapper);
                item.setArmNewsClassList(list);
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            ex.printStackTrace();
            return JsonResult.Get(false);
        }
        return JsonResult.Get().putPage(newsList);
    }

    /**
     * 通过id 获取新闻信息
     *
     * @param id 新闻id
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 16:33
     */
    @GetMapping("/getNewsById/{id}")
    @ApiOperation("通过id查询News信息")
    public JsonResult getNewsById(@PathVariable Long id) {
        if (id == null) {
            return JsonResult.Get(false).setCode("500").setMsg("参数错误");
        }
        ArmNews armNews = armNewsService.getById(id);
        //获取新闻附件列表
        QueryWrapper<ArmAnnex> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("REAL_ID", armNews.getId());
        List<ArmAnnex> armAnnexList = armAnnexService.list(queryWrapper);
        armNews.setArmAnnexList(armAnnexList);
        return JsonResult.Get().put("data", armNews);
    }

    /**
     * 审核
     *
     * @param armNews
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/9/29 9:55
     */
    @PostMapping("/checkArmNews")
    @ApiOperation("审核News信息")
    public JsonResult checkArmNews(HttpServletRequest request,ArmNews armNews,@RequestParam(required = false) String reason){
        ArmAdminUser currentAdmin = (ArmAdminUser) SecurityUtils.getSubject().getPrincipal();
        ArmNewsLog newsLog = new ArmNewsLog();
        newsLog.setAdminUserName(currentAdmin.getRealName());
        newsLog.setAdminUserId(currentAdmin.getId());
        newsLog.setTitle("审核发布内容");
        switch (armNews.getAuditStatus()){
            case SHOW:
                newsLog.setOperateType(VipEnums.OperateType.PASS);
                break;
            case SHIELD:
                newsLog.setOperateType(VipEnums.OperateType.SHIELD);
                break;
            case REFUSE:
                newsLog.setOperateType(VipEnums.OperateType.REJECT);
                newsLog.setContent(reason);
                break;
            case UNAUDITED:
                newsLog.setOperateType(VipEnums.OperateType.UNAUDITED);
                break;
        }

        newsLog.setIpAddress(IpUtils.getIpAddr(request));
        newsLog.setObjId(armNews.getId());
        try{
            armNewsLogService.save(newsLog);
            armNewsService.saveOrUpdate(armNews);
        }catch(Exception ex){
            ex.printStackTrace();
            return JsonResult.Get(false).setMsg("审核时发生异常");
        }

        return JsonResult.Get();
    }


    /**
     * 添加或修改 新闻
     * <p>
     * 修改时的关于附件的情景：
     * case1：annexList为null，oldFile也为null =》清空之前的所有附件（除封面外）
     * case2：annexList不为null，oldFile为null =》保留annexList中的文件
     * case3：annexList为null，oldFile不为null =》对比oldFile和以前的数量，删掉差异的文件
     * case4: annexList不为null，oldFile不为null
     * 修改时关于封面的情景：
     * case1:cover不为null，保留上传的文件，若以前有旧的封面数据直接删除
     * case2:cover为null，oldCover也为null，则需要对比以前的cover，以前有则删除，
     * </p>
     * @param cover     新上传的封面file
     * @param annexList 新上传的附件file
     * @param oldFile   旧的附件（仅修改时）
     * @param oldCover  旧的封面（仅修改时）
     * @param armNews   新闻信息
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 14:00
     */
    @PostMapping("/saveOrUpdateNews")
    @ApiOperation("添加或修改“新闻”")
    @XssExclusion
    public JsonResult saveOrUpdateNews(HttpServletRequest request,
                                       ArmNews armNews,
                                       MultipartFile cover,
                                       MultipartFile[] annexList,
                                       @RequestParam(required = false) String[] oldFile,
                                       @RequestParam(required = false) String oldCover) {
        if (armNews == null) {
            return JsonResult.Get(false).setCode("500").setMsg("参数错误");
        }

        //修改
        if (armNews.getId() != null) {
            ArmNews news = armNewsService.getById(armNews.getId());
            news.setTitle(armNews.getTitle());
            news.setPublishContent(armNews.getPublishContent());
            news.setLinkAddress(armNews.getLinkAddress());
            news.setAuditStatus(armNews.getAuditStatus());
            news.setNumericalOrder(armNews.getNumericalOrder());
            news.setColumnId(armNews.getColumnId());
            news.setClassId(armNews.getClassId());
            Subject subject = SecurityUtils.getSubject();
            ArmAdminUser armAdminUser = (ArmAdminUser) subject.getPrincipal();
            news.setPublishAuthor(armAdminUser.getId());
            return updateAnnexAndData(request,news, cover, annexList,VipEnums.OperateType.ADD,oldFile,oldCover);
        } else {
            Subject subject = SecurityUtils.getSubject();
            ArmAdminUser armAdminUser = (ArmAdminUser) subject.getPrincipal();
            armNews.setPublishAuthor(armAdminUser.getId());
            return saveAnnexAndData(request,armNews, cover, annexList,VipEnums.OperateType.EDITE);
        }
    }

    @GetMapping("/getNewsClassByColumnId/{columnId}")
    @ApiOperation("栏目对应类型列表")
    public JsonResult getNewsClassByColumnId(@PathVariable Long columnId){
        QueryWrapper<ArmNewsClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("COLUMN_ID", columnId);
        List<ArmNewsClass> list = armNewsClassService.list(queryWrapper);
        return JsonResult.Get().put("data",list);
    }

    /**
     * 更新文件并修改数据
     *
     * @param request
     * @param armNews     新闻信息
     * @param file        封面
     * @param annexList   附件
     * @param operateType 操作类型
     * @param oldFile     旧附件
     * @param oldCover    旧封面
     * @return {@link boolean}
     * @author 01
     * @date 2021/9/28 10:14
     */
    private JsonResult updateAnnexAndData(HttpServletRequest request,
                                       ArmNews armNews,
                                       MultipartFile file,
                                       MultipartFile[] annexList,
                                       VipEnums.OperateType operateType,
                                       String[] oldFile,
                                       String oldCover) {
        LocalDate today = LocalDate.now();
        String dateDir = today.getYear() + "" + today.getMonthValue();
        //封面
        String coverFileName = null;
        //有上传封面
        if (file != null) {
            String imgUrl = armNews.getImgUrl();
            //上传新的封面
            try {
                coverFileName = FileUploadUtils.setRename(true).setSuperDir(VipEnums.AnnexSrc.WEBPUBLISH.name()+ File.separator+dateDir).upload(file);
            } catch (Exception ex) {
                ex.printStackTrace();
                JsonResult.Get(false).setMsg(ex.getMessage());
            }

            //以前的封面是有的,需要删除掉以前的
            if (StringUtils.isNotBlank(imgUrl)) {
                //删除掉旧的封面的附件信息
                UpdateWrapper updateQuery = new UpdateWrapper<ArmAnnex>();
                updateQuery.eq("REAL_ID", armNews.getId());
                updateQuery.eq("FILE_PATH", armNews.getImgUrl());
                armAnnexService.remove(updateQuery);
            }
            //保存附件信息
            ArmAnnex annex = new ArmAnnex();
            String fileName = file.getOriginalFilename();
            annex.setFileSize(new Long(file.getSize() / 1024).intValue());
            annex.setAnnexSrc(VipEnums.AnnexSrc.WEBPUBLISH);
            annex.setFilePath(coverFileName);
            annex.setTitle(fileName);
            annex.setRealId(armNews.getId());
            armAnnexService.save(annex);

            armNews.setImgUrl(coverFileName);

            //更新信息
            try {
                armNewsService.saveOrUpdate(armNews);
                //操作日志
                ArmAdminUser currentAdmin = (ArmAdminUser) SecurityUtils.getSubject().getPrincipal();
                ArmNewsLog newsLog = new ArmNewsLog();
                newsLog.setAdminUserName(currentAdmin.getRealName());
                newsLog.setAdminUserId(currentAdmin.getId());
                newsLog.setOperateType(operateType);
                newsLog.setTitle("修改发布内容");
                newsLog.setIpAddress(IpUtils.getIpAddr(request));
                newsLog.setObjId(armNews.getId());
                armNewsLogService.save(newsLog);
            } catch (Exception ex) {
                log.info("修改“新闻信息”操作失败，原因：{}", ex.getMessage());
                JsonResult.Get(false).setMsg(ex.getMessage());
            }
            //没有上传封面的情况
        } else {
            String imgUrl = armNews.getImgUrl();
            //以前的封面是有的
            if (StringUtils.isNotBlank(imgUrl)&&StringUtils.isBlank(oldCover)) {
                //删除掉旧的封面的附件信息
                UpdateWrapper<ArmAnnex> updateQuery = new UpdateWrapper<ArmAnnex>();
                updateQuery.eq("REAL_ID", armNews.getId());
                updateQuery.eq("FILE_PATH", armNews.getImgUrl());
                armAnnexService.remove(updateQuery);

                armNews.setImgUrl(null);
                //更新信息
                try {
                    armNewsService.saveOrUpdate(armNews);
                    //操作日志
                    ArmAdminUser currentAdmin = (ArmAdminUser) SecurityUtils.getSubject().getPrincipal();
                    ArmNewsLog newsLog = new ArmNewsLog();
                    newsLog.setAdminUserName(currentAdmin.getRealName());
                    newsLog.setAdminUserId(currentAdmin.getId());
                    newsLog.setOperateType(operateType);
                    newsLog.setTitle("修改发布内容");
                    newsLog.setIpAddress(IpUtils.getIpAddr(request));
                    newsLog.setObjId(armNews.getId());
                    armNewsLogService.save(newsLog);
                } catch (Exception ex) {
                    log.info("修改“新闻信息”操作失败，原因：{}", ex.getMessage());
                    JsonResult.Get(false).setMsg(ex.getMessage());
                }
            }
        }

        //有上传附件
        if (annexList.length > 0) {
            if (oldFile==null||oldFile.length==0) {
                //清空除cover外所有附件
                QueryWrapper<ArmAnnex> armAnnexQueryWrapper = new QueryWrapper<>();
                armAnnexQueryWrapper.eq("REAL_ID", armNews.getId());
                armAnnexQueryWrapper.ne("FILE_PATH", armNews.getImgUrl());
                armAnnexService.remove(armAnnexQueryWrapper);
                //上传新文件
                List<ArmAnnex> armAnnexList = new ArrayList<>();
                for (MultipartFile multipartFile : annexList) {
                    String filePath = null;
                    try {
                        filePath = FileUploadUtils.setRename(true).setSuperDir(VipEnums.AnnexSrc.WEBPUBLISH.name()+ File.separator+dateDir).upload(multipartFile);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JsonResult.Get(false).setMsg(ex.getMessage());
                    }
                    ArmAnnex armAnnex = new ArmAnnex();
                    String fileName = multipartFile.getOriginalFilename();
                    armAnnex.setFileSize(new Long(multipartFile.getSize() / 1024).intValue());
                    armAnnex.setAnnexSrc(VipEnums.AnnexSrc.WEBPUBLISH);
                    armAnnex.setFilePath(filePath);
                    armAnnex.setTitle(fileName);
                    armAnnex.setRealId(armNews.getId());
                    armAnnexList.add(armAnnex);
                }
                //保存到数据库
                armAnnexService.saveBatch(armAnnexList);
            } else {
                //和以前的文件对比，清除掉差异的文件
                QueryWrapper<ArmAnnex> armAnnexQueryWrapper = new QueryWrapper<>();
                armAnnexQueryWrapper = armAnnexQueryWrapper.eq("REAL_ID", armNews.getId());
                for (String temp : oldFile) {
                    armAnnexQueryWrapper =  armAnnexQueryWrapper.ne("FILE_PATH", temp);
                }
                //除开封面
                if(StringUtils.isNotBlank(armNews.getImgUrl())){
                    armAnnexQueryWrapper = armAnnexQueryWrapper.ne("FILE_PATH",armNews.getImgUrl());
                }
                armAnnexService.remove(armAnnexQueryWrapper);
                //上传新文件
                List<ArmAnnex> armAnnexList = new ArrayList<>();
                for (MultipartFile multipartFile : annexList) {
                    String filePath = null;
                    try {
                        filePath = FileUploadUtils.setRename(true).setSuperDir(VipEnums.AnnexSrc.WEBPUBLISH.name()+ File.separator+dateDir).upload(multipartFile);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JsonResult.Get(false).setMsg(ex.getMessage());
                    }
                    ArmAnnex armAnnex = new ArmAnnex();
                    String fileName = multipartFile.getOriginalFilename();
                    armAnnex.setFileSize(new Long(multipartFile.getSize() / 1024).intValue());
                    armAnnex.setAnnexSrc(VipEnums.AnnexSrc.WEBPUBLISH);
                    armAnnex.setFilePath(filePath);
                    armAnnex.setTitle(fileName);
                    armAnnex.setRealId(armNews.getId());
                    armAnnexList.add(armAnnex);
                }
                //保存到数据库
                armAnnexService.saveBatch(armAnnexList);
            }

            //更新信息
            try {
                armNewsService.saveOrUpdate(armNews);
                //操作日志
                ArmAdminUser currentAdmin = (ArmAdminUser) SecurityUtils.getSubject().getPrincipal();
                ArmNewsLog newsLog = new ArmNewsLog();
                newsLog.setAdminUserName(currentAdmin.getRealName());
                newsLog.setAdminUserId(currentAdmin.getId());
                newsLog.setOperateType(operateType);
                newsLog.setTitle("修改发布内容");
                newsLog.setIpAddress(IpUtils.getIpAddr(request));
                newsLog.setObjId(armNews.getId());
                armNewsLogService.save(newsLog);
            } catch (Exception ex) {
                log.info("修改“新闻信息”操作失败，原因：{}", ex.getMessage());
                JsonResult.Get(false).setMsg(ex.getMessage());
            }

            //没有上传附件
        } else {
            if (oldFile==null||oldFile.length==0) {
                //清空除cover外所有附件
                QueryWrapper<ArmAnnex> armAnnexQueryWrapper = new QueryWrapper<>();
                armAnnexQueryWrapper = armAnnexQueryWrapper.eq("REAL_ID", armNews.getId());
                if(StringUtils.isNotBlank(armNews.getImgUrl())){
                    armAnnexQueryWrapper = armAnnexQueryWrapper.ne("FILE_PATH", armNews.getImgUrl());
                }

                armAnnexService.remove(armAnnexQueryWrapper);
            } else {
                //和以前的文件对比，清除掉差异的文件
                QueryWrapper<ArmAnnex> armAnnexQueryWrapper = new QueryWrapper<>();
                armAnnexQueryWrapper = armAnnexQueryWrapper.eq("REAL_ID", armNews.getId());
                for (String temp : oldFile) {
                    armAnnexQueryWrapper = armAnnexQueryWrapper.ne("FILE_PATH", temp);
                }
                //除开封面
                if(StringUtils.isNotBlank(armNews.getImgUrl())){
                    armAnnexQueryWrapper = armAnnexQueryWrapper.ne("FILE_PATH",armNews.getImgUrl());
                }
                armAnnexService.remove(armAnnexQueryWrapper);
            }

            //更新信息
            try {
                armNewsService.saveOrUpdate(armNews);
                //操作日志
                ArmAdminUser currentAdmin = (ArmAdminUser) SecurityUtils.getSubject().getPrincipal();
                ArmNewsLog newsLog = new ArmNewsLog();
                newsLog.setAdminUserName(currentAdmin.getRealName());
                newsLog.setAdminUserId(currentAdmin.getId());
                newsLog.setOperateType(operateType);
                newsLog.setTitle("修改发布内容");
                newsLog.setIpAddress(IpUtils.getIpAddr(request));
                newsLog.setObjId(armNews.getId());
                armNewsLogService.save(newsLog);
            } catch (Exception ex) {
                log.info("修改“新闻信息”操作失败，原因：{}", ex.getMessage());
                JsonResult.Get(false).setMsg(ex.getMessage());
            }
        }

        return JsonResult.Get();
    }

    /**
     * 保存文件和数据
     *
     * @param request
     * @param armNews
     * @param file
     * @param annexList
     * @param operateType
     * @return {@link boolean}
     * @author 01
     * @date 2021/9/28 13:36
     */
    private JsonResult saveAnnexAndData(HttpServletRequest request, ArmNews armNews, MultipartFile file, MultipartFile[] annexList, VipEnums.OperateType operateType) {
        LocalDate today = LocalDate.now();
        String dateDir = today.getYear() + "" + today.getMonthValue();
        //封面
        String coverFileName = null;
        if (file != null) {
            try {
                coverFileName = FileUploadUtils.setRename(true).setSuperDir(VipEnums.AnnexSrc.WEBPUBLISH.name()+ File.separator+dateDir).upload(file);
            } catch (Exception ex) {
                ex.printStackTrace();
                return JsonResult.Get(false).setMsg(ex.getMessage());
            }
            if (StringUtils.isNotBlank(coverFileName)) {
                armNews.setImgUrl(coverFileName);
            } else {
                log.info("附件上传失败");
                return JsonResult.Get(false).setMsg("附件上传失败");
            }
        }

        List<Map<String, Object>> annexFilePathList = new ArrayList<>();
        if (annexList != null && annexList.length > 0) {
            //附件

            for (MultipartFile multipartFile : annexList) {
                String fileName = null;
                try {
                    fileName = FileUploadUtils.setRename(true).setSuperDir(VipEnums.AnnexSrc.WEBPUBLISH.name()+ File.separator+dateDir).upload(multipartFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return JsonResult.Get(false).setMsg(ex.getMessage());
                }

                if (StringUtils.isNotBlank(fileName)) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("fileRelativePath", fileName);
                    map.put("file", multipartFile);
                    annexFilePathList.add(map);
                } else {
                    log.info("附件上传失败");
                    return JsonResult.Get(false).setMsg("附件上传失败");
                }
            }
        }

        List<ArmAnnex> armAnnexList = new ArrayList<>();

        try {
            armNewsService.saveOrUpdate(armNews);
            //操作日志
            ArmAdminUser currentAdmin = (ArmAdminUser) SecurityUtils.getSubject().getPrincipal();
            ArmNewsLog newsLog = new ArmNewsLog();
            newsLog.setAdminUserName(currentAdmin.getRealName());
            newsLog.setAdminUserId(currentAdmin.getId());
            newsLog.setOperateType(operateType);
            newsLog.setTitle("新增发布内容");
            newsLog.setIpAddress(IpUtils.getIpAddr(request));
            newsLog.setObjId(armNews.getId());
            armNewsLogService.save(newsLog);
        } catch (Exception ex) {
            log.info("新增“新闻信息”操作失败，原因：{}", ex.getMessage());
            return JsonResult.Get(false).setMsg(ex.getMessage());
        }
        try {
            //封面 附件表加信息
            if (file != null) {
                ArmAnnex armAnnex = new ArmAnnex();
                String filename = file.getOriginalFilename();
                armAnnex.setFileSize(new Long(file.getSize() / 1024).intValue());
                armAnnex.setAnnexSrc(VipEnums.AnnexSrc.WEBPUBLISH);
                armAnnex.setFilePath(coverFileName);
                armAnnex.setTitle(filename);
                armAnnex.setRealId(armNews.getId());
                armAnnexList.add(armAnnex);
            }
            if (annexList != null && annexList.length > 0) {
                //附件 附件表加信息
                for (Map<String, Object> stringObjectMap : annexFilePathList) {
                    String path = (String) stringObjectMap.get("fileRelativePath");
                    MultipartFile multipartFile = (MultipartFile) stringObjectMap.get("file");
                    ArmAnnex annex = new ArmAnnex();
                    String multipartFileName = multipartFile.getOriginalFilename();
                    annex.setFileSize(new Long(multipartFile.getSize() / 1024).intValue());
                    annex.setAnnexSrc(VipEnums.AnnexSrc.WEBPUBLISH);
                    annex.setFilePath(path);
                    annex.setTitle(multipartFileName);
                    annex.setRealId(armNews.getId());
                    armAnnexList.add(annex);
                }
            }
            if (armAnnexList.size() > 0) {
                armAnnexService.saveBatch(armAnnexList);
            }
        } catch (Exception ex) {
            log.info("相关附件保存失败：{}", ex.getMessage());
            log.info("newsId:{}", armNews.getId());
            return JsonResult.Get(false).setMsg(ex.getMessage());
        }
        return JsonResult.Get();
    }


    /**
     * 通过News.id逻辑删除
     * TODO 附件是否需要同时删除？或者统一删除
     * @param ids News.id
     * @return {@link JsonResult}
     * @author 01
     * @date 2021/8/20 16:01
     */
    @PostMapping("/deleteNewsByIds")
    @ApiOperation("通过News.id逻辑删除")
    public JsonResult deleteNewsByIds(@RequestBody Long[] ids,HttpServletRequest request) {
        if (ids == null) {
            return JsonResult.Get().setCode("500").setMsg("参数错误");
        }
        try {
            armNewsService.deleteByIdsWithTransaction(ids,request);
        } catch (Exception ex) {
            log.info("通过id删除News失败，原因：{}", ex.getMessage());
            return JsonResult.Get().setCode("500").setMsg("操作失败");
        }


        return JsonResult.Get().setMsg("操作成功");
    }


}

