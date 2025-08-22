package br.uel.gamehub.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.uel.gamehub.dao.DesenvolvedorDAO;
import br.uel.gamehub.model.Desenvolvedor;

@RestController
@RequestMapping("/desenvolvedores")
public class DesenvolvedorController {

    private final DesenvolvedorDAO desenvolvedorDAO;

    public DesenvolvedorController(DesenvolvedorDAO desenvolvedorDAO) {
        this.desenvolvedorDAO = desenvolvedorDAO;
    }

    @GetMapping
    public ResponseEntity<List<Desenvolvedor>> getAllDesenvolvedores() {
        try {
            List<Desenvolvedor> desenvolvedores = desenvolvedorDAO.all();
            return ResponseEntity.ok(desenvolvedores);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{nome}/{idJogo}")
    public ResponseEntity<Desenvolvedor> getDesenvolvedor(
            @PathVariable String nome, 
            @PathVariable int idJogo) {
        try {
            Desenvolvedor desenvolvedor = desenvolvedorDAO.read(nome, idJogo);
            if (desenvolvedor != null) {
                return ResponseEntity.ok(desenvolvedor);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{idJogo}")
    public ResponseEntity<Desenvolvedor> updateDesenvolvedor(
            @PathVariable int idJogo, 
            @RequestBody Desenvolvedor desenvolvedor) {
        try {
            desenvolvedor.setDesIdJogo(idJogo);
            desenvolvedorDAO.update(desenvolvedor);
            return ResponseEntity.ok(desenvolvedor);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{nome}/{idJogo}")
    public ResponseEntity<Void> deleteDesenvolvedor(
            @PathVariable String nome, 
            @PathVariable int idJogo) {
        try {
            desenvolvedorDAO.delete(nome, idJogo);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
