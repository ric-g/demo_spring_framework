/**
 * 
 */
package com.demo.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.dao.IUserDao;
import com.demo.pojo.User;

/**
 * @author ric.g21
 *
 */
@Service("userService")
public class UserService implements IUserService {
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	//@Autowired
	@Resource(name = "userDao")
	private IUserDao userDao;
	
	/* (non-Javadoc)
	 * @see com.demo.service.IService#findById(int)
	 */
	public User findById(int id) {
		log.debug("find user by id: " + id);
		return userDao.findById(id);
	}
	
	/* (non-Javadoc)
	 * @see com.demo.service.IService#all()
	 */
	public List<User> all(){
		log.debug("get all users");
		return userDao.all();
	}
}
