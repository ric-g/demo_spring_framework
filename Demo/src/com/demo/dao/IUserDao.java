package com.demo.dao;

import java.util.List;

import com.demo.pojo.User;

public interface IUserDao {

	User findById(int id);

	void saveOrUpdate(User object);

	List<User> all();

}