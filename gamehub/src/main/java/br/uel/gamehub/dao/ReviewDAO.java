package br.uel.gamehub.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.uel.gamehub.model.Review;

@Repository
public class ReviewDAO {

    private static final String CREATE_QUERY =
        "INSERT INTO loja.review (rev_id_jogo, rev_id_cliente, rev_comentario, rev_nota, rev_data) " +
        "VALUES (?, ?, ?, ?, ?);";

    private static final String READ_QUERY =
        "SELECT rev_id_jogo, rev_id_cliente, rev_comentario, rev_nota, rev_data " +
        "FROM loja.review WHERE rev_id_jogo = ? AND rev_id_cliente = ?;";

    private static final String UPDATE_QUERY =
        "UPDATE loja.review " +
        "SET rev_comentario = ?, rev_nota = ?, rev_data = ? " +
        "WHERE rev_id_jogo = ? AND rev_id_cliente = ?;";

    private static final String DELETE_QUERY =
        "DELETE FROM loja.review WHERE rev_id_jogo = ? AND rev_id_cliente = ?;";

    private static final String ALL_QUERY =
        "SELECT rev_id_jogo, rev_id_cliente, rev_comentario, rev_nota, rev_data " +
        "FROM loja.review;";

    private static final String FIND_BY_JOGO_QUERY =
        "SELECT r.rev_id_jogo, r.rev_id_cliente, r.rev_comentario, r.rev_nota, r.rev_data, " +
        "CONCAT(c.pnome, ' ', c.snome) as nome_cliente " +
        "FROM loja.review r " +
        "LEFT JOIN loja.cliente c ON r.rev_id_cliente = c.id_cliente " +
        "WHERE r.rev_id_jogo = ? " +
        "ORDER BY r.rev_data DESC;";

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void create(Review review) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, review.getRevIdJogo());
            statement.setInt(2, review.getRevIdCliente());
            statement.setString(3, review.getRevComentario());
            statement.setDouble(4, review.getRevNota());
            Date revData = Date.valueOf(review.getRevData());
            statement.setDate(5, revData);
            statement.executeUpdate();
        } catch (SQLException e) {
            // Adicione um log mais detalhado
            System.err.println("Erro ao criar review: " + e.getMessage());
            throw e;
        }
    }
    

    public Review read(int revIdJogo, int revIdCliente) throws SQLException {
        Review review = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, revIdJogo);
            statement.setInt(2, revIdCliente);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    review = new Review();
                    review.setRevIdJogo(result.getInt("rev_id_jogo"));
                    review.setRevIdCliente(result.getInt("rev_id_cliente"));
                    review.setRevComentario(result.getString("rev_comentario"));
                    review.setRevNota(result.getDouble("rev_nota"));
                    review.setRevData(result.getString("rev_data"));
                }
            }
        }
        return review;
    }

    public void update(Review review) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, review.getRevComentario());
            statement.setDouble(2, review.getRevNota());
            statement.setString(3, review.getRevData());
            statement.setInt(4, review.getRevIdJogo());
            statement.setInt(5, review.getRevIdCliente());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar review: Review não encontrado.");
            }
        }
    }

    public void delete(int revIdJogo, int revIdCliente) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, revIdJogo);
            statement.setInt(2, revIdCliente);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: Review não encontrado.");
            }
        }
    }

    public List<Review> all() throws SQLException {
        List<Review> reviewList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Review review = new Review();
                review.setRevIdJogo(result.getInt("rev_id_jogo"));
                review.setRevIdCliente(result.getInt("rev_id_cliente"));
                review.setRevComentario(result.getString("rev_comentario"));
                review.setRevNota(result.getDouble("rev_nota"));
                review.setRevData(result.getString("rev_data"));

                reviewList.add(review);
            }
        }
        return reviewList;
    }

    public List<Review> findByJogoId(int jogoId) throws SQLException {
        List<Review> reviewList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_JOGO_QUERY)) {
            statement.setInt(1, jogoId);
            
            System.out.println("Executando query: " + FIND_BY_JOGO_QUERY + " com jogoId: " + jogoId);
            
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Review review = new Review();
                    review.setRevIdJogo(result.getInt("rev_id_jogo"));
                    review.setRevIdCliente(result.getInt("rev_id_cliente"));
                    review.setRevComentario(result.getString("rev_comentario"));
                    review.setRevNota(result.getDouble("rev_nota"));
                    review.setRevData(result.getString("rev_data"));
                    
                    // Adicionar nome do cliente se disponível
                    String nomeCliente = result.getString("nome_cliente");
                    if (nomeCliente != null) {
                        review.setNomeCliente(nomeCliente);
                    }

                    reviewList.add(review);
                    System.out.println("Review encontrada: " + review.getRevComentario());
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar reviews por jogo: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        System.out.println("Total de reviews encontradas: " + reviewList.size());
        return reviewList;
    }
}
