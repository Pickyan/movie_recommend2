package com.yzy.movie.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yzy.movie.entity.Movie;
import com.yzy.movie.entity.RecommendMovie;
import com.yzy.movie.service.MovieService;


@Controller
@RequestMapping("movie")
public class MovieController {
	
	@Autowired
	@Qualifier("movieService")
	private MovieService movieService;
	
	@RequestMapping("/findMovieByName")
	public String findMovieByName(String movieName,HttpServletRequest request) {
		
		List<Movie> list = movieService.findMovieByName(movieName);
		
		request.setAttribute("movies", list);
		
		return "selectByName.jsp";
	}
	
	
	
	@RequestMapping("/movieDetail")
	public String movieDetail(String id,HttpServletRequest request) {
		
		Movie movie = movieService.findMovieById(id);
		RecommendMovie reMovie = movieService.findById(id); //推荐电影列表
		
		String reMoviesId = reMovie.getRecommend_movie_id();
		String[] reMoviesIds = reMoviesId.split(",");
		List<Movie> recommendMovies = new ArrayList<>();
		for (String s : reMoviesIds) {
			recommendMovies.add(movieService.findMovieById(s));
		}
		
		
		request.setAttribute("reMovies", recommendMovies);
		request.setAttribute("movie", movie);
		return "movieDetail.jsp";
	}
}
