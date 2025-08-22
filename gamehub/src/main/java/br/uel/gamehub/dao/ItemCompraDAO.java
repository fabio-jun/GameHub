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

import br.uel.gamehub.model.ItemCompra;

@Repository
public class ItemCompraDAO {

    private static final String CREATE_QUERY =
        "INSERT INTO loja.item_compra (item_id_comp, item_id_jogo, item_nome_jogo, item_preco_jogo, item_qtd) " +
        "VALUES (?, ?, ?, ?, ?)";

    private static final String READ_QUERY =
        "SELECT id_item_comp, item_id_comp, item_id_jogo, item_nome_jogo, item_preco_jogo, item_qtd " +
        "FROM loja.item_compra WHERE id_item_comp = ?";

    private static final String UPDATE_QUERY =
        "UPDATE loja.item_compra SET item_id_comp = ?, item_id_jogo = ?, item_nome_jogo = ?, item_preco_jogo = ?, item_qtd = ? " +
        "WHERE id_item_comp = ?";

    private static final String DELETE_QUERY =
        "DELETE FROM loja.item_compra WHERE id_item_comp = ?";

    private static final String ALL_QUERY =
        "SELECT id_item_comp, item_id_comp, item_id_jogo, item_nome_jogo, item_preco_jogo, item_qtd " +
        "FROM loja.item_compra";

    private static final String FIND_ALL_BY_ITEM_ID_COMP_QUERY =
        "SELECT * FROM loja.item_compra WHERE item_id_comp = ?;";

    @Autowired
    private DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void create(ItemCompra itemCompra) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, itemCompra.getItemIdComp());
            statement.setInt(2, itemCompra.getItemIdJogo());
            statement.setString(3, itemCompra.getItemNomeJogo());
            statement.setDouble(4, itemCompra.getItemPrecoJogo());
            statement.setInt(5, itemCompra.getItemQtd());
            statement.executeUpdate();
        }
    }

    public ItemCompra read(int idItemComp) throws SQLException {
        ItemCompra itemCompra = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, idItemComp);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    itemCompra = new ItemCompra();
                    itemCompra.setIdItemComp(result.getInt("id_item_comp"));
                    itemCompra.setItemIdComp(result.getInt("item_id_comp"));
                    itemCompra.setItemIdJogo(result.getInt("item_id_jogo"));
                    itemCompra.setItemNomeJogo(result.getString("item_nome_jogo"));
                    itemCompra.setItemPrecoJogo(result.getDouble("item_preco_jogo"));
                    itemCompra.setItemQtd(result.getInt("item_qtd"));
                }
            }
        }
        return itemCompra;
    }

    public void update(ItemCompra itemCompra) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, itemCompra.getItemIdComp());
            statement.setInt(2, itemCompra.getItemIdJogo());
            statement.setString(3, itemCompra.getItemNomeJogo());
            statement.setDouble(4, itemCompra.getItemPrecoJogo());
            statement.setInt(5, itemCompra.getItemQtd());
            statement.setInt(6, itemCompra.getIdItemComp());
            statement.executeUpdate();
        }
    }

    public void delete(int idItemComp) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, idItemComp);
            statement.executeUpdate();
        }
    }

    public List<ItemCompra> all() throws SQLException {
        List<ItemCompra> itemCompraList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                ItemCompra itemCompra = new ItemCompra();
                itemCompra.setIdItemComp(result.getInt("id_item_comp"));
                itemCompra.setItemIdComp(result.getInt("item_id_comp"));
                itemCompra.setItemIdJogo(result.getInt("item_id_jogo"));
                itemCompra.setItemNomeJogo(result.getString("item_nome_jogo"));
                itemCompra.setItemPrecoJogo(result.getDouble("item_preco_jogo"));
                itemCompra.setItemQtd(result.getInt("item_qtd"));
                itemCompraList.add(itemCompra);
            }
        }
        return itemCompraList;
    }


    public List<ItemCompra> findAllByItemIdComp(int itemIdComp) throws SQLException {
        List<ItemCompra> itensCompra = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_ITEM_ID_COMP_QUERY)) {
            statement.setInt(1, itemIdComp);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ItemCompra itemCompra = new ItemCompra();
                    itemCompra.setIdItemComp(resultSet.getInt("id_item_comp"));
                    itemCompra.setItemIdComp(resultSet.getInt("item_id_comp"));
                    itemCompra.setItemIdJogo(resultSet.getInt("item_id_jogo"));
                    itemCompra.setItemNomeJogo(resultSet.getString("item_nome_jogo"));
                    itemCompra.setItemPrecoJogo(resultSet.getDouble("item_preco_jogo"));
                    itemCompra.setItemQtd(resultSet.getInt("item_qtd"));
                    itensCompra.add(itemCompra);
                }
            }
        }
        return itensCompra;
    }

}
