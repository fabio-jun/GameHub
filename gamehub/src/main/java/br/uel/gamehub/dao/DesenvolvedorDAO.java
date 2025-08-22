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

import br.uel.gamehub.model.Desenvolvedor;

@Repository
public class DesenvolvedorDAO {

    private static final String CREATE_QUERY =
        "INSERT INTO loja.desenvolvedor (nome_desenvolvedor, des_id_jogo) VALUES (?, ?)";

    private static final String READ_QUERY =
        "SELECT nome_desenvolvedor, des_id_jogo FROM loja.desenvolvedor WHERE nome_desenvolvedor = ? AND des_id_jogo = ?";

    private static final String UPDATE_QUERY =
        "UPDATE loja.desenvolvedor SET nome_desenvolvedor = ? WHERE des_id_jogo = ?";

    private static final String DELETE_QUERY =
        "DELETE FROM loja.desenvolvedor WHERE nome_desenvolvedor = ? AND des_id_jogo = ?";

    private static final String ALL_QUERY =
        "SELECT nome_desenvolvedor, des_id_jogo FROM loja.desenvolvedor";

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void create(Desenvolvedor desenvolvedor) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, desenvolvedor.getNomeDesenvolvedor());
            statement.setInt(2, desenvolvedor.getDesIdJogo());
            statement.executeUpdate();
        }
    }

    public Desenvolvedor read(String nomeDesenvolvedor, int desIdJogo) throws SQLException {
        Desenvolvedor desenvolvedor = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setString(1, nomeDesenvolvedor);
            statement.setInt(2, desIdJogo);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    desenvolvedor = new Desenvolvedor();
                    desenvolvedor.setNomeDesenvolvedor(result.getString("nome_desenvolvedor"));
                    desenvolvedor.setDesIdJogo(result.getInt("des_id_jogo"));
                }
            }
        }
        return desenvolvedor;
    }

    public void update(Desenvolvedor desenvolvedor) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, desenvolvedor.getNomeDesenvolvedor());
            statement.setInt(2, desenvolvedor.getDesIdJogo());
            statement.executeUpdate();
        }
    }

    public void delete(String nomeDesenvolvedor, int desIdJogo) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setString(1, nomeDesenvolvedor);
            statement.setInt(2, desIdJogo);
            statement.executeUpdate();
        }
    }

    public List<Desenvolvedor> all() throws SQLException {
        List<Desenvolvedor> desenvolvedorList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Desenvolvedor desenvolvedor = new Desenvolvedor();
                desenvolvedor.setNomeDesenvolvedor(result.getString("nome_desenvolvedor"));
                desenvolvedor.setDesIdJogo(result.getInt("des_id_jogo"));
                desenvolvedorList.add(desenvolvedor);
            }
        }
        return desenvolvedorList;
    }
}
