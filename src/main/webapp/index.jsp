<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${pageContext.servletContext.contextPath }/user/findOneUser.action" method="post">
		输入id:<input type="text" name="id"></br>
		<input type="submit">
	</form>
</body>
</html>