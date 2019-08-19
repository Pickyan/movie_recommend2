package com.yzy.movie.service;

import com.yzy.movie.entity.User;

public interface UserService {

	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	int login(User user);
}
