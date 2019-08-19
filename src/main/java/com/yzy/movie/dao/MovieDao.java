package com.yzy.movie.dao;

import java.util.List;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.yzy.movie.entity.Movie;
import com.yzy.movie.entity.RecommendMovie;

@Repository("movieDao")
public interface MovieDao {

	@Select("select * from movie where m_name like '%${value}%'")
	@Results({
		@Result(id=true,column="m_id",property="id"),
		@Result(column="m_name",property="name"),
		@Result(column="rate",property="rate"),
		@Result(column="director",property="director"),
		@Result(column="screenwriter",property="screenwriter"),
		@Result(column="actor",property="actor"),
		@Result(column="type",property="type"),
		@Result(column="area",property="area"),
		@Result(column="language",property="language"),
		@Result(column="length",property="length"),
		@Result(column="imgurl",property="imgurl"),
		@Result(column="star",property="star")
	})
	List<Movie> findMovieByName(String name);
	
	@Select("select * from movie where m_id=#{id}")
	@Results({
		@Result(id=true,column="m_id",property="id"),
		@Result(column="m_name",property="name"),
		@Result(column="rate",property="rate"),
		@Result(column="director",property="director"),
		@Result(column="screenwriter",property="screenwriter"),
		@Result(column="actor",property="actor"),
		@Result(column="type",property="type"),
		@Result(column="area",property="area"),
		@Result(column="language",property="language"),
		@Result(column="length",property="length"),
		@Result(column="imgurl",property="imgurl"),
		@Result(column="star",property="star")
	})
	Movie findMovieById(String id);
	
	@Select("select * from recommend_movie where m_id=#{id}")
	RecommendMovie findById(String id);
}
