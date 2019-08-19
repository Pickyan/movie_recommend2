package com.yzy.movie.dao;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.yzy.movie.entity.User;

@Repository("userDao")
public interface UserDao {
	
	@Select("select count(*) from user where u_id=#{u_id} and u_passwd=#{u_passwd}")
	int login(User user);
}
