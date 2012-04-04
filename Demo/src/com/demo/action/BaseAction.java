/**
 * 
 */
package com.demo.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author ric.g21
 *
 */
public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware{

	private static final long serialVersionUID = 1L;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}
