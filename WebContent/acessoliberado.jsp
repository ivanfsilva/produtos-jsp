<jsp:useBean id="calcula" class="beans.BeanUsuario" type="beans.BeanUsuario" scope="page"></jsp:useBean>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<jsp:setProperty property="*" name="calcula"/>
	<h3>Seja bem vindo ao sistema em JSP</h3>
	
	<a href="salvarUsuario?acao=listartodos">Cadastro de usu�rios</a>
</body>
</html>