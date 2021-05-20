package com.yxkong.common.holder;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RequestHolder {
	/**
	 * 获取 HttpServletRequest
	 * 
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * HttpServletResponse
	 * 
	 * @return HttpServletResponse
	 */

	public static HttpServletResponse getResponse() {
		return ((ServletWebRequest) RequestContextHolder.getRequestAttributes()).getResponse();
	}

	/**
	 * HttpSession
	 * 
	 * @return HttpSession
	 */
	public static HttpSession getSession() {

		return getRequest().getSession();
	}

	/**
	 * 获取header 值
	 * @param name
	 * @return
	 */
	public static String getHeaderVal(String name) {

		return getRequest().getHeader(name);
	}

}
