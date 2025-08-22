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

import br.uel.gamehub.model.Categoria;

@Repository
public class CategoriaDAO {

    private static final String CREATE_QUERY =
        "INSERT INTO loja.categoria (nome_categoria, cat_id_jogo) VALUES (?, ?)";

    private static final String READ_QUERY =
        "SELECT nome_categoria, cat_id_jogo FROM loja.categoria WHERE nome_categoria = ? AND cat_id_jogo = ?";

    private static final String UPDATE_QUERY =
        "UPDATE loja.categoria SET nome_categoria = ? WHERE cat_id_jogo = ?";

    private static final String DELETE_QUERY =
        "DELETE FROM loja.categoria WHERE nome_categoria = ? AND cat_id_jogo = ?";

    private static final String ALL_QUERY =
        "SELECT nome_categoria, cat_id_jogo FROM loja.categoria";

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void create(Categoria categoria) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, categoria.getNomeCategoria());
            statement.setInt(2, categoria.getIdJogo());
            statement.executeUpdate();
        }
    }

    public Categoria read(String nomeCategoria, int idJogo) throws SQLException {
        Categoria categoria = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setString(1, nomeCategoria);
            statement.setInt(2, idJogo);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    categoria = new Categoria();
                    categoria.setNomeCategoria(result.getString("nome_categoria"));
                    categoria.setIdJogo(result.getInt("cat_id_jogo"));
                }
            }
        }
        return categoria;
    }

    public void update(Categoria categoria) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, categoria.getNomeCategoria());
            statement.setInt(2, categoria.getIdJogo());
            statement.executeUpdate();
        }
    }

    public void delete(String nomeCategoria, int idJogo) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setString(1, nomeCategoria);
            statement.setInt(2, idJogo);
            statement.executeUpdate();
        }
    }

    public List<Categoria> all() throws SQLException {
        List<Categoria> categoriaList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Categoria categoria = new Categoria();
                categoria.setNomeCategoria(result.getString("nome_categoria"));
                categoria.setIdJogo(result.getInt("cat_id_jogo"));
                categoriaList.add(categoria);
            }
        }
        return categoriaList;
    }
}
