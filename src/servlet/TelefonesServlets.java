package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.BeanTelefone;
import beans.BeanUsuario;
import dao.DaoTelefone;
import dao.DaoUsuario;

@WebServlet("/salvarTelefones")
public class TelefonesServlets extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DaoUsuario daoUsuario = new DaoUsuario();
	
	private DaoTelefone daoTelefone = new DaoTelefone();
	
	public TelefonesServlets() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		try {
			String user = request.getParameter("user");
			String acao = request.getParameter("acao");
			
			if (acao.endsWith("addFone")) {
				BeanUsuario usuario = daoUsuario.consultar(user);
				request.getSession().setAttribute("userEscolhido", usuario);
				
				RequestDispatcher view = request.getRequestDispatcher("/telefones.jsp");
				request.setAttribute("telefones", daoTelefone.listar(usuario.getId()));
				request.setAttribute("msg", "Salvo com sucesso");
				// request.setAttribute("userId", user);
				view.forward(request, response);
			} else if (acao.endsWith("deleteFone")) {
				String foneId = request.getParameter("foneId");
				daoTelefone.delete(foneId);
				
				BeanUsuario usuario = (BeanUsuario) request.getSession().getAttribute("userEscolhido");
				
				RequestDispatcher view = request.getRequestDispatcher("/telefones.jsp");
				request.setAttribute("telefones", daoTelefone.listar(usuario.getId()));
				request.setAttribute("msg", "Removido com sucesso");
				// request.setAttribute("userId", user);
				view.forward(request, response);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}


	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		BeanUsuario usuario = (BeanUsuario) request.getSession().getAttribute("userEscolhido");
		
		String numero = request.getParameter("numero");
		String tipo = request.getParameter("tipo");
		
		//System.out.println(usuario.getId());
		//System.out.println(numero);
		//System.out.println(tipo);
		
		BeanTelefone telefone = new BeanTelefone();
		telefone.setNumero(numero);
		telefone.setTipo(tipo);
		telefone.setUsuarioId(usuario.getId());
		
		daoTelefone.salvar(telefone);
		
		RequestDispatcher view = request.getRequestDispatcher("/telefones.jsp");
		try {
			request.setAttribute("telefones", daoTelefone.listar(usuario.getId()));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("msg", "Salvo com sucesso");
		// request.setAttribute("userId", user);
		view.forward(request, response);
	}

}
