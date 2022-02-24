package com.cqvip.innocence.common.util.file;

import cn.hutool.core.lang.UUID;
import com.cqvip.innocence.common.util.yml.YmlReadUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import static com.cqvip.innocence.common.constant.ConfigConstant.YML_FILE_NAME;

/**
 * 文件上传工具
 * 通过方法链的方式来完成上传方法的操作
 * 如: 末尾test()方法中所示
 *
 * @author 01
 * @date 2021-09-09 9:39
 */
public class FileUploadUtils {

    /**
     * 上传文件的基础位置
     *
     * @author Innocence
     * @date 2020/9/15
     */
    public static final String DIRPATHBASE = YmlReadUtil.newInstance().getValueToString(YML_FILE_NAME, "myFile.upload-url");


    /**
     * 获取当前系统路径
     */
    public static String getPath(String basePath) {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        if (!path.exists()) {
            path = new File("");
        }
        File upload = new File(path.getAbsolutePath(), "static" + File.separator + basePath);
        if (!upload.exists()) {
            upload.mkdirs();
        }
        return upload.getAbsolutePath();
    }

    /**
     * 重命名后的文件名
     *
     * @param renameValue 重命名的文件名
     * @return
     */
    public static FileUploadUtils.Builder setRenameValue(String renameValue) {
        return builder().setRenameValue(renameValue);
    }

    /**
     * 是否重命名
     *
     * @param rename 是否要重命名 true or false
     * @return
     */
    public static FileUploadUtils.Builder setRename(boolean rename) {
        return builder().setRename(rename);
    }

    /**
     * 设置允许上传文件大小
     *
     * @param maxSize
     * @return
     */
    public static FileUploadUtils.Builder setMaxSize(int maxSize) {
        return builder().setMaxSize(maxSize);
    }

    /**
     * 设置允许上传文件格式
     *
     * @param allowedExtension
     * @return
     */
    public static FileUploadUtils.Builder setAllowedExtension(String[] allowedExtension) {
        return builder().setAllowedExtension(allowedExtension);
    }

    public static FileUploadUtils.Builder builder() {
        return new Builder();
    }

    public static class Builder {
        /**
         * 默认大小 50M
         */
        private long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;
        /**
         * 允许上传文件的后缀名
         */
        private String[] DEFAULT_ALLOWED_EXTENSION = {};
        /*
         * 是否重命名
         */
        private Boolean RENAME = false;
        /**
         * 重命名的名字
         */
        private String RENAME_VALUE;
        /**
         * 上级文件夹名 如 news
         */
        private String SUPER_DIR;

        public FileUploadUtils.Builder setSuperDir(String superDir) {
            this.SUPER_DIR = superDir;
            return this;
        }

        public FileUploadUtils.Builder setRenameValue(String renameValue) {
            this.RENAME_VALUE = renameValue;
            return this;
        }


        public FileUploadUtils.Builder setRename(Boolean rename) {
            this.RENAME = rename;
            return this;
        }

        public FileUploadUtils.Builder setMaxSize(int maxSize) {
            this.DEFAULT_MAX_SIZE = maxSize * 1024 * 1024;
            return this;
        }


        public FileUploadUtils.Builder setAllowedExtension(String[] allowedExtension) {
            this.DEFAULT_ALLOWED_EXTENSION = allowedExtension;
            return this;
        }


        /**
         * 上传
         *
         * @param file
         * @return
         * @throws IOException
         */
        public final String upload(MultipartFile file) throws IOException {
            //相对路径（不含文件）
            String relativePath = DIRPATHBASE + (StringUtils.isNotBlank(SUPER_DIR) ? File.separator + SUPER_DIR : "");
            //绝对路径（不含文件）
            String abstractPath = getPath(relativePath);

            if (StringUtils.isBlank(abstractPath)) {
                throw new IOException("文件路径异常");
            }

            String extensionFilename = FilenameUtils.getExtension(file.getOriginalFilename());
            if (!ArrayUtils.isEmpty(DEFAULT_ALLOWED_EXTENSION) && !ArrayUtils.contains(DEFAULT_ALLOWED_EXTENSION, extensionFilename)) {
                throw new IOException("只允许" + ArrayUtils.toString(DEFAULT_ALLOWED_EXTENSION) + "文件上传");
            }

            if (file.getSize() > DEFAULT_MAX_SIZE) {
                throw new IOException("文件过大");
            }

            //准备上传
            String fileName = extractFilename(file);
            File desc = getAbsoluteFile(
                    abstractPath + File.separator , fileName);
            file.transferTo(desc);
            return relativePath + File.separator + fileName;
        }

        /**
         * 编码文件名
         */
        private String extractFilename(MultipartFile file) {
            String filename = null;
            //是否重命名
            if (RENAME) {
                //若有重命名后的文件名则用该名字，没有则用uuid
                filename = StringUtils.isNotBlank(RENAME_VALUE) ? RENAME_VALUE : UUID.randomUUID().toString();
            } else {
                filename = file.getOriginalFilename();
            }
            String extension = getExtension(file);
            //filename = DateFormatUtils.format(new Date(), "yyyy-MM-dd") + File.separator + encodingFilename(filename) + "." + extension;
            filename = File.separator + encodingFilename(filename) + "." + extension;
            return filename;
        }

        /**
         * 编码文件名
         */
        private String encodingFilename(String filename) {
            filename = filename.replace("_", " ");
            filename = DigestUtils.md5Hex(filename + System.nanoTime() + RandomStringUtils.randomNumeric(6));
            return filename;
        }

        /**
         * 获取文件名的后缀
         *
         * @param file 表单文件
         * @return 后缀名
         */
        public final String getExtension(MultipartFile file) {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            return extension;
        }

        /**
         * 创建文件
         *
         * @param uploadDir 文件保存路径（不含文件夹） 例如 d:/upload
         * @param filename  保存的文件
         */
        private File getAbsoluteFile(String uploadDir, String filename) throws IOException {
            File desc = new File(uploadDir + File.separator + filename);
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
            if (!desc.exists()) {
                desc.createNewFile();
            }
            return desc;
        }
    }

    private static void test(MultipartFile multipartFile) throws IOException {
        /*
         * 0、setRename(Boolean rename): 是否要重命名
         * 1、setSuperDir(String str): 设置文件上级目录,适用于用文件夹分隔上传的文件
         * 2、setRenameValue(String str): 配合setRename(true)时,使用当需要自定义文件名时可以自己命名，若不调用则默认用uuid
         * 3、setAllowedExtension(String[] suffix):符合文件上传的格式要求
         * 4、setMaxSize(int size):文件最大size（MB）
         * 5、return 为相对路径
         */
        String fileName = FileUploadUtils
                //设置重命名
                .setRename(true)
                //设置上级目录
                .setSuperDir("news")
                //设置文件重命名后的名字
                .setRenameValue("xxxx")
                //设置文件需要的格式
                .setAllowedExtension(new String[]{".jpg", ".xls"})
                //设置文件上传最大size（MB）
                .setMaxSize(10)
                .upload(multipartFile);
    }
}
