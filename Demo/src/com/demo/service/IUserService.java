package com.demo.service;

import java.util.List;

import com.demo.pojo.User;

public interface IUserService {

	User findById(int id);

	List<User> all();

}