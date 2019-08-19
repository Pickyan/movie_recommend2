package com.yzy.movie.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.yzy.movie.dao.UserDao;
import com.yzy.movie.entity.User;
import com.yzy.movie.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;

	@Override
	public int login(User user) {
		return userDao.login(user);
	}
	
}
