package com.yzy.movie.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.yzy.movie.dao.MovieDao;
import com.yzy.movie.entity.Movie;
import com.yzy.movie.entity.RecommendMovie;
import com.yzy.movie.service.MovieService;
@Service("movieService")
public class MovieServiceImpl implements MovieService {

	@Autowired
	@Qualifier("movieDao")
	private MovieDao movieDao;
	
	@Override
	public List<Movie> findMovieByName(String name) {
		List<Movie> list = movieDao.findMovieByName(name);
		return list;
	}

	@Override
	public Movie findMovieById(String id) {
		return movieDao.findMovieById(id);
	}

	@Override
	public RecommendMovie findById(String id) {
		return movieDao.findById(id);
	}
}
