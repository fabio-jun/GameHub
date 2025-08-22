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

import br.uel.gamehub.model.Plataforma;

@Repository
public class PlataformaDAO {

    private static final String CREATE_QUERY =
        "INSERT INTO loja.plataforma (nome_plataforma, plat_id_jogo) " +
        "VALUES (?, ?);";

    private static final String READ_QUERY =
        "SELECT nome_plataforma, plat_id_jogo " +
        "FROM loja.plataforma WHERE nome_plataforma = ? AND plat_id_jogo = ?;";

    private static final String UPDATE_QUERY =
        "UPDATE loja.plataforma " +
        "SET nome_plataforma = ? WHERE plat_id_jogo = ?;";

    private static final String DELETE_QUERY =
        "DELETE FROM loja.plataforma WHERE nome_plataforma = ? AND plat_id_jogo = ?;";

    private static final String ALL_QUERY =
        "SELECT nome_plataforma, plat_id_jogo " +
        "FROM loja.plataforma;";

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void create(Plataforma plataforma) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, plataforma.getNomePlataforma());
            statement.setInt(2, plataforma.getPlatIdJogo());
            statement.executeUpdate();
        }
    }

    public Plataforma read(String nomePlataforma, int platIdJogo) throws SQLException {
        Plataforma plataforma = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setString(1, nomePlataforma);
            statement.setInt(2, platIdJogo);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    plataforma = new Plataforma();
                    plataforma.setNomePlataforma(result.getString("nome_plataforma"));
                    plataforma.setPlatIdJogo(result.getInt("plat_id_jogo"));
                }
            }
        }
        return plataforma;
    }

    public void update(Plataforma plataforma) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, plataforma.getNomePlataforma());
            statement.setInt(2, plataforma.getPlatIdJogo());
            statement.executeUpdate();
        }
    }

    public void delete(String nomePlataforma, int platIdJogo) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setString(1, nomePlataforma);
            statement.setInt(2, platIdJogo);
            statement.executeUpdate();
        }
    }

    public List<Plataforma> all() throws SQLException {
        List<Plataforma> plataformaList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Plataforma plataforma = new Plataforma();
                plataforma.setNomePlataforma(result.getString("nome_plataforma"));
                plataforma.setPlatIdJogo(result.getInt("plat_id_jogo"));
                plataformaList.add(plataforma);
            }
        }
        return plataformaList;
    }
}
