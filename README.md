## 项目简介
此程序是利用MapReduce等大数据相关知识，对原始数据进行处理，获得推荐数据，在用SSM框架创建Web项目进行页面展示的web小项目。
## 实现效果
- 用户登录
- 电影名模糊找寻电影，列表展示
- 电影详情
- 评分矩阵
- 推荐电影展示
## 数据相关处理
#### 原始电影数据样式
![原始电影数据样式](https://github.com/Pickyan/movie_recommend2/blob/master/src/main/webapp/images/aa.png?raw=true "原始电影数据样式")
#### 目标电影数据样式
![目标电影数据样式](https://github.com/Pickyan/movie_recommend2/blob/master/src/main/webapp/images/bb.png?raw=true "目标电影数据样式")
#### 实现
通过计算电影相似度方式计算用户的推荐向量，取前十进行推荐
##### [代码](https://github.com/Pickyan/movie_recommend2/tree/master/src/main/java/com/yzy/data/douban2 "代码")
## 页面效果
![登录界面](https://github.com/Pickyan/movie_recommend2/blob/master/src/main/webapp/images/login.png?raw=true "登录界面")
![电影查找页面](https://github.com/Pickyan/movie_recommend2/blob/master/src/main/webapp/images/find.png?raw=true "电影查找页面")
![详情页](https://github.com/Pickyan/movie_recommend2/blob/master/src/main/webapp/images/de.png?raw=true "详情页")
![推荐电影](https://github.com/Pickyan/movie_recommend2/blob/master/src/main/webapp/images/recommend.png?raw=true "推荐电影")
