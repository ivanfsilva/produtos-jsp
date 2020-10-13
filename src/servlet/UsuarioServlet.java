package servlet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

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

		if (acao != null && acao.equalsIgnoreCase("delete")) {
			daoUsuario.delete(user);
			request.setAttribute("msg", "Usuário excluído com sucesso!");

			redirecionaCadastroUsuario(request, response);
		} else if (acao != null && acao.equalsIgnoreCase("editar")) {

			try {
				BeanUsuario usuario = daoUsuario.consultar(user);
				RequestDispatcher view = request.getRequestDispatcher("/cadastroUsuario.jsp");
				request.setAttribute("user", usuario);
				request.setAttribute("usuarios", daoUsuario.listar());
				view.forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (acao != null && acao.equalsIgnoreCase("listartodos")) {
			redirecionaCadastroUsuario(request, response);
		} else if (acao != null && acao.equalsIgnoreCase("download")) {
			try {
				BeanUsuario usuario = daoUsuario.consultar(user);
				if (usuario != null) {
					String contentType = "";
					byte[] fileBytes = null;
					String tipo = request.getParameter("tipo");
					
					if (tipo.equalsIgnoreCase("imagem")) {
						contentType = usuario.getContentType();
						/* Converte a base64 da imagem do banco para byte[] */
						fileBytes = new Base64().decodeBase64(usuario.getFotoBase64());
					} else if (tipo.equalsIgnoreCase("curriculo")) {
						/* Converte a base64 da imagem do banco para byte[] */
						contentType = usuario.getContentTypeCurriculo();
						fileBytes = new Base64().decodeBase64(usuario.getCurriculoBase64());
					}
					
					response.setHeader("content-Disposition", 
							"attachment;filename=arquivo." + 
					contentType.split("\\/")[1]);
					
					/* Converte a base64 da imagem do banco para byte[] */
					//fileBytes = new Base64().decodeBase64(usuario.getFotoBase64());
					/* Coloca os bytes em um objeto de entrada para processar */
					InputStream is = new ByteArrayInputStream(fileBytes);
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
		} else {
			// colar redirect para a mesma tela 
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

				        String fotoBase64 = new Base64()
				        		.encodeBase64String(converteStremParaByte(imagemFoto.getInputStream()));

				        usuario.setFotoBase64(fotoBase64);
				        usuario.setContentType(imagemFoto.getContentType());
				        /* Inicio miniatura imagem */
				        
				        /* Transforma enum bufferedImage */
				        byte[] imageByteDecode = new Base64().decodeBase64(fotoBase64);
				        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageByteDecode));
				        
				        /* Pega o tipo da imagem */
				        int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB: bufferedImage.getType();
				        
				        /* Cria imagem miniatura */
				        BufferedImage resizedImage = new BufferedImage(100, 100, type);
				        Graphics2D g = resizedImage.createGraphics();
				        g.drawImage(bufferedImage, 0, 0, 100, 100, null);
				        g.dispose();
				        
				        /* Escrever imagem novamente */
				        ByteArrayOutputStream baos = new ByteArrayOutputStream();
				        ImageIO.write(resizedImage, "png", baos);
				        
				        String miniaturaBase64 = "data:image/png;base64," + 
				        		DatatypeConverter.printBase64Binary(baos.toByteArray());
				        
				        /* Fim miniatura imagem */
				        
				        
				        usuario.setFotoBase64Miniatura(miniaturaBase64);
				    } else {
				    	usuario.setFotoBase64(request.getParameter("fotoTemp"));
				        usuario.setContentType(request.getParameter("contenttypeTemp"));
				    }
			    	
			    	// Processa PDF
			    	
			    	Part curriculoPdf = request.getPart("curriculo");
			    	if (curriculoPdf != null && curriculoPdf.getInputStream().available() > 0) {
			    		String curriculoBase64 = Base64.encodeBase64String(converteStremParaByte(curriculoPdf.getInputStream()));
			    		usuario.setCurriculoBase64(curriculoBase64);
				        usuario.setContentTypeCurriculo(curriculoPdf.getContentType());
			    	} else {
			    		usuario.setCurriculoBase64(request.getParameter("curriculoTemp"));
				        usuario.setContentTypeCurriculo(request.getParameter("contenttypecurriculoTemp"));
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
