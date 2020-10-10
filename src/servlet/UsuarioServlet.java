package servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.buf.UDecoder;
import org.apache.tomcat.util.codec.binary.Base64;


import beans.BeanUsuario;
import dao.DaoUsuario;

@WebServlet("/salvarUsuario")
@MultipartConfig
public class UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DaoUsuario daoUsuario = new DaoUsuario();

	public UsuarioServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String acao = request.getParameter("acao");
		String user = request.getParameter("user");

		if (acao.equalsIgnoreCase("delete")) {
			daoUsuario.delete(user);
			request.setAttribute("msg", "Usuário excluído com sucesso!");

			redirecionaCadastroUsuario(request, response);
		} else if (acao.equalsIgnoreCase("editar")) {

			try {
				BeanUsuario usuario = daoUsuario.consultar(user);
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("user", usuario);
				request.setAttribute("usuarios", daoUsuario.listar());
				view.forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (acao.equalsIgnoreCase("listartodos")) {
			redirecionaCadastroUsuario(request, response);
		} else if (acao.equalsIgnoreCase("download")) {
			try {
				BeanUsuario usuario = daoUsuario.consultar(user);
				if (usuario != null) {
					response.setHeader("content-Disposition", 
							"attachment;filename=arquivo." + 
					usuario.getContentType().split("\\/")[1]);
					
					/* Converte a base64 da imagem do banco para byte[] */
					byte[] imageFotoByte = new Base64().decodeBase64(usuario.getFotoBase64());
					/* Coloca os bytes em um objeto de entrada para processar */
					InputStream is = new ByteArrayInputStream(imageFotoByte);
					/* Inicio da resposta para o navegador */
					int read = 0;
					byte[] bytes = new byte[1024];
					OutputStream os = response.getOutputStream();
					
					while ((read = is.read(bytes)) != - 1) {
						os.write(bytes, 0, read);
					}
					
					os.flush();
					os.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String acao = request.getParameter("acao");

		if (acao != null && acao.equalsIgnoreCase("reset")) {
			redirecionaCadastroUsuario(request, response);
		} else {

			String id = request.getParameter("id");
			String login = request.getParameter("login");
			String senha = request.getParameter("senha");
			String nome = request.getParameter("nome");
			String telefone = request.getParameter("telefone");
			
			String cep = request.getParameter("cep");
			String logradouro = request.getParameter("logradouro");
			String bairro = request.getParameter("bairro");
			String cidade = request.getParameter("cidade");
			String uf = request.getParameter("uf");
			String ibge = request.getParameter("ibge");

			BeanUsuario usuario = new BeanUsuario();
			usuario.setId(!id.isEmpty() ? Long.parseLong(id) : null);
			usuario.setLogin(login);
			usuario.setSenha(senha);
			usuario.setNome(nome);
			usuario.setTelefone(telefone);
			
			usuario.setCep(cep);
			usuario.setLogradouro(logradouro);
			usuario.setBairro(bairro);
			usuario.setCidade(cidade);
			usuario.setUf(uf);
			usuario.setIbge(ibge);

			try {
				
				/* Inicio File upload de imagens e pdf */
				
				if (ServletFileUpload.isMultipartContent(request)) {

				    Part imagemFoto = request.getPart("foto");

				    if (imagemFoto != null && imagemFoto.getInputStream().available() > 0) {

				        String fotoBase64 = Base64.encodeBase64String(converteStremParaByte(imagemFoto.getInputStream()));

					usuario.setFotoBase64(fotoBase64);
					usuario.setContentType(imagemFoto.getContentType());
				//	usuario.setFotoBase64Miniatura(criarMiniaturaImagem(fotoBase64));
				    } else {
				//	usuario.setAtualizarImagem(Boolean.FALSE);
				    }
				}
				
				/* Fim File upload de imagens e pdf */
				
				if (id == null || id.isEmpty() && !daoUsuario.validarLogin(login)) {
					request.setAttribute("msg", "Login já cadastrado.");
					request.setAttribute("user", usuario);
				}
				if (id == null || id.isEmpty() && daoUsuario.validarLogin(login)) {
					daoUsuario.salvar(usuario);
					request.setAttribute("msg", "Salvo com sucesso!");
				} else if ( id != null && !id.isEmpty() ) {
					daoUsuario.atualizar(usuario);
					request.setAttribute("msg", "Atualizado com sucesso!");
				} 
			} catch (Exception e) {
				e.printStackTrace();
			} 
			redirecionaCadastroUsuario(request, response);
		}
	}

	private void redirecionaCadastroUsuario(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
		try {
			request.setAttribute("usuarios", daoUsuario.listar());
			view.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*Converte a entrada de fluxo de dados da imagem para byte[]*/
	private byte[] converteStremParaByte(InputStream imagem) throws Exception {
		
	 ByteArrayOutputStream baos = new ByteArrayOutputStream();
	 int reads = imagem.read();
	 while (reads != -1){
		 baos.write(reads);
		 reads = imagem.read();
	 }
	 
	 return baos.toByteArray();
	
	}
}
