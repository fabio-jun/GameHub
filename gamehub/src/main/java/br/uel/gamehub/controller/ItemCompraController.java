package br.uel.gamehub.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uel.gamehub.dao.ItemCompraDAO;
import br.uel.gamehub.model.ItemCompra;

@RestController
@RequestMapping("/itens-compra")
public class ItemCompraController {

    private final ItemCompraDAO itemCompraDAO;

    public ItemCompraController(ItemCompraDAO itemCompraDAO) {
        this.itemCompraDAO = itemCompraDAO;
    }

    @PostMapping
    public ResponseEntity<ItemCompra> createItemCompra(@RequestBody ItemCompra itemCompra) {
        try {
            itemCompraDAO.create(itemCompra);
            return ResponseEntity.status(HttpStatus.CREATED).body(itemCompra);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemCompra> getItemCompra(@PathVariable int id) {
        try {
            ItemCompra itemCompra = itemCompraDAO.read(id);
            if (itemCompra != null) {
                return ResponseEntity.ok(itemCompra);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemCompra> updateItemCompra(@PathVariable int id, @RequestBody ItemCompra itemCompra) {
        try {
            itemCompra.setIdItemComp(id);
            itemCompraDAO.update(itemCompra);
            return ResponseEntity.ok(itemCompra);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemCompra(@PathVariable int id) {
        try {
            itemCompraDAO.delete(id);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ItemCompra>> getAllItensCompra() {
        try {
            List<ItemCompra> itemCompraList = itemCompraDAO.all();
            return ResponseEntity.ok(itemCompraList);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
