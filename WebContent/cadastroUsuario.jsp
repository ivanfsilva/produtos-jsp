<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Cadastro de Usu�rio</title>
<link rel="stylesheet" href="resources/css/cadastro.css" />
<!-- Adicionando JQuery -->
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"
            integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
            crossorigin="anonymous">
    </script>

</head>
<body>
	<a href="acessoliberado.jsp">In�cio</a>
	<a href="index.jsp">Sair</a>
	<center>
		<h1>Cadastro de Usu�rio</h1>
		<h3 style="color: orange;">${ msg }</h3>
	</center>
	
	<form action="salvarUsuario" method="post" id="formUser" onsubmit="return validarCampos();">
		<ul class="form-style-1">
			<li>
				<table>
					<tr>
						<td>Id:</td>
						<td>
							<input type="text" id="id" name="id" value="${user.id }" readonly="readonly" />	
						</td>
					</tr>
					<tr>
						<td>Login: *</td>
						<td>
							<input type="text" id="login" name="login" value="${user.login }" required="true" />	
						</td>
					</tr>
					<tr>
						<td>Senha: *</td>
						<td>
							<input type="password" id="senha" name="senha" value="${user.senha }" required="true"/>	
						</td>
					</tr>
					<tr>
						<td>Nome: *</td>
						<td>
							<input type="text" id="nome" name="nome" value="${user.nome }" required="true"/>	
						</td>
					</tr>
					<tr>
						<td>Telefone: *</td>
						<td>
							<input type="text" id="telefone" name="telefone" value="${user.telefone }" required="true" />	
						</td>
					</tr>
					<tr>
						<td>Cep:</td>
						<td><input type="text" id="cep" name="cep"  onblur="consultaCep();" value="${user.cep }" /></td>
					</tr>
					<tr>
						<td>Logradouro:</td>
						<td><input type="text" id="logradouro" name="logradouro" value="${user.logradouro }" /></td>
					</tr>
					<tr>
						<td>Bairro:</td>
						<td><input type="text" id="bairro" name="bairro" value="${user.bairro }" /></td>
					</tr>
					<tr>
						<td>Cidade:</td>
						<td><input type="text" id="cidade" name="cidade" value="${user.cidade }" /></td>
					</tr>
					<tr>
						<td>UF:</td>
						<td><input type="text" id="uf" name="uf" value="${user.uf }" /></td>
					</tr>
					<tr>
						<td>IBGE:</td>
						<td><input type="text" id="ibge" name="ibge" value="${user.ibge }" /></td>
					</tr>
					<tr>
						<td></td>
						<td>
							<input type="submit" value="Salvar" />
							<input type="submit" value="Cancelar" 
								onclick="document.getElementById('formUser').action = 'salvarUsuario?acao=reset'"/>
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
			      <th>LOGIN</th>
			      <th>NOME</th>
			      <th>TELEFONE</th>
			      <th>A��O</th>
			    </tr>
			</thead>
			<caption>Usu�rios Cadastrados</caption>
			<tbody>
				<c:forEach items="${ usuarios }" var="user">
					<tr>
						<td style="width: 150px;">
							<c:out value="${user.id}"></c:out>
						</td>
						<td style="width: 150px;">
							<c:out value="${user.login}"></c:out>
						</td>
						<td>
							<c:out value="${user.nome}"></c:out>
						</td>
						<td>
							<c:out value="${user.telefone}"></c:out>
						</td>
						<td>
							<a href="salvarUsuario?acao=delete&user=${user.id}"><img src="resources/img/excluir.png" 
							alt="Excluir" title="Excluir" width="20px" height="20px" /></a>
						</td>
						<td>
							<a href="salvarUsuario?acao=editar&user=${user.id}"><img src="resources/img/editar.png" 
							alt="Editar" title="Editar" width="20px" height="20px" /></a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<script type="text/javascript">
		function validarCampos() {
			if (document.getElementById("login").value == '') {
				alert('Informe o LOGIN');
				return false;
			} else if (document.getElementById("senha").value == '') {
					alert('Informe a SENHA');
					return false;
			} else if (document.getElementById("nome").value == '') {
				alert('Informe o NOME');
				return false;
			} else if (document.getElementById("telefone").value == '') {
				alert('Informe o TELEFONE');
				return false;
			}			
		return true;
		}
		
		function consultaCep() {
			var cep = $("#cep").val();

			//Verifica se campo cep possui valor informado.
            if (cep != "") {

                //Express�o regular para validar o CEP.
                var validacep = /^[0-9]{8}$/;

                //Consulta o webservice viacep.com.br/
                $.getJSON("https://viacep.com.br/ws/"+ cep +"/json/?callback=?", function(dados) {

                    if (!("erro" in dados)) {
                        //Atualiza os campos com os valores da consulta.
                        $("#logradouro").val(dados.logradouro);
                        $("#bairro").val(dados.bairro);
                        $("#cidade").val(dados.localidade);
                        $("#uf").val(dados.uf);
                        $("#ibge").val(dados.ibge);
                    } 
                    else {
                        //CEP pesquisado n�o foi encontrado.
                        limpa_formul�rio_cep();
                        alert("CEP n�o encontrado.");
                        
                    }
                });
            }
		}
		
		function limpa_formul�rio_cep() {
            // Limpa valores do formul�rio de cep.
			$("#logradouro").val('');
            $("#bairro").val('');
            $("#cidade").val('');
            $("#uf").val('');
            $("#ibge").val('');
        }
	</script>
</body>
</html>