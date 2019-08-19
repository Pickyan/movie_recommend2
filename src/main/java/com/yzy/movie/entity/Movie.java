package com.yzy.movie.entity;

public class Movie {

	private String id;
	private String name;
	private Integer rate; // 评分
	private String director; // 导演
	private String screenwriter; // 编剧
	private String actor; // 演员
	private String type; // 类型
	private String area; // 地区
	private String language; // 语言
	private Integer length; // 片长
	private String imgurl; // 图片地址
	private String star;

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", name=" + name + ", rate=" + rate + ", director=" + director + ", screenwriter="
				+ screenwriter + ", actor=" + actor + ", type=" + type + ", area=" + area + ", language=" + language
				+ ", length=" + length + ", imgurl=" + imgurl + ", star=" + star + "]";
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRate() {
		return rate;
	}

	public void setRate(Integer rate) {
		this.rate = rate;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getScreenwriter() {
		return screenwriter;
	}

	public void setScreenwriter(String screenwriter) {
		this.screenwriter = screenwriter;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}
}
