package com.cqvip.innocence.common.util.file;

import cn.hutool.core.util.StrUtil;
import com.cqvip.innocence.common.util.BaseUtil;
import com.cqvip.innocence.common.util.yml.YmlReadUtil;
import com.cqvip.innocence.project.model.dto.JsonResult;
import com.cqvip.innocence.project.model.entity.ArmAnnex;
import com.cqvip.innocence.project.model.entity.ArmWebsiteConfig;
import com.cqvip.innocence.project.model.enums.VipEnums;
import com.cqvip.innocence.project.service.ArmWebsiteConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.cqvip.innocence.common.constant.ConfigConstant.YML_FILE_NAME;

/**
 * @ClassName FileUtil
 * @Description 文件操作工具类，EasyExcel依赖的poi版本为3.17
 * @Author Innocence
 * @Date 2020/9/11 9:32
 * @Version 1.0
 */
@Component
public class FileUtil extends BaseUtil {

    @Autowired
    private ArmWebsiteConfigService websiteConfigService;

    /**
     * 允许上传的文件类型
     * @author Innocence
     * @date 2020/9/15
     */
//    public static final String ALLOWEXTS = YmlReadUtil.newInstance().getValueToString(YML_FILE_NAME,"myFile.allow-exts");
    public static String ALLOWEXTS = "";

    /**
     * 文件上传的大小限制(MB)
     */
    public static Integer fileSizeLimit = 100;

    /**
     * 上传文件的基础位置
     * @author Innocence
     * @date 2020/9/15
     */
    public static final String DIRPATHBASE = YmlReadUtil.newInstance().getValueToString(YML_FILE_NAME,"myFile.upload-url");

    /**
     * 生成excel的基础路径
     * @author Innocence
     * @date 2020/9/15
     */
    private static final String EXCELURL = YmlReadUtil.newInstance().getValueToString(YML_FILE_NAME,"myFile.excel-url");

    private List<String> fileAllowExts;


    public static FileUtil newInstance(){
        return (FileUtil) BaseUtil.instance(FileUtil.class.getName());
    }

    /**
     * 上传文件并返回路径
     * @author Innocence
     * @date 2020/9/11
     * @param file
     * @param basePath 自定义的文件路径
     * @return java.lang.String
     */
    public JsonResult uploadUtil(MultipartFile file,String basePath){
        if (!file.isEmpty()){
            String filename = file.getOriginalFilename();
//            String type = filename.substring(filename.lastIndexOf(".") + 1);
//            if (!fileAllowExts.contains(type)){
//                return JsonResult.Get(false,"不允许的文件类型");
//            }
            JsonResult validate = validateFile(file);
            if(!validate.getSuccess()){
                return validate;
            }
            String path ;
            if (StrUtil.isNotBlank(basePath)){
                path = getPath(DIRPATHBASE+File.separator+basePath);
            }else {
                path = getPath(DIRPATHBASE);
            }
            String filePath = path + File.separator + filename;
            File newFile = new File(filePath);
            FileOutputStream outputStream = null;
            BufferedOutputStream out = null;
            try {
                outputStream = new FileOutputStream(newFile);
                out = new BufferedOutputStream(outputStream);
                out.write(file.getBytes());
                out.flush();
                return JsonResult.Get(true,filePath);
            } catch (FileNotFoundException e) {
                return JsonResult.Get(false,"找不到文件位置");
            } catch (IOException e) {
                return JsonResult.Get(false,"文件IO流异常");
            }finally {
                try {
                    out.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 下载
     * @author Innocence
     * @date 2020/9/11
     * @param request, response, path
     * @return com.cqvip.innocence.project.model.dto.JsonResult
     */
    public void download(HttpServletRequest request, HttpServletResponse response, String path,String fileName) throws Exception{
        response.setContentType("text/html;charset=UTF-8");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            request.setCharacterEncoding("utf-8");
//            String downLoadPath = request.getSession().getServletContext().getRealPath("/") + path;
            long fileLength = new File(path).length();
            String name = path.substring(path.lastIndexOf(File.separator)+1);
            String substring = name.substring(name.lastIndexOf(".") + 1);
            response.setContentType("application/zip;application/octet-stream");
            //    String s = new String(fileName.getBytes("UTF-8"), "ISO8859-1");

            //360下2种模式都正常，ie，谷歌和迅雷下文件都没有乱码，但是火狐下出现乱码
            String s  = URLEncoder.encode(fileName+"."+substring, "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + s);
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(path));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bis.close();
            bos.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除文件
     * @author Innocence
     * @date 2020/9/11
     * @param path
     * @return boolean
     */
    public boolean deleteFile(String path){
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }
    /**
     * 获取当前系统路径
     */
    public String getPath(String basePath) {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!path.exists()) {
            path = new File("");
        }
        File upload = new File(path.getAbsolutePath(), "static/"+basePath);
        if (!upload.exists()) {
            upload.mkdirs();
        }
        return upload.getAbsolutePath();
    }

    /**
     * 运行获取项目target路径 jar包运行获取jar包同级路径
     * @author Innocence
     * @date 2020/11/25
     * @return java.lang.String
     */
    public String getTargetPath(){
        ApplicationHome applicationHome = new ApplicationHome(getClass());
        File source = applicationHome.getSource();
        String targetPath = source.getParentFile().toString();
        return targetPath;
    }

    /**
     * byte数组转文件
     * @author Innocence
     * @date 2020/11/25
     * @param buffer
     * @param filePath
     * @return void
     */
    public void bytesToFile(byte[] buffer, String filePath){
        File file = new File(filePath);
        OutputStream output = null;
        BufferedOutputStream bufferedOutput = null;
        try {
            output = new FileOutputStream(file);
            bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(null!=bufferedOutput){
                try {
                    bufferedOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != output){
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存文件并返回annex表id
     * @param file
     * @param annexSrc
     * @return
     */
    public Long saveFile(MultipartFile file, VipEnums.AnnexSrc annexSrc,Long objId) throws Exception {
        String filename = file.getOriginalFilename();
        String type = filename.substring(filename.lastIndexOf(".") + 1);
//        List<String> fileAllowExts = StrUtil.splitTrim(ALLOWEXTS,",");
//        if (!fileAllowExts.contains(type)){
//            throw new Exception("不允许的文件类型!");
//        }
        JsonResult validate = validateFile(file);
        if(!validate.getSuccess()){
            throw new Exception(validate.get("msg").toString());
        }
        String path;
        LocalDate today = LocalDate.now();
        String dateDir = today.getYear() + "" + today.getMonthValue();
        if (annexSrc == null){
            path = getPath(DIRPATHBASE+ File.separator+"annex" + dateDir);
        }else {
            path = getPath(DIRPATHBASE+ File.separator+"annex"  + File.separator+annexSrc.name()+ File.separator + dateDir);
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
            armAnnex.setRealId(objId);
            armAnnex.insert();
            return armAnnex.getId();
        }finally {
            try {
                out.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JsonResult validateFile(MultipartFile file){
        ArmWebsiteConfig config = websiteConfigService.list().get(0);
        ALLOWEXTS = config.getFileExtension();
        fileAllowExts = StrUtil.splitTrim(ALLOWEXTS,",");
        fileSizeLimit = config.getFileSize();
        String filename = file.getOriginalFilename();
        String type = filename.substring(filename.lastIndexOf(".") + 1);
        List<String> fileAllowExts = StrUtil.splitTrim(ALLOWEXTS,";");
        if (!fileAllowExts.contains(type)){
            return JsonResult.Get(false,"不允许的文件类型");
        }else if(file.getSize()/1024/1024>fileSizeLimit){
            return JsonResult.Get(false,"文件大小最大为"+fileSizeLimit+"MB");
        }
        return JsonResult.Get();
    }
    
}
