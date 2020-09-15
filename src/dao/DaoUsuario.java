package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.BeanCursoJsp;
import connection.SingleConnection;

public class DaoUsuario {

	private Connection connection;

	public DaoUsuario() {
		connection = SingleConnection.getConnection();
	}

	public void salvar(BeanCursoJsp usuario) {
		try {
			String sql = "INSERT INTO usuario (usuario, senha) VALUES (?, ?);";
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, usuario.getLogin());
			stm.setString(2, usuario.getSenha());
			stm.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public List<BeanCursoJsp> listar() throws SQLException {
		List<BeanCursoJsp> listar = new ArrayList<>();

		String sql = "SELECT * FROM usuario;";
		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rst = stm.executeQuery();
		while (rst.next()) {
			BeanCursoJsp beanCursoJsp = new BeanCursoJsp();
			beanCursoJsp.setId(rst.getLong("id"));
			beanCursoJsp.setLogin(rst.getString("usuario"));
			beanCursoJsp.setSenha(rst.getString("senha"));

			listar.add(beanCursoJsp);
		}
		return listar;
	}
	
	public void delete(String login) {
		String sql = "DELETE FROM usuario WHERE usuario = '" + login + "'";
		
		try {
			PreparedStatement stm;
			stm = connection.prepareStatement(sql);
			stm.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
	}

	public BeanCursoJsp consultar(String usuario) throws SQLException {
		String sql = "SELECT * FROM usuario WHERE usuario = '" + usuario + "'";
		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rst = stm.executeQuery();
		
		if (rst.next()) {
			BeanCursoJsp beanCursoJsp = new BeanCursoJsp();
			beanCursoJsp.setId(rst.getLong("id"));
			beanCursoJsp.setLogin(rst.getString("usuario"));
			beanCursoJsp.setSenha(rst.getString("senha"));
			
			return beanCursoJsp;
		}
		return null;		
	}

	public void atualizar(BeanCursoJsp usuario) {
		String sql = "UPDATE usuario SET login = ?, senha = ? WHERE id = " + usuario.getId();
		
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, usuario.getLogin());
			stm.setString(2, usuario.getSenha());
			stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}
