package com.cqvip.innocence.common.util.enums;


import com.cqvip.innocence.common.annotation.EnumAlias;
import com.cqvip.innocence.common.exception.ReflectionException;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;

/**
 * 枚举工具类
 * @author 卢有佳(finemi)
 * @date 2016年8月5日 下午1:08:45
 * @version 1.0
 */
@Slf4j
public class EnumUtil {

	/**
	 * 获取枚举别名，如果未使用@EnumAlias注解，则返回null
	 */
	public static String getAlias(Enum<?> e){
		if(e==null)return null;
		EnumAlias alias;
		try {
			alias = e.getClass().getField(e.name()).getAnnotation(EnumAlias.class);
		} catch (Exception ex) {
			throw new ReflectionException(ex);
		}
		if(alias==null){
			return null;
		}else{
			return alias.value();
		}
	}

	/**
	 * 根据名字获得枚举<br>
	 * 未找到返回null
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends Enum> T getByName(Class<T> clazz,String name){
		try{
			return getByNameCheck(clazz,name);
		}catch (Exception ex){
			log.error("转换到枚举出错",ex);
			return null;
		}
	}
	/**
	 * 根据名字获得枚举<br>
	 * 未找到抛出异常
	 */
	public static <T extends Enum> T getByNameCheck(Class<T> clazz,String name){
		return (T)Enum.valueOf((Class<Enum>)clazz, name);
	}

	/**
	 * 根据序号获得枚举<br>
	 * 未找到返回null
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Enum> T getByOrdinal(Class<T> clazz,int ordinal){
		try{
			return getByOrdinalCheck(clazz,ordinal);
		}catch (Exception ex){
			log.error("转换到枚举出错",ex);
			return null;
		}
	}
	/**
	 * 根据序号获得枚举<br>
	 * 未找到抛出异常
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Enum> T getByOrdinalCheck(Class<T> clazz,int ordinal){
		T[] enums = clazz.getEnumConstants();
		for (Enum e : enums) {
			if(e.ordinal()==ordinal){
				return (T)e;
			}
		}
		throw new ReflectionException("["+ordinal+"]无法转换到Enum["+clazz+"]");
	}
	/**
	 * 根据别名获得枚举<br>
	 * 未找到返回null
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Enum> T getByAlias(Class<T> clazz,String alias){
		try{
			return getByAliasCheck(clazz,alias);
		}catch (Exception ex){
			log.info("转换到枚举出错",ex);
			return null;
		}
	}
	/**
	 * 根据别名获得枚举<br>
	 * 未找到抛出异常
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T extends Enum> T getByAliasCheck(Class<T> clazz,String alias){
		T[] enums = clazz.getEnumConstants();
		for (Enum e : enums) {
			String a = getAlias(e);
			if(alias.equals(a)){
				return (T)e;
			}
		}
		throw new ReflectionException("["+alias+"]无法转换到Enum["+clazz+"]");
	}

	/**
	 * 将枚举转为Map对象，name作为key，别名alias作为value
	 */
	public static LinkedHashMap<String,String> getNameAliasMap(Class<? extends Enum> clazz){
		Enum[] enums = clazz.getEnumConstants();
		LinkedHashMap<String,String> map = new LinkedHashMap<>();
		for (Enum anEnum : enums) {
			String k = anEnum.name();
			String v = getAlias(anEnum);
			map.put(k,v);
		}
		return map;
	}
}
