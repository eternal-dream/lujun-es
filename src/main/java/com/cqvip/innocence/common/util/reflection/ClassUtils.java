package com.cqvip.innocence.common.util.reflection;

import cn.hutool.core.util.StrUtil;
import com.cqvip.innocence.common.util.BaseUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @ClassName ClassUtils
 * @Description TODO
 * @Author Innocence
 * @Date 2021/1/6 15:45
 * @Version 1.0
 */
public class ClassUtils {

    public static ClassUtils newInstance() {
        return (ClassUtils) BaseUtil.instance(ClassUtils.class.getName());
    }

    /**
     * 获取指定包下所有的类
     * @author Innocence
     * @date 2021/1/6
     * @param packageName
     * @return java.util.Set<java.lang.Class<?>>
     */
    public Set<Class<?>> getClasses(String packageName) throws Exception{
        // 第一个class类的集合
        Set<Class<?>> classes = new HashSet<>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    //
                    addClass(classes,filePath,packageName);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * 以文件的方式扫描整个包下的文件 并添加到集合中
     * @author Innocence
     * @date 2021/1/6
     * @param classes 类的集合
     * @param filePath 文件路径
     * @param packageName 包名
     * @return void
     */
    public void addClass(Set<Class<?>> classes, String filePath, String packageName) throws Exception{
        File[] files=new File(filePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile()&&file.getName().endsWith(".class"))||file.isDirectory();
            }
        });
        for(File file:files){
            String fileName=file.getName();
            if(file.isFile()){
                String className=fileName.substring(0,fileName.lastIndexOf("."));
                if(StrUtil.isNotBlank(packageName)){
                    className=packageName+"."+className;
                }
                doAddClass(classes,className);
            }

        }
    }

    /**
     * 根据名称加载类，添加到集合中
     * @author Innocence
     * @date 2021/1/6
     * @param classes 类的集合
     * @param className 名称
     * @return void
     */
    public void doAddClass(Set<Class<?>> classes, final String className) throws Exception{
        ClassLoader classLoader=new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return super.loadClass(name);
            }
        };
        classes.add(classLoader.loadClass(className));
    }
}
