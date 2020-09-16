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
	
	<a href="salvarUsuario?acao=listartodos">
		<img src="resources/img/usuario.png" 
			alt="Cadastro de usuário" title="Cadastro de usuário" 
			width="50px" height="50px" />
	</a>
</body>
</html>