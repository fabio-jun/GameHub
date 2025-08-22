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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.uel.gamehub.dao.JogoDAO;
import br.uel.gamehub.model.Jogo;

@RestController
@RequestMapping("/jogos")
public class JogoController {

    private final JogoDAO jogoDAO;

    public JogoController(JogoDAO jogoDAO) {
        this.jogoDAO = jogoDAO;
    }

    @GetMapping
    public ResponseEntity<List<Jogo>> getAllJogos() {
        try {
            List<Jogo> jogos = jogoDAO.all();
            return ResponseEntity.ok(jogos);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jogo> getJogoById(@PathVariable int id) {
        try {
            Jogo jogo = jogoDAO.read(id);
            if (jogo == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(jogo);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Jogo> createJogo(@RequestBody Jogo jogo) {
        try {
            jogoDAO.create(jogo);
            return ResponseEntity.status(HttpStatus.CREATED).body(jogo);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Jogo> updateJogo(@PathVariable int id, @RequestBody Jogo jogo) {
        try {
            jogo.setIdJogo(id);
            jogoDAO.update(jogo);
            return ResponseEntity.ok(jogo);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJogo(@PathVariable int id) {
        try {
            jogoDAO.delete(id);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Jogo>> searchByKeyword(@RequestParam String keyword) {
        try {
            List<Jogo> jogos = jogoDAO.searchByKeyword(keyword);
            if (jogos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(jogos);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/desenvolvedor/{nomeDesenvolvedor}")
    public ResponseEntity<List<Jogo>> getJogosByDesenvolvedor(@PathVariable String nomeDesenvolvedor) {
        try {
            List<Jogo> jogos = jogoDAO.searchByDesenvolvedor(nomeDesenvolvedor);
            if (jogos.isEmpty()) {
                return ResponseEntity.noContent().build(); 
            }
            return ResponseEntity.ok(jogos); 
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/plataforma/{nomePlataforma}")
    public ResponseEntity<List<Jogo>> getJogosByPlataforma(@PathVariable String nomePlataforma) {
        try {
            List<Jogo> jogos = jogoDAO.searchByPlataforma(nomePlataforma);
            if (jogos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(jogos);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/categoria/{nomeCategoria}")
    public ResponseEntity<List<Jogo>> getJogosByCategoria(@PathVariable String nomeCategoria) {
        try {
            List<Jogo> jogos = jogoDAO.searchByCategoria(nomeCategoria);
            if (jogos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(jogos);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
