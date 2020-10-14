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
					+ "curriculobase64, contenttypecurriculo, fotobase64miniatura, ativo, sexo, perfil) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
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
			stm.setString(16, usuario.getSexo());
			stm.setString(17, usuario.getPerfil());
			
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
	
	public List<BeanUsuario> listar (String descricaoconsulta) throws SQLException{
		String sql = "SELECT * FROM usuario "
				+ " WHERE login <> 'admin' AND "
				+ " LOWER(nome) LIKE LOWER('%" + descricaoconsulta + "%') "
				+ " ORDER BY nome"; 
		return consultarUsuarios(sql);
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
		sql.append(" uf = ?, ibge = ?, ativo = ?, sexo = ?, perfil = ? "); 
		
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
			stm.setString(11, usuario.getSexo());
			stm.setString(12, usuario.getPerfil());
			
			if (usuario.isAtualizarImage()) {
				stm.setString(13, usuario.getFotoBase64());
				stm.setString(14, usuario.getContentType());
			}

			if (usuario.isAtualizarPdf()) {
				
				if (usuario.isAtualizarPdf() && !usuario.isAtualizarImage()){
					stm.setString(13, usuario.getCurriculoBase64());
					stm.setString(14,
							usuario.getContentTypeCurriculo());
				} else {
					stm.setString(15, usuario.getCurriculoBase64());
					stm.setString(16,
							usuario.getContentTypeCurriculo());
				}

			}else {
				if (usuario.isAtualizarImage()) {
					stm.setString(15,
							usuario.getFotoBase64Miniatura());
				}
			}

			if (usuario.isAtualizarImage() && usuario.isAtualizarPdf()) {
				stm.setString(17,
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
		beanUsuario.setAtivo(rst.getBoolean("ativo"));
		beanUsuario.setSexo(rst.getString("sexo"));
		beanUsuario.setPerfil(rst.getString("perfil"));
		
		return beanUsuario;
	}
	
	private List<BeanUsuario> consultarUsuarios(String sql)
			throws SQLException {
		List<BeanUsuario> listar = new ArrayList<BeanUsuario>();
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			BeanUsuario beanUsuario = new BeanUsuario();
			beanUsuario.setId(resultSet.getLong("id"));
			beanUsuario.setLogin(resultSet.getString("login"));
			beanUsuario.setSenha(resultSet.getString("senha"));
			beanUsuario.setNome(resultSet.getString("nome"));
			beanUsuario.setCep(resultSet.getString("cep"));
			beanUsuario.setLogradouro(resultSet.getString("logradouro"));
			beanUsuario.setBairro(resultSet.getString("bairro"));
			beanUsuario.setCidade(resultSet.getString("cidade"));
			beanUsuario.setUf(resultSet.getString("uf"));
			beanUsuario.setIbge(resultSet.getString("ibge"));
			beanUsuario.setContentType(resultSet.getString("contenttype"));
			// beanCursoJsp.setFotoBase64(resultSet.getString("fotobase64"));
			beanUsuario.setFotoBase64Miniatura(resultSet
					.getString("fotobase64miniatura"));
			beanUsuario.setCurriculoBase64(resultSet
					.getString("curriculobase64"));
			beanUsuario.setContentTypeCurriculo(resultSet
					.getString("contenttypecurriculo"));
			
			beanUsuario.setAtivo(resultSet.getBoolean("ativo"));
			beanUsuario.setSexo(resultSet.getString("sexo"));
			beanUsuario.setPerfil(resultSet.getString("perfil"));
			listar.add(beanUsuario);
		}
		
		return listar;
	}
}
