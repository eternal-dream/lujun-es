package com.cqvip.innocence.common.util.yml;




import com.cqvip.innocence.common.util.BaseUtil;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName YmlReadUtil
 * @Description 读取yml配置
 * @Author Innocence
 * @Date 2020/9/22 14:09
 * @Version 1.0
 */
public class YmlReadUtil extends BaseUtil{
    public static YmlReadUtil newInstance() {
        return (YmlReadUtil) BaseUtil.instance(YmlReadUtil.class.getName());
    }
    /**
     * ${} 占位符 正则表达式
     * @author Innocence
     * @date 2020/9/22
     */
    private static Pattern p1 = Pattern.compile("\\$\\{.*?\\}");

//    public YmlReadUtil(){
//        throw new AssertionError();
//    }

    /**
     * key:文件索引名
     * value：配置文件内容
     */
    private static Map<String , LinkedHashMap> ymls = new HashMap<>();
    /**
     * String:当前线程需要查询的文件名
     */
    private static ThreadLocal<String> nowFileName = new InheritableThreadLocal<>();

    /**
     * 加载配置文件
     * @param fileName
     */
    private void loadYml(String fileName){
        nowFileName.set(fileName);
        if (!ymls.containsKey(fileName)){
            ymls.put(fileName , new Yaml().loadAs(YmlReadUtil.class.getResourceAsStream("/" + fileName),LinkedHashMap.class));
        }
    }

    /**
     * 读取yml文件中的某个value。
     * 支持解析 yml文件中的 ${} 占位符
     * @param key
     * @return Object
     */
    private Object getValue(String key){
        String[] keys = key.split("[.]");
        Map ymlInfo = (Map) ymls.get(nowFileName.get()).clone();
        for (int i = 0; i < keys.length; i++) {
            Object value = ymlInfo.get(keys[i]);
            if (i < keys.length - 1){
                ymlInfo = (Map) value;
            }else if (value == null){
                throw new RuntimeException("key不存在");
            }else {
                String g;
                String keyChild;
                String v1 = String.valueOf(value);
                for(Matcher m = p1.matcher(v1); m.find(); value = v1.replace(g, (String)getValue(keyChild))) {
                    g = m.group();
                    keyChild = g.replaceAll("\\$\\{", "").replaceAll("\\}", "");
                }
                return value;
            }
        }
        return "";
    }

    /**
     * 读取yml文件中的某个value
     * @param fileName  yml名称
     * @param key
     * @return Object
     */
    public Object getValue(String fileName, String key){
        loadYml(fileName);
        return getValue(key);
    }

    /**
     * 读取yml文件中的某个value，返回String
     * @param fileName
     * @param key
     * @return String
     */
    public String getValueToString(String fileName, String key){
        return String.valueOf(getValue(fileName, key));
    }

    /**
     * 读取yml文件中的某个value，返回Integer。不能转换为integer时将抛出类型转化异常
     * @author Innocence
     * @date 2020/9/22
     * @param fileName
     * @param key
     * @return java.lang.Integer
     */
    public Integer getValueToInteger(String fileName, String key){
        return Integer.valueOf(getValueToString(fileName,key));
    }

}
