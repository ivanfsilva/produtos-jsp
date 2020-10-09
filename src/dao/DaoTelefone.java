package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.BeanProduto;
import beans.BeanTelefone;
import connection.SingleConnection;

public class DaoTelefone {
	
	private Connection connection;
	
	public DaoTelefone() {
		connection = SingleConnection.getConnection();
	}
	
	public void salvar(BeanTelefone telefone) {
		try {
			String sql = "INSERT INTO telefone (numero, tipo, usuario_id) VALUES (?, ?, ?);";
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, telefone.getNumero());
			stm.setString(2, telefone.getTipo());
			stm.setLong(3, telefone.getUsuarioId());
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
	
	public List<BeanTelefone> listar(Long usuarioId) throws SQLException {
		List<BeanTelefone> listar = new ArrayList<>();
		
		String sql = "SELECT * FROM telefone WHERE usuario_id = " + usuarioId;
		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rst = stm.executeQuery();
		
		while (rst.next()) {
			BeanTelefone telefone = new BeanTelefone();
			telefone.setId(rst.getLong("id"));
			telefone.setNumero(rst.getString("numero"));
			telefone.setTipo(rst.getString("tipo"));
			telefone.setUsuarioId(rst.getLong("usuario_id"));
			
			listar.add(telefone);
		}
		
		
		return listar;
	}
	
	public void delete(String id) {
		String sql = "DELETE FROM telefone WHERE id = '" + id + "'";
		
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
	
}
