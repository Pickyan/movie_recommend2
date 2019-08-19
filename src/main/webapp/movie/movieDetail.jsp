<%@page import="com.yzy.movie.entity.Movie"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta name="referrer" content="no-referrer">
<meta charset="UTF-8">
<title>电影详情页面</title>
<script src="http://echarts.baidu.com/build/dist/echarts-all.js"></script>
<style type="text/css">
.title {
	height: 50px;
	font-size: 30px;
	font-weight: bold;
	margin-top: 10px;
}

.content {
	height: 400px;
	font-size: 14px;
	border-top: 1px solid gray;
}

.image {
	float: left;
	width: 200px;
	height: 296px;
	margin: 20px 0px;
}

.introduction {
	float: left;
	width: 400px;
	height: 296px;
	margin-top: 20px;
	margin-left: 60px;
}

#echarts {
	float: left;
	width: 390px;
	height: 296px;
	margin-top: 20px;
	margin-left: 5px;
}

.recommend {
	font-size: 25px;
}

.re_movies {
	border-top: 1px solid gray;
}

.movie_li {
	float: left;
	list-style: none;
	width: 210px;
	height: 356px;
	margin-left: 10px;
	margin-top: 10px;
	border: 1px solid gray;
	font-size: 13px;
	text-align: center;
}

</style>
</head>
<body>
	<div class="title">${movie.name }</div>
	<div class="content">
		<div class="image">
			<img style="width: 200px; height: 296px;"
				onerror="javascript:this.src='${pageContext.servletContext.contextPath }/images/001.jpg'"
				src="${movie.imgurl }">
		</div>
		<div class="introduction">
			<p>
				<b>导演：</b>${movie.director }</p>
			<p>
				<b>编剧：</b>${movie.screenwriter }</p>
			<p>
				<b>主演：</b>${movie.actor }</p>
			<p>
				<b>类型：</b>${movie.type }</p>
			<p>
				<b>地区：</b>${movie.area }</p>
			<p>
				<b>语言：</b>${movie.language }</p>
			<p>
				<b>片长：</b>${movie.length }分钟</p>
		</div>
		<div id="echarts"></div>
	</div>
	<div class="recommend">
		喜欢这部电影的人也喜欢
		<div class="re_movies">
			<ul>
				<c:forEach items="${reMovies }" var="movie">
					<a
						href="${pageContext.servletContext.contextPath }/movie/movieDetail.action?id=${movie.id }">
						<li class="movie_li"><img
							style="width: 200px; height: 296px; margin: 5px;"
							onerror="javascript:this.src='${pageContext.servletContext.contextPath }/images/001.jpg'"
							src="${movie.imgurl }"> <span style="color: blue;">${movie.name }</span><br>
							<span style="color: black">评分：${movie.rate }</span></li>
					</a>
				</c:forEach>
			</ul>
		</div>
	</div>


	<script type="text/javascript">
		var m = "${movie.star}".split(",");
		// 基于准备好的dom，初始化echarts图表
		var myChart = echarts.init(document.getElementById('echarts'));
		var option = {
			tooltip : {
				show : true
			},
			legend : {
				data : [ '评分：${movie.rate}' ]
			},
			xAxis : [ {
				type : 'category',
				data : [ "1星", "2星", "3星", "4星", "5星" ]
			} ],
			yAxis : [ {
				type : 'value'
			} ],
			series : [ {
				"name" : "人数",
				"type" : "bar",
				"data" : [ m[0], m[1], m[2], m[3], m[4] ]
			} ]
		};
		// 为echarts对象加载数据 
		myChart.setOption(option);
	</script>
</body>
</html>