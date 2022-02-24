package com.cqvip.innocence.common.exception;



/**
 * 反射工具类异常
 * @author 卢有佳(finemi)
 * @date 2015年12月15日 上午2:02:01
 * @version 1.0
 */
public class ReflectionException extends RuntimeException {
	private static final long serialVersionUID = -1175814234390404351L;
	
	public ReflectionException(){
	}
	public ReflectionException(String paramString) {
		super(paramString);
	}
	public ReflectionException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}
	public ReflectionException(Throwable paramThrowable) {
		super(paramThrowable);
	}
	protected ReflectionException(String paramString, Throwable paramThrowable, boolean paramBoolean1,
                                  boolean paramBoolean2) {
		super(paramString, paramThrowable, paramBoolean1, paramBoolean2);
	}
}
