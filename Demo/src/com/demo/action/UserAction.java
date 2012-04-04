/**
 * 
 */
package com.demo.action;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.service.IUserService;




/**
 * @author ric.g21
 *
 */
public class UserAction extends BaseAction{

	private static final long serialVersionUID = -8543692147989779875L;

	private static final Logger log = LoggerFactory.getLogger(UserAction.class);
    
	@Resource(name = "userService")
	private IUserService userService;
	public String getUser(){
		log.debug("getUser");
		userService.all();
		return SUCCESS;
	}
}
