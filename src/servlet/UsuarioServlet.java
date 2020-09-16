package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.BeanUsuario;
import dao.DaoUsuario;

@WebServlet("/salvarUsuario")
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

			BeanUsuario usuario = new BeanUsuario();
			usuario.setId(!id.isEmpty() ? Long.parseLong(id) : 0);
			usuario.setLogin(login);
			usuario.setSenha(senha);
			usuario.setNome(nome);

			if (id == null || id.isEmpty()) {
				daoUsuario.salvar(usuario);
			} else {
				daoUsuario.atualizar(usuario);
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
}
