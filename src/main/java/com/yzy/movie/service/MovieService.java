package com.yzy.movie.service;

import java.util.List;

import com.yzy.movie.entity.Movie;
import com.yzy.movie.entity.RecommendMovie;

public interface MovieService {
	/**
	 * 根据名字模糊查询出所有电影
	 * @param name
	 * @return
	 */
	List<Movie> findMovieByName(String name);
	/**
	 * 根据Id查询
	 * @param id
	 * @return
	 */
	Movie findMovieById(String id);
	/**
	 * 查询指定id电影的推荐电影
	 * @param id
	 * @return
	 */
	RecommendMovie findById(String id);
}
