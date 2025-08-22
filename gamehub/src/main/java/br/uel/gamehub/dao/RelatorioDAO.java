package br.uel.gamehub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.uel.gamehub.model.AnaliseVendasAvaliacoes;
import br.uel.gamehub.model.ComprasPorFaixaHoraria;
import br.uel.gamehub.model.DesempenhoPorCategoria;
import br.uel.gamehub.model.ProdutoComMenosVendas;
import br.uel.gamehub.model.ProdutoMaisVendido;
import br.uel.gamehub.model.VendasPorPeriodo;
import br.uel.gamehub.model.VendasPorPlataforma;

@Repository
public class RelatorioDAO {

    private static final Logger LOGGER = Logger.getLogger(RelatorioDAO.class.getName());

    private static final String PRODUTOS_MAIS_VENDIDOS_QUERY =
        "SELECT j.nome AS jogo, SUM(ic.item_qtd) AS total_vendido " +
        "FROM loja.item_compra ic " +
        "JOIN loja.jogo j ON ic.item_id_jogo = j.id_jogo " +
        "GROUP BY j.nome " +
        "ORDER BY total_vendido DESC;";

    private static final String VENDAS_POR_PERIODO_QUERY =
        "SELECT COUNT(c.id_comp) AS total_vendas, SUM(c.comp_preco) AS total_arrecadado " +
        "FROM loja.compra c " +
        "WHERE c.comp_data_hora BETWEEN CAST(? AS TIMESTAMP) AND CAST(? AS TIMESTAMP)";

    private static final String DESEMPENHO_POR_CATEGORIA_QUERY =
        "SELECT cat.nome_categoria, SUM(ic.item_qtd) AS total_vendido " +
        "FROM loja.item_compra ic " +
        "JOIN loja.jogo j ON ic.item_id_jogo = j.id_jogo " +
        "JOIN loja.categoria cat ON j.id_jogo = cat.cat_id_jogo " +
        "GROUP BY cat.nome_categoria " +
        "ORDER BY total_vendido DESC;";

    private static final String ANALISE_VENDAS_AVALIACOES_QUERY =
        "SELECT j.nome AS jogo, AVG(r.rev_nota) AS media_avaliacao, SUM(ic.item_qtd) AS total_vendido " +
        "FROM loja.jogo j " +
        "LEFT JOIN loja.review r ON j.id_jogo = r.rev_id_jogo " +
        "JOIN loja.item_compra ic ON j.id_jogo = ic.item_id_jogo " +
        "GROUP BY j.nome " +
        "ORDER BY total_vendido DESC;";

    private static final String COMPRAS_POR_FAIXA_HORARIA_QUERY =
        "SELECT " +
        "CASE " +
        "WHEN EXTRACT(HOUR FROM c.comp_data_hora) BETWEEN 6 AND 11 THEN 'Manhã' " +
        "WHEN EXTRACT(HOUR FROM c.comp_data_hora) BETWEEN 12 AND 17 THEN 'Tarde' " +
        "WHEN EXTRACT(HOUR FROM c.comp_data_hora) BETWEEN 18 AND 23 THEN 'Noite' " +
        "ELSE 'Madrugada' " +
        "END AS faixa_horaria, " +
        "COUNT(c.id_comp) AS total_vendas " +
        "FROM loja.compra c " +
        "GROUP BY faixa_horaria " +
        "ORDER BY faixa_horaria;";

    private static final String PRODUTOS_COM_MENOS_VENDAS_QUERY =
        "SELECT j.nome AS jogo, SUM(ic.item_qtd) AS total_vendido " +
        "FROM loja.jogo j " +
        "LEFT JOIN loja.item_compra ic ON j.id_jogo = ic.item_id_jogo " +
        "GROUP BY j.nome " +
        "HAVING SUM(ic.item_qtd) > 0" +
        "ORDER BY total_vendido ASC " +
        "LIMIT 5;";

    private static final String VENDAS_POR_PLATAFORMA_QUERY =
        "SELECT p.nome_plataforma, SUM(ic.item_qtd) AS total_vendido " +
        "FROM loja.item_compra ic " +
        "JOIN loja.jogo j ON ic.item_id_jogo = j.id_jogo " +
        "JOIN loja.plataforma p ON j.id_jogo = p.plat_id_jogo " +
        "GROUP BY p.nome_plataforma " +
        "ORDER BY total_vendido DESC;";

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public List<ProdutoMaisVendido> getProdutosMaisVendidos() {
        List<ProdutoMaisVendido> produtos = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(PRODUTOS_MAIS_VENDIDOS_QUERY);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                String jogo = result.getString("jogo");
                int totalVendido = result.getInt("total_vendido");
                produtos.add(new ProdutoMaisVendido(jogo, totalVendido));
            }
        } catch (SQLException e) {
            LOGGER.severe(() -> "Erro ao buscar produtos mais vendidos: " + e.getMessage());
        }
        return produtos;
    }

    public VendasPorPeriodo getVendasPorPeriodo(String dataInicial, String dataFinal) {
        VendasPorPeriodo vendas = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(VENDAS_POR_PERIODO_QUERY)) {
             
            statement.setString(1, dataInicial);
            statement.setString(2, dataFinal);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    int totalVendas = result.getInt("total_vendas");
                    double totalArrecadado = result.getDouble("total_arrecadado");
                    vendas = new VendasPorPeriodo(totalVendas, totalArrecadado);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(() -> "Erro ao buscar vendas por período: " + e.getMessage());
        }
        return vendas;
    }

    public List<DesempenhoPorCategoria> getDesempenhoPorCategoria() {
        List<DesempenhoPorCategoria> categorias = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DESEMPENHO_POR_CATEGORIA_QUERY);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                String nomeCategoria = result.getString("nome_categoria");
                int totalVendido = result.getInt("total_vendido");
                categorias.add(new DesempenhoPorCategoria(nomeCategoria, totalVendido));
            }
        } catch (SQLException e) {
            LOGGER.severe(() -> "Erro ao buscar desempenho por categoria: " + e.getMessage());
        }
        return categorias;
    }

    public List<AnaliseVendasAvaliacoes> getAnaliseVendasAvaliacoes() {
        List<AnaliseVendasAvaliacoes> analises = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ANALISE_VENDAS_AVALIACOES_QUERY);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                String jogo = result.getString("jogo");
                double mediaAvaliacao = result.getDouble("media_avaliacao");
                int totalVendido = result.getInt("total_vendido");
                analises.add(new AnaliseVendasAvaliacoes(jogo, mediaAvaliacao, totalVendido));
            }
        } catch (SQLException e) {
            LOGGER.severe(() -> "Erro ao buscar análise de vendas e avaliações: " + e.getMessage());
        }
        return analises;
    }

    public List<ComprasPorFaixaHoraria> getComprasPorFaixaHoraria() {
        List<ComprasPorFaixaHoraria> faixas = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(COMPRAS_POR_FAIXA_HORARIA_QUERY);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                String faixaHoraria = result.getString("faixa_horaria");
                int totalVendas = result.getInt("total_vendas");
                faixas.add(new ComprasPorFaixaHoraria(faixaHoraria, totalVendas));
            }
        } catch (SQLException e) {
            LOGGER.severe(() -> "Erro ao buscar compras por faixa horária: " + e.getMessage());
        }
        return faixas;
    }

    public List<ProdutoComMenosVendas> getProdutosComMenosVendas() {
        List<ProdutoComMenosVendas> produtos = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(PRODUTOS_COM_MENOS_VENDAS_QUERY);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                String jogo = result.getString("jogo");
                int totalVendido = result.getInt("total_vendido");
                produtos.add(new ProdutoComMenosVendas(jogo, totalVendido));
            }
        } catch (SQLException e) {
            LOGGER.severe(() -> "Erro ao buscar produtos com menos vendas: " + e.getMessage());
        }
        return produtos;
    }

    public List<VendasPorPlataforma> getVendasPorPlataforma() {
        List<VendasPorPlataforma> plataformas = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(VENDAS_POR_PLATAFORMA_QUERY);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                String nomePlataforma = result.getString("nome_plataforma");
                int totalVendido = result.getInt("total_vendido");
                plataformas.add(new VendasPorPlataforma(nomePlataforma, totalVendido));
            }
        } catch (SQLException e) {
            LOGGER.severe(() -> "Erro ao buscar vendas por plataforma: " + e.getMessage());
        }
        return plataformas;
    }
}
