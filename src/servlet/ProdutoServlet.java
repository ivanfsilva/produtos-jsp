package servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.BeanProduto;
import dao.DaoProduto;

@WebServlet("/salvarProduto")
public class ProdutoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DaoProduto daoProduto = new DaoProduto();
       
    public ProdutoServlet() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String acao = request.getParameter("acao") != null ? request.getParameter("acao") : "listartodos";
		String id = request.getParameter("id");

		if (acao.equalsIgnoreCase("delete")) {
			daoProduto.delete(id);

			redirecionaCadastroProduto(request, response);
		} else if (acao.equalsIgnoreCase("editar")) {

			try {
				BeanProduto produto = daoProduto.consultar(id);
				RequestDispatcher view = request.getRequestDispatcher("/cadastroProduto.jsp");
				request.setAttribute("produto", produto);
				request.setAttribute("produtos", daoProduto.listar());
				view.forward(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (acao.equalsIgnoreCase("listartodos")) {
			redirecionaCadastroProduto(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String acao = request.getParameter("acao");

		if (acao != null && acao.equalsIgnoreCase("reset")) {
			redirecionaCadastroProduto(request, response);
		} else {

			String id = request.getParameter("id");
			String nome = request.getParameter("nome");
			String valor = request.getParameter("valor");
			String quantidade = request.getParameter("quantidade");

			BeanProduto produto = new BeanProduto();
			produto.setId(!id.isEmpty() ? Long.parseLong(id) : null);
			produto.setNome(nome);
			if (valor != null && !valor.isEmpty()) {
				String valorParse = valor.replaceAll("\\.", "");// 10500,20
				valorParse = valorParse.replaceAll("\\,", ".");//10500.20
				
				produto.setValor(Double.parseDouble(valorParse));
			}
			produto.setQuantidade(Double.parseDouble(quantidade));
			try {
				if (id == null || id.isEmpty() && !daoProduto.validarNome(nome)) {
					request.setAttribute("msg", "Nome já cadastrado.");
					request.setAttribute("produto", produto);
				}
				if (id == null || id.isEmpty() && daoProduto.validarNome(nome)) {
					daoProduto.salvar(produto);
				} else if ( id != null && !id.isEmpty() ) {
					daoProduto.atualizar(produto);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			redirecionaCadastroProduto(request, response);
		}
	}
	
	private void redirecionaCadastroProduto(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher view = request.getRequestDispatcher("/cadastroProduto.jsp");
		try {
			request.setAttribute("produtos", daoProduto.listar());
			view.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
