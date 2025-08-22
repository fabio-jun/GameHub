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

import br.uel.gamehub.dao.CarrinhoDAO;
import br.uel.gamehub.model.Carrinho;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoDAO carrinhoDAO;

    public CarrinhoController(CarrinhoDAO carrinhoDAO) {
        this.carrinhoDAO = carrinhoDAO;
    }

    @GetMapping
    public ResponseEntity<List<Carrinho>> getAllCarrinho() {
        try {
            List<Carrinho> carrinhoList = carrinhoDAO.all();
            if (carrinhoList.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(carrinhoList);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/cliente/{idCliente}")
    public ResponseEntity<Carrinho> adicionarItemAoCarrinho(@PathVariable int idCliente, @RequestBody Carrinho carrinho) {
        try {
            carrinho.setIdCliente(idCliente);
            carrinhoDAO.create(carrinho);
        return ResponseEntity.status(HttpStatus.CREATED).body(carrinho);
    } catch (SQLException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


    @GetMapping("/{idCliente}")
    public ResponseEntity<List<Carrinho>> getCarrinhoByCliente(@PathVariable int idCliente) {
        try {
            List<Carrinho> carrinhoList = carrinhoDAO.findByClienteId(idCliente);
            if (carrinhoList.isEmpty()) {
                return ResponseEntity.noContent().build(); 
            }
        return ResponseEntity.ok(carrinhoList);
    } catch (SQLException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


    @PutMapping("/{idCliente}/{idJogo}")
    public ResponseEntity<Carrinho> atualizarQuantidadeItem(@PathVariable int idCliente, @PathVariable int idJogo, @RequestBody Carrinho carrinho) {
        try {
            carrinho.setIdCliente(idCliente);
            carrinho.setIdJogo(idJogo);
            carrinhoDAO.update(carrinho);
            return ResponseEntity.ok(carrinho);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{idCliente}/{idJogo}")
    public ResponseEntity<Void> removerItemDoCarrinho(@PathVariable int idCliente, @PathVariable int idJogo) {
        try {
            carrinhoDAO.delete(idCliente, idJogo);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
