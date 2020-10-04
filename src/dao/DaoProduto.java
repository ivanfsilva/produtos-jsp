package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.BeanProduto;
import connection.SingleConnection;

public class DaoProduto {
	
	private Connection connection;
	
	public DaoProduto() {
		connection = SingleConnection.getConnection();
	}
	
	public void Salvar(BeanProduto produto) {
		try {
			String sql = "INSERT INTO produto (nome, valor, quantidade) VALUES (?, ?, ?);";
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setString(1, produto.getNome());
			stm.setDouble(2, produto.getValor());
			stm.setDouble(3, produto.getQuantidade());
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public List<BeanProduto> Listar() throws SQLException {
		List<BeanProduto> listar = new ArrayList<>();
		
		String sql = " SELECT * FROM produto ORDER BY nome, id;";
		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rst = stm.executeQuery();
		
		while (rst.next()) {
			BeanProduto produto = new BeanProduto();
			produto.setId(rst.getLong("id"));
			produto.setNome(rst.getString("nome"));
			produto.setQuantidade(rst.getDouble("quantidade"));
			produto.setValor(rst.getDouble("valor"));
			
			listar.add(produto);
		}
		
		
		return listar;
	}
	
	public void delete(String id) {
		String sql = "DELETE FROM produto WHERE id = '" + id + "'";
		
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
	
	public BeanProduto consultar(String id) throws SQLException {
		String sql = "SELECT * FROM usuario WHERE id = " + id;
		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rst = stm.executeQuery();
		
		if (rst.next()) { 
			BeanProduto produto = new BeanProduto();
			produto.setId(rst.getLong("id"));
			produto.setNome(rst.getString("nome"));
			produto.setQuantidade(rst.getDouble("quantidade"));
			produto.setValor(rst.getDouble("valor"));
			
			return produto;
		}
		return null;
	}

}
