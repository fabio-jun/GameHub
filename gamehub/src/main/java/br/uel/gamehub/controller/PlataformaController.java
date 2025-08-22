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

import br.uel.gamehub.dao.PlataformaDAO;
import br.uel.gamehub.model.Plataforma;

@RestController
@RequestMapping("/plataformas")
public class PlataformaController {

    private final PlataformaDAO plataformaDAO;

    public PlataformaController(PlataformaDAO plataformaDAO) {
        this.plataformaDAO = plataformaDAO;
    }

    @PostMapping
    public ResponseEntity<Plataforma> createPlataforma(@RequestBody Plataforma plataforma) {
        try {
            plataformaDAO.create(plataforma);
            return ResponseEntity.status(HttpStatus.CREATED).body(plataforma);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{nome}/{idJogo}")
    public ResponseEntity<Plataforma> getPlataforma(@PathVariable String nome, @PathVariable int idJogo) {
        try {
            Plataforma plataforma = plataformaDAO.read(nome, idJogo);
            if (plataforma != null) {
                return ResponseEntity.ok(plataforma);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{idJogo}")
    public ResponseEntity<Plataforma> updatePlataforma(@PathVariable int idJogo, @RequestBody Plataforma plataforma) {
        try {
            plataforma.setPlatIdJogo(idJogo);
            plataformaDAO.update(plataforma);
            return ResponseEntity.ok(plataforma);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @DeleteMapping("/{nome}/{idJogo}")
    public ResponseEntity<Void> deletePlataforma(@PathVariable String nome, @PathVariable int idJogo) {
        try {
            plataformaDAO.delete(nome, idJogo);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Plataforma>> getAllPlataformas() {
        try {
            List<Plataforma> plataformaList = plataformaDAO.all();
            return ResponseEntity.ok(plataformaList);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
