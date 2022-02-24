package com.cqvip.innocence.common.util.zip;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 多文件压缩下载
 * @author 01
 * @date 2021-10-26 16:59
 */
@Slf4j
public class ZipUtil {

    private static final int BUFFER_SIZE = 2 * 1024;

    /**
     * 压缩成ZIP 方法1
     *
     * @param srcDir           压缩文件夹路径
     * @param out              压缩文件输出流
     * @param keepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         <p>
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, OutputStream out, boolean keepDirStructure) throws RuntimeException {

        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), keepDirStructure);
            long end = System.currentTimeMillis();
            log.info("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 压缩成ZIP 方法2
     *
     * @param srcFiles 需要压缩的文件列表
     * @param out      压缩文件输出流
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[BUFFER_SIZE];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param keepDirStructure 是否保留原来的目录结构, true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean keepDirStructure)
            throws Exception {

        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (keepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + File.separator));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (keepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + File.separator + file.getName(), keepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), keepDirStructure);
                    }
                }
            }
        }
    }


    /**
     * 拷贝文件夹
     *
     * @param oldPath 原文件夹
     * @param newPath 指定文件夹
     */
    public static void copyDir(String oldPath, String newPath) throws IOException {
        File file = new File(oldPath);
        //文件名称列表
        String[] filePath = file.list();

        if (!(new File(newPath)).exists()) {
            (new File(newPath)).mkdir();
        }

        for (int i = 0; i < filePath.length; i++) {
            if ((new File(oldPath + File.separator + filePath[i])).isDirectory()) {
                copyDir(oldPath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }

            if (new File(oldPath + File.separator + filePath[i]).isFile()) {
                copyFile(oldPath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
            }

        }
    }

    /**
     * 拷贝文件到指定文件及
     * @param filePath 文件路径
     * @param dirPath 指定文件夹(绝对路径)
     */
    public static void copyFileToDir(String filePath,String dirPath) throws IOException {
        File file = new File(filePath);
        File newFile = new File(dirPath+File.separator+file.getName());
        FileInputStream in = new FileInputStream(file);
        FileOutputStream out = new FileOutputStream(newFile);
        byte[] buffer = new byte[2097152];

        while ((in.read(buffer)) != -1) {
            out.write(buffer);
        }
        in.close();
        out.close();
    }

    /**
     * 拷贝文件
     *
     * @param oldPath 资源文件
     * @param newPath 指定文件
     */
    public static void copyFile(String oldPath, String newPath) throws IOException {
        File oldFile = new File(oldPath);
        File file = new File(newPath);
        FileInputStream in = new FileInputStream(oldFile);
        FileOutputStream out = new FileOutputStream(file);


        byte[] buffer = new byte[2097152];

        while ((in.read(buffer)) != -1) {
            out.write(buffer);
        }
        in.close();
        out.close();
    }

    public static void main(String[] args) throws Exception {
        /** 测试压缩方法1 */
        //FileOutputStream fos1 = new FileOutputStream(new File("E:\\aaaDEel\\fileMail\\mytest01.zip"));
        //ZipUtil.toZip("D:\\upload", fos1, true);
        /** 测试压缩方法2 */
		List<File> fileList = new ArrayList<>();
		fileList.add(new File("E:\\pic\\photo1.jpg"));
		fileList.add(new File("E:\\pic\\photo2.jpg"));
		FileOutputStream fos2 = new FileOutputStream(new File("E:/test-multi-download.zip"));
		ZipUtil.toZip(fileList, fos2);
    }

}
