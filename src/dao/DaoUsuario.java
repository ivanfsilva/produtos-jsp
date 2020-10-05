package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.BeanUsuario;
import connection.SingleConnection;

public class DaoUsuario {

	private Connection connection;

	public DaoUsuario() {
		connection = SingleConnection.getConnection();
	}

	public void salvar(BeanUsuario usuario) {
		try {
			String sql = "INSERT INTO usuario (login, senha, nome, telefone, "
					+ "cep, logradouro, bairro, cidade, uf, ibge) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, usuario.getLogin());
			stm.setString(2, usuario.getSenha());
			stm.setString(3, usuario.getNome());
			stm.setString(4, usuario.getTelefone());
			
			stm.setString(5, usuario.getCep());
			stm.setString(6, usuario.getLogradouro());
			stm.setString(7, usuario.getBairro());
			stm.setString(8, usuario.getCidade());
			stm.setString(9, usuario.getUf());
			stm.setString(10, usuario.getIbge());
			
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

	public List<BeanUsuario> listar() throws SQLException {
		List<BeanUsuario> listar = new ArrayList<>();

		String sql = "SELECT * FROM usuario ORDER BY nome, id;";
		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rst = stm.executeQuery();
		while (rst.next()) {
			BeanUsuario beanUsuario = new BeanUsuario();
			beanUsuario.setId(rst.getLong("id"));
			beanUsuario.setLogin(rst.getString("login"));
			beanUsuario.setSenha(rst.getString("senha"));
			beanUsuario.setNome(rst.getString("nome"));
			beanUsuario.setTelefone(rst.getString("telefone"));
			
			beanUsuario.setCep(rst.getString("cep"));
			beanUsuario.setLogradouro(rst.getString("logradouro"));
			beanUsuario.setBairro(rst.getString("bairro"));
			beanUsuario.setCidade(rst.getString("cidade"));
			beanUsuario.setUf(rst.getString("uf"));
			beanUsuario.setIbge(rst.getString("ibge"));

			listar.add(beanUsuario);
		}
		return listar;
	}
	
	public void delete(String id) {
		String sql = "DELETE FROM usuario WHERE id = '" + id + "'";
		
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

	public BeanUsuario consultar(String id) throws SQLException {
		String sql = "SELECT * FROM usuario WHERE id = " + id;
		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rst = stm.executeQuery();
		
		if (rst.next()) {
			BeanUsuario beanUsuario = new BeanUsuario();
			beanUsuario.setId(rst.getLong("id"));
			beanUsuario.setLogin(rst.getString("login"));
			beanUsuario.setSenha(rst.getString("senha"));
			beanUsuario.setNome(rst.getString("nome"));
			beanUsuario.setTelefone(rst.getString("telefone"));
			
			beanUsuario.setCep(rst.getString("cep"));
			beanUsuario.setLogradouro(rst.getString("logradouro"));
			beanUsuario.setBairro(rst.getString("bairro"));
			beanUsuario.setCidade(rst.getString("cidade"));
			beanUsuario.setUf(rst.getString("uf"));
			beanUsuario.setIbge(rst.getString("ibge"));
			
			return beanUsuario;
		}
		return null;		
	}
	
	public boolean validarLogin(String login) throws SQLException {
		String sql = "SELECT COUNT(1) AS qtd FROM usuario WHERE login = '" + login + "'";
		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rst = stm.executeQuery();
		
		if (rst.next()) {
			return rst.getInt("qtd") <= 0;
		}
		return false;		
	}

	public void atualizar(BeanUsuario usuario) {
		String sql = "UPDATE usuario "
				+ " SET login = ?, senha = ?, nome = ?, telefone = ?, "
				+ " cep = ?, logradouro = ?, bairro = ?, cidade = ?, "
				+ " uf = ?, ibge = ? "
				+ " WHERE id = " + usuario.getId();
		
		try {
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, usuario.getLogin());
			stm.setString(2, usuario.getSenha());
			stm.setString(3, usuario.getNome());
			stm.setString(4, usuario.getTelefone());
			
			stm.setString(5, usuario.getCep());
			stm.setString(6, usuario.getLogradouro());
			stm.setString(7, usuario.getBairro());
			stm.setString(8, usuario.getCidade());
			stm.setString(9, usuario.getUf());
			stm.setString(10, usuario.getIbge());
			
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
