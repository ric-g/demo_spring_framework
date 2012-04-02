/**
 * 
 */
package com.demo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.demo.dao.IUserDao;
import com.demo.pojo.User;

/**
 * @author ric.g21
 *
 */
@Service("userService")
public class UserService implements IUserService {
	//@Autowired
	@Resource(name = "userDao")
	private IUserDao userDao;
	
	/* (non-Javadoc)
	 * @see com.demo.service.IService#findById(int)
	 */
	public User findById(int id) {
		return userDao.findById(id);
	}
	
	/* (non-Javadoc)
	 * @see com.demo.service.IService#all()
	 */
	public List<User> all(){
		return userDao.all();
	}
}
