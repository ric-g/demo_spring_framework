/**
 * 
 */
package com.demo.dao;

import java.util.List;

import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;

import com.demo.pojo.User;

/**
 * @author ric.g21
 *
 */
@Component("userDao")
public class UserDao extends HibernateDaoSupport implements IUserDao{
	private static final Logger log = LoggerFactory.getLogger(UserDao.class);

	/* (non-Javadoc)
	 * @see com.demo.dao.IUserDao#findById(int)
	 */
	public User findById(int id) {
		log.debug("getting User instance with id: " + id);
		try {
			User instance = this.getHibernateTemplate().get(User.class, id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (DataAccessException e) {
			log.error("get failed", e);
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.demo.dao.IUserDao#saveOrUpdate(com.demo.pojo.User)
	 */
	public void saveOrUpdate(User object){
		Transaction t = getSessionFactory().getCurrentSession().beginTransaction();
		try{
			t.begin();
			getHibernateTemplate().saveOrUpdate(object);
			t.commit();
		}catch(DataAccessException e){
			t.rollback();
			log.error("save failed", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.demo.dao.IUserDao#all()
	 */
	public List<User> all(){
		log.debug("getting all users: ");
		DetachedCriteria detachedCritera = DetachedCriteria.forClass(User.class);
		return this.getHibernateTemplate().findByCriteria(detachedCritera);
	}
}
