<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Cadastro de Usuário</title>
<link rel="stylesheet" href="resources/css/cadastro.css" />

</head>
<body>
	<a href="acessoliberado.jsp">Início</a>
	<a href="index.jsp">Sair</a>
	<center>
		<h1>Cadastro de Telefones</h1>
		<h3 style="color: orange;">${ msg }</h3>
	</center>
	
	<form action="salvarTelefones" method="post" id="formUser" onsubmit="return validarCampos();">
		<ul class="form-style-1">
			<li>
				<table>
					<tr>
						<td>Id: </td>
						<td>
							<input type="text" id="id" name="id" value="${ userEscolhido.id }" readonly="readonly"/>	
						</td>
					</tr>
					<tr>
						<td>Nome: </td>
						<td>
							<input type="text" id="nome" name="nome" value="${ userEscolhido.nome }" readonly="readonly"/>	
						</td>
					</tr>
					<tr>
						<td>Telefone: </td>
						<td>
							<input type="text" id="numero" name="numero" />	
						</td>
					</tr>
					<tr>
						<td>Tipo: </td>
						<td>
							<!-- <input type="text" id="tipo" name="tipo" />	 -->
							<select id="tipo" name="tipo">
								<option>Casa</option>
								<option>Contato</option>
								<option>Celular</option>
							</select>
						</td>
					</tr>
					
					<tr>
						<td></td>
						<td>
							<input type="submit" value="Salvar" />
						</td>
						<td>
							<input type="submit" value="Voltar" onclick="document.getElementById('formUser').action = 'salvarTelefones?acao=voltar'"/>
						</td>
					</tr>
				</table>
			</li>
		</ul>
	</form>
	
	<div class="container">
		<table class="responsive-table">
			<thead>
			    <tr>
			      <th>ID</th>
			      <th>NÚMERO</th>
			      <th>TIPO</th>
			      <th>Excluir</th>
			    </tr>
			</thead>
			<caption>Usuários Cadastrados</caption>
			<tbody>
				<c:forEach items="${ telefones }" var="fone">
					<tr>
						<td style="width: 150px;">
							<c:out value="${fone.id}"></c:out>
						</td>
						<td style="width: 150px;">
							<c:out value="${fone.numero}"></c:out>
						</td>
						<td>
							<c:out value="${fone.tipo}"></c:out>
						</td>
						
						<td>
							<a href="salvarTelefones?acao=deleteFone&foneId=${fone.id}"><img src="resources/img/delete.png" 
							alt="Excluir" title="Excluir" width="20px" height="20px" /></a>
						</td>
						
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<script type="text/javascript">
		function validarCampos() {
			if (document.getElementById("numero").value == '') {
				//alert('Informe o NÚMERO');
				//return false;
			} else if (document.getElementById("tipo").value == '') {
					alert('Informe o TIPO');
					return false;
			}			
		return true;
		}
	</script>
</body>
</html>