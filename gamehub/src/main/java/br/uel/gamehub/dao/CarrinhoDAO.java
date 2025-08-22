package br.uel.gamehub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.uel.gamehub.model.Carrinho;

@Repository
public class CarrinhoDAO {

    private static final String CREATE_QUERY =
        "INSERT INTO loja.carrinho (car_id_cliente, car_id_jogo, car_qtd) VALUES (?, ?, ?)";

    private static final String READ_QUERY =
        "SELECT car_id_cliente, car_id_jogo, car_qtd FROM loja.carrinho WHERE car_id_cliente = ? AND car_id_jogo = ?";

    private static final String UPDATE_QUERY =
        "UPDATE loja.carrinho SET car_qtd = ? WHERE car_id_cliente = ? AND car_id_jogo = ?";

    private static final String DELETE_QUERY =
        "DELETE FROM loja.carrinho WHERE car_id_cliente = ? AND car_id_jogo = ?";

    private static final String ALL_QUERY =
        "SELECT car_id_cliente, car_id_jogo, car_qtd FROM loja.carrinho";

    private static final String FIND_BY_CLIENTE_QUERY =
        "SELECT * FROM loja.carrinho WHERE car_id_cliente = ?";

    private static final String DELETE_BY_CLIENTE_QUERY =
        "DELETE FROM loja.carrinho WHERE car_id_cliente = ?";

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void create(Carrinho carrinho) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, carrinho.getIdCliente());
            statement.setInt(2, carrinho.getIdJogo());
            statement.setInt(3, carrinho.getQtd());
            statement.executeUpdate();
        }
    }

    public Carrinho read(int idCliente, int idJogo) throws SQLException {
        Carrinho carrinho = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, idCliente);
            statement.setInt(2, idJogo);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    carrinho = new Carrinho();
                    carrinho.setIdCliente(result.getInt("car_id_cliente"));
                    carrinho.setIdJogo(result.getInt("car_id_jogo"));
                    carrinho.setQtd(result.getInt("car_qtd"));
                }
            }
        }
        return carrinho;
    }

    public void update(Carrinho carrinho) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, carrinho.getQtd());
            statement.setInt(2, carrinho.getIdCliente());
            statement.setInt(3, carrinho.getIdJogo());
            statement.executeUpdate();
        }
    }

    public void delete(int idCliente, int idJogo) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, idCliente);
            statement.setInt(2, idJogo);
            statement.executeUpdate();
        }
    }

    public List<Carrinho> all() throws SQLException {
        List<Carrinho> carrinhoList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Carrinho carrinho = new Carrinho();
                carrinho.setIdCliente(result.getInt("car_id_cliente"));
                carrinho.setIdJogo(result.getInt("car_id_jogo"));
                carrinho.setQtd(result.getInt("car_qtd"));
                carrinhoList.add(carrinho);
            }
        }
        return carrinhoList;
    }

    public List<Carrinho> findByClienteId(int clienteId) throws SQLException {
        List<Carrinho> carrinhoList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CLIENTE_QUERY)) {
            statement.setInt(1, clienteId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Carrinho carrinho = new Carrinho();
                    carrinho.setIdCliente(result.getInt("car_id_cliente"));
                    carrinho.setIdJogo(result.getInt("car_id_jogo"));
                    carrinho.setQtd(result.getInt("car_qtd"));
                    carrinhoList.add(carrinho);     
                }
            }
        }
        return carrinhoList;
    }

    public void clearCarrinhoByClienteId(int clienteId) throws SQLException {
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_BY_CLIENTE_QUERY)) {
            statement.setInt(1, clienteId);
            int affectedRows = statement.executeUpdate();
        
            if (affectedRows == 0) {
                throw new SQLException("Erro ao limpar carrinho: nenhum item encontrado para o clienteId: " + clienteId);
            }
        } catch (SQLException ex) {
        throw new SQLException("Erro ao limpar carrinho: " + ex.getMessage(), ex);
        }
    }

}
