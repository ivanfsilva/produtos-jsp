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
			String sql = "INSERT INTO usuario (login, senha, nome, "
					+ "cep, logradouro, bairro, cidade, uf, ibge, fotobase64, contenttype, "
					+ "curriculobase64, contenttypecurriculo, fotobase64miniatura, ativo) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, usuario.getLogin());
			stm.setString(2, usuario.getSenha());
			stm.setString(3, usuario.getNome());
			
			stm.setString(4, usuario.getCep());
			stm.setString(5, usuario.getLogradouro());
			stm.setString(6, usuario.getBairro());
			stm.setString(7, usuario.getCidade());
			stm.setString(8, usuario.getUf());
			stm.setString(9, usuario.getIbge());
			
			stm.setString(10, usuario.getFotoBase64());
			stm.setString(11, usuario.getContentType());
			stm.setString(12, usuario.getCurriculoBase64());
			stm.setString(13, usuario.getContentTypeCurriculo());
			stm.setString(14, usuario.getFotoBase64Miniatura());
			stm.setBoolean(15, usuario.isAtivo());
			
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

		String sql = "SELECT * FROM usuario WHERE login <> 'adm' ORDER BY nome, id;";
		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rst = stm.executeQuery();
		while (rst.next()) {
			BeanUsuario beanUsuario = popularBeanUsuario(rst);

			listar.add(beanUsuario);
		}
		return listar;
	}
	
	public void delete(String id) {
		String sql = "DELETE FROM usuario WHERE id = '" + id + "' AND login <> 'adm'";
		
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
		String sql = "SELECT * FROM usuario WHERE id = '" + id
				+ "' and login <> 'adm'";
		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rst = stm.executeQuery();
		
		if (rst.next()) {
			BeanUsuario beanUsuario = popularBeanUsuario(rst);
			
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
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" UPDATE usuario ");
		sql.append(" SET login = ?, senha = ?, nome = ?, ");
		sql.append(" cep = ?, logradouro = ?, bairro = ?, cidade = ?, ");
		sql.append(" uf = ?, ibge = ?, ativo = ? "); 
		
		if (usuario.isAtualizarImage()) {
			sql.append(", fotobase64 = ?, contenttype = ? ");
		}
		
		if(usuario.isAtualizarPdf()) {
			sql.append(", curriculobase64 = ?, contenttypecurriculo = ? "); 
		}
				
		if (usuario.isAtualizarImage()) {
			sql.append(", fotobase64miniatura = ? ");
		}
				
		sql.append(" WHERE id = " + usuario.getId());
		
		try {
			PreparedStatement stm = connection.prepareStatement(sql.toString());
			stm.setString(1, usuario.getLogin());
			stm.setString(2, usuario.getSenha());
			stm.setString(3, usuario.getNome());
			
			stm.setString(4, usuario.getCep());
			stm.setString(5, usuario.getLogradouro());
			stm.setString(6, usuario.getBairro());
			stm.setString(7, usuario.getCidade());
			stm.setString(8, usuario.getUf());
			stm.setString(9, usuario.getIbge());
			stm.setBoolean(10, usuario.isAtivo());
			
			if (usuario.isAtualizarImage()) {
				stm.setString(11, usuario.getFotoBase64());
				stm.setString(12, usuario.getContentType());
			}

			if (usuario.isAtualizarPdf()) {
				
				if (usuario.isAtualizarPdf() && !usuario.isAtualizarImage()){
					stm.setString(11, usuario.getCurriculoBase64());
					stm.setString(12,
							usuario.getContentTypeCurriculo());
				} else {
					stm.setString(13, usuario.getCurriculoBase64());
					stm.setString(14,
							usuario.getContentTypeCurriculo());
				}

			}else {
				if (usuario.isAtualizarImage()) {
					stm.setString(13,
							usuario.getFotoBase64Miniatura());
				}
			}

			if (usuario.isAtualizarImage() && usuario.isAtualizarPdf()) {
				stm.setString(15,
						usuario.getFotoBase64Miniatura());
			}
			
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
	
	private BeanUsuario popularBeanUsuario(ResultSet rst) throws SQLException {
		BeanUsuario beanUsuario = new BeanUsuario();
		beanUsuario.setId(rst.getLong("id"));
		beanUsuario.setLogin(rst.getString("login"));
		beanUsuario.setSenha(rst.getString("senha"));
		beanUsuario.setNome(rst.getString("nome"));
		
		beanUsuario.setCep(rst.getString("cep"));
		beanUsuario.setLogradouro(rst.getString("logradouro"));
		beanUsuario.setBairro(rst.getString("bairro"));
		beanUsuario.setCidade(rst.getString("cidade"));
		beanUsuario.setUf(rst.getString("uf"));
		beanUsuario.setIbge(rst.getString("ibge"));
		
		beanUsuario.setFotoBase64(rst.getString("fotoBase64"));
		beanUsuario.setFotoBase64Miniatura(rst.getString("fotobase64miniatura"));
		beanUsuario.setContentType(rst.getString("contentType"));		
		beanUsuario.setCurriculoBase64(rst.getString("curriculobase64"));
		beanUsuario.setContentTypeCurriculo(rst.getString("contenttypecurriculo"));	
		
		return beanUsuario;
	}
}
