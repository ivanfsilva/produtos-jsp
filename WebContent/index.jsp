<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login</title>
<link rel="stylesheet" href="resources/css/estilo.css" />
</head>
<body>
	<div class="login-page">
		<div class="form">
			<form class="login-form" action="LoginServlet" method="post">
				Login: <input type="text" id="login" name="login" /> <br />
				Senha: <input type="password" id="senha" name="senha" /> <br /> 
				<button	type="submit" value="Logar">Logar</button> <br />
			</form>
		</div>
	</div>

</body>
</html>