package com.cqvip.innocence.project.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqvip.innocence.common.sessions.SessionKeys;
import com.cqvip.innocence.common.util.file.FileUtil;
import com.cqvip.innocence.common.util.redis.RedisUtil;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.dto.VerifyCode;
import com.cqvip.innocence.project.model.entity.ArmAnnex;
import com.cqvip.innocence.project.model.entity.ArmClcClassInfo;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmAnnexService;
import com.cqvip.innocence.project.service.ArmClcClassInfoService;
import com.cqvip.innocence.project.service.IverifyCodeGen;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.InputFormatException;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.cqvip.innocence.common.constant.RedisCommonKeys.CLASS_INFO_KEY;
import static com.cqvip.innocence.common.util.file.FileUtil.ALLOWEXTS;
import static com.cqvip.innocence.common.util.file.FileUtil.DIRPATHBASE;


/**
 * @ClassName CommonController
 * @Description 一些本项目通用接口
 * @Author Innocence
 * @Date 2021/8/26 14:34
 * @Version 1.0
 */
@RestController
@RequestMapping("/common/")
@Api(tags = "一些前后台通用接口")
public class CommonController {

    @Value("${myFile.video-type}")
    private String videoType;

    @Autowired
    private ArmAnnexService annexService;

    @Autowired
    private IverifyCodeGen iverifyCodeGen;

    @Autowired
    private ArmClcClassInfoService classInfoservice;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    private ArmClcClassInfoService clcClassInfoService;

    @PostMapping("upload")
    @ApiOperation("统一文件上传接口(返回文件存入附件表后的主键id和文件存储的相对路径)")
    public JsonResult commonUpload( VipEnums.AnnexSrc annexSrc,  MultipartFile file){
        if (file.isEmpty()){
            return JsonResult.Get(false);
        }
        String filename = file.getOriginalFilename();
        String type = filename.substring(filename.lastIndexOf(".") + 1);
//        List<String> fileAllowExts = StrUtil.splitTrim(ALLOWEXTS,",");
//        if (!fileAllowExts.contains(type)){
//            return JsonResult.Get(false,"不允许的文件类型");
//        }
        JsonResult validate = fileUtil.validateFile(file);
        if(!validate.getSuccess()){
            return validate;
        }
        String path;
        if (annexSrc == null){
            path = fileUtil.getPath(DIRPATHBASE+ File.separator+"annex");
        }else {
            path = fileUtil.getPath(DIRPATHBASE+ File.separator+"annex"+File.separator+annexSrc.name());
        }
        String filePath = path + File.separator + UUID.randomUUID().toString().replace( "-" , "" )+"."+type;
        File newFile = new File(filePath);
        FileOutputStream outputStream = null;
        BufferedOutputStream out = null;
        try {
            outputStream = new FileOutputStream(newFile);
            out = new BufferedOutputStream(outputStream);
            out.write(file.getBytes());
            out.flush();
            ArmAnnex armAnnex = new ArmAnnex();
            armAnnex.setTitle(filename);
            armAnnex.setAnnexSrc(annexSrc);
            armAnnex.setFilePath(filePath.substring(filePath.indexOf("upload")-1));
            armAnnex.setFileSize((int)(file.getSize()/1024));
            List<String> list = StrUtil.splitTrim(videoType, ",");
            if (list.contains(type)){
                MultimediaObject multimediaObject = new MultimediaObject(newFile);
                MultimediaInfo info = multimediaObject.getInfo();
                String ceil =String.valueOf( Math.ceil((double) info.getDuration() / 1000));
                armAnnex.setTimeLength(ceil);
            }
            annexService.save(armAnnex);
            return JsonResult.Get().putRes(armAnnex.getId()).put("path",armAnnex.getFilePath());
        } catch (FileNotFoundException e) {
            return JsonResult.Get(false,"找不到文件位置");
        } catch (IOException e) {
            return JsonResult.Get(false,"文件IO流异常");
        } catch (InputFormatException e) {
            e.printStackTrace();
        } catch (EncoderException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @PostMapping("commonDelete")
    @ApiOperation("统一的文件删除接口")
    public JsonResult commonDelete(String filePath){
        boolean b = fileUtil.deleteFile(fileUtil.getPath("")+filePath);
        if (b){
            LambdaQueryWrapper<ArmAnnex> eq = new QueryWrapper<ArmAnnex>().lambda().eq(ArmAnnex::getFilePath, filePath);
            boolean remove = annexService.remove(eq);
            return JsonResult.Get(remove);
        }
        return JsonResult.Get(b,"文件不存在或删除失败！");
    }

    @GetMapping("verifyCode")
    @ApiOperation(value = "验证码")
    public JsonResult verifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置长宽
        VerifyCode verifyCode = iverifyCodeGen.generate(100, 35);
        String code = verifyCode.getCode();
        //将VerifyCode绑定session
        HttpSession session = request.getSession();
        session.setAttribute(SessionKeys.VcodeKeys.CODE, code);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date startTime=null;
        try {
            startTime = format.parse(format.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        session.setAttribute(SessionKeys.VcodeKeys.CREAT_TIME,startTime);
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(verifyCode.getImgBytes());
        JsonResult get = JsonResult.Get();
        get.put("img",encode);
        get.put("sessionId",session.getId());
        return get;

    }

    @GetMapping("getClassInfoTreeData")
    @ApiOperation("获取中图分类的树形结构")
    public JsonResult getClassInfoTreeData(){
        List<ArmClcClassInfo> list = (List<ArmClcClassInfo>)redisUtil.get(CLASS_INFO_KEY);
        if (list == null || list.isEmpty()){
            list = classInfoservice.getTreeData();
            redisUtil.set(CLASS_INFO_KEY,list);
        }
        return JsonResult.Get().putList(list);
    }

    @PostMapping("downloadByPath")
    @ApiOperation("根据文件路径下载文件")
    public void downloadByPath(HttpServletRequest request, HttpServletResponse response, String path){
        String name = path.substring(path.lastIndexOf(File.separator)+1,path.lastIndexOf("."));
        path = fileUtil.getPath("") + File.separator+path;
        try {
            fileUtil.download(request,response,path,name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("getAllClcClass")
    public JsonResult getAllClcClass(){
        List<ArmClcClassInfo> treeData = clcClassInfoService.getTreeData();
        return JsonResult.Get("allClcClass",treeData);
    }
}
