<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Cadastro de Produto</title>
<link rel="stylesheet" href="resources/css/cadastro.css" />
</head>
<body>
	<a href="acessoliberado.jsp">Início</a>
	<a href="index.jsp">Sair</a>
	<center>
		<h1>Cadastro de Produto</h1>
		<h3 style="color: orange;">${ msg }</h3>
	</center>
	
	<form action="salvarProduto" method="post" id="formUser" onsubmit="return validarCamposProduto();">
		<ul class="form-style-1">
			<li>
				<table>
					<tr>
						<td>Id:</td>
						<td>
							<input type="text" id="id" name="id" value="${produto.id }" readonly="readonly" />	
						</td>
					</tr>
					<tr>
						<td>Nome:</td>
						<td>
							<input type="text" id="nome" name="nome" value="${produto.nome }" />	
						</td>
					</tr>
					<tr>
						<td>Quantidade:</td>
						<td>
							<input type="text" id="quantidade" name="quantidade" value="${produto.quantidade }"/>	
						</td>
					</tr>
					<tr>
						<td>Valor:</td>
						<td>
							<input type="text" id="valor" name="valor" value="${produto.valor }"/>	
						</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<input type="submit" value="Salvar" />
							<input type="submit" value="Cancelar" 
								onclick="document.getElementById('formUser').action = 'salvarProduto?acao=reset'"/>
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
			      <th>NOME</th>
			      <th>QUANTIDADE</th>
			      <th>VALOR</th>
			      <th>AÇÃO</th>
			    </tr>
			</thead>
			<caption>Produtos Cadastrados</caption>
			<tbody>
				<c:forEach items="${ produtos }" var="produto">
					<tr>
						<td style="width: 150px;">
							<c:out value="${produto.id}"></c:out>
						</td>
						<td style="width: 150px;">
							<c:out value="${produto.nome}"></c:out>
						</td>
						<td>
							<c:out value="${produto.quantidade}"></c:out>
						</td>
						<td>
							<c:out value="${produto.valor}"></c:out>
						</td>
						<td>
							<a href="salvarProduto?acao=delete&id=${produto.id}"><img src="resources/img/excluir.png" 
							alt="Excluir" title="Excluir" width="20px" height="20px" /></a>
						</td>
						<td>
							<a href="salvarProduto?acao=editar&id=${produto.id}"><img src="resources/img/editar.png" 
							alt="Editar" title="Editar" width="20px" height="20px" /></a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<script type="text/javascript">
		function validarCamposProduto() {
			if (document.getElementById("nome").value == '') {
				alert('Informe o NOME');
				document.getElementById("nome").focus();
				return false;
			} else if (document.getElementById("quantidade").value == '') {
				alert('Informe a QUANTIDADE');
				document.getElementById("quantidade").focus();
				return false;
			} else if (document.getElementById("valor").value == '') {
				alert('Informe o VALOR');
				document.getElementById("valor").focus();
				return false;
			}
			return true;
		}
	</script>
</body>
</html>