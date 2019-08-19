<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>登陆界面</title>
<style type="text/css">
	.main{
		width: 300px;
		height: 350px;
		margin: auto;
		margin-top: 100px;
		background: #C1B767;
		border-radius: 10px;
		box-shadow: 5px 5px;
	}
</style>
</head>
<body bgcolor="#EDF4ED">
	<div class="main">
		<br>
		<span style="margin: 20px 10px;">Login</span>
		<form action="${pageContext.servletContext.contextPath}/user/login.action" method="post">
			<div style="height: 200px;margin-top: 50px;">
				&nbsp;&nbsp;&nbsp;&nbsp;User:<input type="text" name="u_id" style="margin:20px 10px; "><br>
				&nbsp;&nbsp;&nbsp;&nbsp;Pass:<input type="text" name="u_passwd" style="margin:20px 10px; "><br>
				<input type="submit" value="登录" style="margin: 20px 65px;width: 160px;border-radius: 5px;box-shadow: 3px 3px;">
			</div>
		</form>
	</div>
</body>
</html>