package br.uel.gamehub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.uel.gamehub.model.Jogo;

@Repository
public class JogoDAO implements DAO<Jogo> {
    
    private static final String CREATE_QUERY =
            "INSERT INTO loja.jogo (nome, descricao, preco, lancamento, nota, image_path) " +
            "VALUES (?, ?, ?, ?, ?, ?);";
                            
    private static final String ALL_QUERY =
            "SELECT id_jogo, nome, descricao, preco, lancamento, nota, image_path " +
            "FROM loja.jogo " +
            "ORDER BY id_jogo;";    

    private static final String READ_QUERY =
            "SELECT j.id_jogo, j.nome, j.descricao, j.preco, j.lancamento, j.nota, j.image_path, " +
            "c.nome_categoria, p.nome_plataforma, d.nome_desenvolvedor " +
            "FROM loja.jogo j " +
            "LEFT JOIN loja.categoria c ON c.cat_id_jogo = j.id_jogo " +
            "LEFT JOIN loja.plataforma p ON p.plat_id_jogo = j.id_jogo " +
            "LEFT JOIN loja.desenvolvedor d ON d.des_id_jogo = j.id_jogo " +
            "WHERE j.id_jogo = ?;";          

    private static final String UPDATE_QUERY =
            "UPDATE loja.jogo " +
            "SET nome = ?, descricao = ?, preco = ?, lancamento = ?, nota = ?, image_path = ? " +
            "WHERE id_jogo = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM loja.jogo " +
            "WHERE id_jogo = ?;";

    private static final String SEARCH_QUERY =
            "SELECT id_jogo, nome, descricao, preco, lancamento, nota, image_path " +
            "FROM loja.jogo WHERE nome ILIKE ? OR descricao ILIKE ?;";  // ILIKE faz a busca ser case-insensitive

    private static final String SEARCH_BY_DESENVOLVEDOR_QUERY = 
            "SELECT j.id_jogo, j.nome, j.descricao, j.preco, j.lancamento, j.nota, j.image_path, " +
            "c.nome_categoria, p.nome_plataforma, d.nome_desenvolvedor " +
            "FROM loja.categoria c " +
            "JOIN loja.jogo j ON c.cat_id_jogo = j.id_jogo " +
            "JOIN loja.plataforma p ON c.cat_id_jogo = p.plat_id_jogo " +
            "JOIN loja.desenvolvedor d ON c.cat_id_jogo = d.des_id_jogo " +
            "WHERE d.nome_desenvolvedor = ?";

    private static final String SEARCH_BY_PLATAFORMA_QUERY =
            "SELECT j.id_jogo, j.nome, j.descricao, j.preco, j.lancamento, j.nota, j.image_path, p.nome_plataforma " +
            "FROM loja.jogo j " +
            "JOIN loja.plataforma p ON j.id_jogo = p.plat_id_jogo " +
            "WHERE p.nome_plataforma = ?";

    private static final String SEARCH_BY_CATEGORIA_QUERY = 
            "SELECT j.id_jogo, j.nome, j.descricao, j.preco, j.lancamento, j.nota, j.image_path, " +
            "c.nome_categoria, p.nome_plataforma, d.nome_desenvolvedor " +
            "FROM loja.categoria c " +
            "JOIN loja.jogo j ON c.cat_id_jogo = j.id_jogo " +
            "JOIN loja.plataforma p ON c.cat_id_jogo = p.plat_id_jogo " +
            "JOIN loja.desenvolvedor d ON c.cat_id_jogo = d.des_id_jogo " +
            "WHERE c.nome_categoria = ?";

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void create(Jogo jogo) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, jogo.getNome());
            statement.setString(2, jogo.getDescricao());
            statement.setBigDecimal(3, jogo.getPreco());
            statement.setDate(4, jogo.getLancamento());
            statement.setBigDecimal(5, jogo.getNota());
            statement.setString(6, jogo.getImagePath());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().contains("uk_jogo_nome")) {
                throw new SQLException("Erro ao inserir jogo: Nome do jogo já existente.");
            } else if (ex.getMessage().contains("uk_jogo_image_path")) {
                throw new SQLException("Erro ao inserir jogo: Imagem já existente.");
            } else if (ex.getMessage().contains("ck_jogo_preco")) {
                throw new SQLException("Erro ao inserir jogo: O preço do jogo deve ser maior ou igual a 0.00.");
            } else if (ex.getMessage().contains("ck_jogo_nota")) {
                throw new SQLException("Erro ao inserir jogo: A nota deve estar entre 0.00 e 10.0.");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao inserir jogo: Pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao inserir jogo.");
            }
        }
    }

    @Override
    public Jogo read(Integer id) throws SQLException {
        Jogo jogo = new Jogo();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    jogo.setIdJogo(id);
                    jogo.setNome(result.getString("nome"));
                    jogo.setDescricao(result.getString("descricao"));
                    jogo.setPreco(result.getBigDecimal("preco"));
                    jogo.setLancamento(result.getDate("lancamento"));
                    jogo.setNota(result.getBigDecimal("nota"));
                    jogo.setImagePath(result.getString("image_path"));
                    jogo.setCategoria(result.getString("nome_categoria"));
                    jogo.setPlataforma(result.getString("nome_plataforma"));
                    jogo.setDesenvolvedor(result.getString("nome_desenvolvedor"));
                } else {
                    throw new SQLException("Erro ao visualizar: Jogo não encontrado.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao visualizar: Jogo não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao visualizar jogo.");
            }
        }
        return jogo;
    }

    @Override
    public void update(Jogo jogo) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, jogo.getNome());
            statement.setString(2, jogo.getDescricao());
            statement.setBigDecimal(3, jogo.getPreco());
            statement.setDate(4, jogo.getLancamento());
            statement.setBigDecimal(5, jogo.getNota());
            statement.setString(6, jogo.getImagePath());
            statement.setInt(7, jogo.getIdJogo());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar jogo: Jogo não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao editar jogo: Jogo não encontrado.")) {
                throw ex;
            } else if (ex.getMessage().contains("uk_jogo_nome")) {
                throw new SQLException("Erro ao editar jogo: Nome do jogo já existente.");
            } else if (ex.getMessage().contains("uk_jogo_image_path")) {
                throw new SQLException("Erro ao editar jogo: Imagem já existente.");
            } else if (ex.getMessage().contains("ck_jogo_preco")) {
                throw new SQLException("Erro ao editar jogo: O preço do jogo deve ser maior ou igual a 0.00.");
            } else if (ex.getMessage().contains("ck_jogo_nota")) {
                throw new SQLException("Erro ao editar jogo: A nota deve estar entre 0.00 e 10.0.");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar jogo: Pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao editar jogo.");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: Jogo não encontrado.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            if (ex.getMessage().equals("Erro ao excluir: Jogo não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir jogo.");
            }
        }
    }

    @Override
    public List<Jogo> all() throws SQLException {
        List<Jogo> jogoList = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Jogo jogo = new Jogo();
                jogo.setIdJogo(result.getInt("id_jogo"));
                jogo.setNome(result.getString("nome"));
                jogo.setDescricao(result.getString("descricao"));
                jogo.setPreco(result.getBigDecimal("preco"));
                jogo.setLancamento(result.getDate("lancamento"));
                jogo.setNota(result.getBigDecimal("nota"));
                jogo.setImagePath(result.getString("image_path"));

                jogoList.add(jogo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

            throw new SQLException("Erro ao listar jogos.");
        }
        return jogoList;
    }

    public List<Jogo> searchByKeyword(String keyword) throws SQLException {
        keyword = keyword.trim().replaceAll("\\r?\\n", "");

        List<Jogo> jogos = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_QUERY)) {
            String searchPattern = "%" + keyword + "%";
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Jogo jogo = new Jogo();
                    jogo.setIdJogo(resultSet.getInt("id_jogo"));
                    jogo.setNome(resultSet.getString("nome"));
                    jogo.setDescricao(resultSet.getString("descricao"));
                    jogo.setPreco(resultSet.getBigDecimal("preco"));
                    jogo.setLancamento(resultSet.getDate("lancamento"));
                    jogo.setNota(resultSet.getBigDecimal("nota"));
                    jogo.setImagePath(resultSet.getString("image_path"));
                    jogos.add(jogo);
                }
            }
        }
        return jogos;
    }

    public List<Jogo> searchByDesenvolvedor(String nomeDesenvolvedor) throws SQLException {
        List<Jogo> jogos = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_BY_DESENVOLVEDOR_QUERY)) {
            
            Logger.getLogger(JogoDAO.class.getName()).log(Level.INFO, "Buscando jogos para desenvolvedor: {0}", nomeDesenvolvedor);
            
            statement.setString(1, nomeDesenvolvedor);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Jogo jogo = new Jogo();
                    jogo.setIdJogo(resultSet.getInt("id_jogo"));
                    jogo.setNome(resultSet.getString("nome"));
                    jogo.setDescricao(resultSet.getString("descricao"));
                    jogo.setPreco(resultSet.getBigDecimal("preco"));
                    jogo.setLancamento(resultSet.getDate("lancamento"));
                    jogo.setNota(resultSet.getBigDecimal("nota"));
                    jogo.setImagePath(resultSet.getString("image_path"));
                    
                    String categoria = resultSet.getString("nome_categoria");
                    String plataforma = resultSet.getString("nome_plataforma");
                    String desenvolvedor = resultSet.getString("nome_desenvolvedor");

                    Logger.getLogger(JogoDAO.class.getName()).log(Level.INFO, "Jogo encontrado: {0}, Categoria: {1}, Plataforma: {2}, Desenvolvedor: {3}", 
                        new Object[]{jogo.getNome(), categoria, plataforma, desenvolvedor});
                    
                    jogos.add(jogo);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, "Erro ao buscar jogos por desenvolvedor: " + nomeDesenvolvedor, e);
            throw e;
        }
        return jogos;
    }


    public List<Jogo> searchByPlataforma(String nomePlataforma) throws SQLException {
        List<Jogo> jogos = new ArrayList<>();
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(SEARCH_BY_PLATAFORMA_QUERY)) {
            
            Logger.getLogger(JogoDAO.class.getName()).log(Level.INFO, "Buscando jogos para plataforma: {0}", nomePlataforma);

            statement.setString(1, nomePlataforma);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Jogo jogo = new Jogo();
                    jogo.setIdJogo(resultSet.getInt("id_jogo"));
                    jogo.setNome(resultSet.getString("nome"));
                    jogo.setDescricao(resultSet.getString("descricao"));
                    jogo.setPreco(resultSet.getBigDecimal("preco"));
                    jogo.setLancamento(resultSet.getDate("lancamento"));
                    jogo.setNota(resultSet.getBigDecimal("nota"));
                    jogo.setImagePath(resultSet.getString("image_path"));
                    jogos.add(jogo);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, "Erro ao buscar jogos por plataforma: " + nomePlataforma, e);
            throw e;
        }
        return jogos;
    }

    public List<Jogo> searchByCategoria(String nomeCategoria) throws SQLException {
        List<Jogo> jogos = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SEARCH_BY_CATEGORIA_QUERY)) {

            Logger.getLogger(JogoDAO.class.getName()).log(Level.INFO, "Buscando jogos para categoria: {0}", nomeCategoria);

            statement.setString(1, nomeCategoria);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    Logger.getLogger(JogoDAO.class.getName()).log(Level.INFO, "Nenhum jogo encontrado para a categoria: {0}", nomeCategoria);
                }
                while (resultSet.next()) {
                    Jogo jogo = new Jogo();
                    jogo.setIdJogo(resultSet.getInt("id_jogo"));
                    jogo.setNome(resultSet.getString("nome"));
                    jogo.setDescricao(resultSet.getString("descricao"));
                    jogo.setPreco(resultSet.getBigDecimal("preco"));
                    jogo.setLancamento(resultSet.getDate("lancamento"));
                    jogo.setNota(resultSet.getBigDecimal("nota"));
                    jogo.setImagePath(resultSet.getString("image_path"));

                    String categoria = resultSet.getString("nome_categoria");
                    String plataforma = resultSet.getString("nome_plataforma");
                    String desenvolvedor = resultSet.getString("nome_desenvolvedor");

                    Logger.getLogger(JogoDAO.class.getName()).log(Level.INFO, 
                        "Jogo encontrado: {0}, Categoria: {1}, Plataforma: {2}, Desenvolvedor: {3}", 
                        new Object[]{jogo.getNome(), categoria, plataforma, desenvolvedor});

                    jogos.add(jogo);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(JogoDAO.class.getName()).log(Level.SEVERE, "Erro ao buscar jogos por categoria: " + nomeCategoria, e);
            throw e;
        }
        return jogos;
    }

}