package br.uel.gamehub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.uel.gamehub.model.Compra;

@Repository
public class CompraDAO implements DAO<Compra> {

    private static final String CREATE_QUERY = 
            "INSERT INTO loja.compra (comp_id_cliente, comp_preco, comp_data_hora) " +
            "VALUES (?, ?, ?) RETURNING id_comp;";

    private static final String READ_QUERY = 
            "SELECT id_comp, comp_id_cliente, comp_preco, comp_data_hora " +
            "FROM loja.compra " +
            "WHERE id_comp = ?;";

    private static final String UPDATE_QUERY = 
            "UPDATE loja.compra " +
            "SET comp_id_cliente = ?, comp_preco = ?, comp_data_hora = ? " +
            "WHERE id_comp = ?;";

    private static final String DELETE_QUERY = 
            "DELETE FROM loja.compra " +
            "WHERE id_comp = ?;";

    private static final String ALL_QUERY = 
            "SELECT id_comp, comp_id_cliente, comp_preco, comp_data_hora " +
            "FROM loja.compra " +
            "ORDER BY id_comp;";

    private static final String FIND_BY_CLIENTE_QUERY = 
            "SELECT id_comp, comp_id_cliente, comp_preco, comp_data_hora " +
            "FROM loja.compra " +
            "WHERE comp_id_cliente = ? " +
            "ORDER BY comp_data_hora DESC;";

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void create(Compra compra) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, compra.getIdCliente());
            statement.setBigDecimal(2, compra.getPreco());
            statement.setTimestamp(3, Timestamp.valueOf(compra.getDataHoraCompra()));

            try (ResultSet generatedKeys = statement.executeQuery()) {
                if (generatedKeys.next()) {
                    compra.setIdCompra(generatedKeys.getInt(1)); 
                } else {
                    throw new SQLException("Erro ao inserir compra, nenhum ID foi gerado.");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao inserir compra: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Compra read(Integer id) throws SQLException {
        Compra compra = new Compra();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    compra.setIdCompra(result.getInt("id_comp"));
                    compra.setIdCliente(result.getInt("comp_id_cliente"));
                    compra.setPreco(result.getBigDecimal("comp_preco"));
                    compra.setDataHoraCompra(result.getObject("comp_data_hora", LocalDateTime.class));
                } else {
                    throw new SQLException("Erro ao visualizar: Compra não encontrada.");
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao visualizar compra: " + ex.getMessage(), ex);
        }

        return compra;
    }

    @Override
    public void update(Compra compra) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, compra.getIdCliente());
            statement.setBigDecimal(2, compra.getPreco());
            statement.setObject(3, compra.getDataHoraCompra());
            statement.setInt(4, compra.getIdCompra());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: Compra não encontrada.");
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao editar compra: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: Compra não encontrada.");
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao excluir compra: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Compra> all() throws SQLException {
        List<Compra> compraList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Compra compra = new Compra();
                compra.setIdCompra(result.getInt("id_comp"));
                compra.setIdCliente(result.getInt("comp_id_cliente"));
                compra.setPreco(result.getBigDecimal("comp_preco"));
                compra.setDataHoraCompra(result.getObject("comp_data_hora", LocalDateTime.class));

                compraList.add(compra);
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao listar compras: " + ex.getMessage(), ex);
        }

        return compraList;
    }

    public List<Compra> findByClienteId(int clienteId) throws SQLException {
        List<Compra> compraList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CLIENTE_QUERY)) {
            statement.setInt(1, clienteId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Compra compra = new Compra();
                    compra.setIdCompra(result.getInt("id_comp"));
                    compra.setIdCliente(result.getInt("comp_id_cliente"));
                    compra.setPreco(result.getBigDecimal("comp_preco"));
                    compra.setDataHoraCompra(result.getObject("comp_data_hora", LocalDateTime.class));

                    compraList.add(compra);
                }
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao listar compras do cliente: " + ex.getMessage(), ex);
        }

        return compraList;
    }
}
