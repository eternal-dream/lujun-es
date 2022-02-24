package com.cqvip.innocence.framework.filter;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 重写参数，转义特殊字符
 * @author Innocence
 * @date 2020/9/21
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private HttpServletRequest request;

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		this.request = request;
	}
	@Override
	public String getParameter(String name) {
		String value = request.getParameter(name);
		if (!StrUtil.isEmpty(value)) {
			value = StringEscapeUtils.escapeHtml4(value);
		}
		return value;
	}
	@Override
	public String[] getParameterValues(String name) {
		String[] parameterValues = super.getParameterValues(name);
		if (parameterValues == null) {
			return null;
		}
		for (int i = 0; i < parameterValues.length; i++) {
			String value = parameterValues[i];
			parameterValues[i] = StringEscapeUtils.escapeHtml4(value);
		}
		return parameterValues;
	}
}
