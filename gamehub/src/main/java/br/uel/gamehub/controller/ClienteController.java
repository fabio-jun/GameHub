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

import br.uel.gamehub.dao.ClienteDAO;
import br.uel.gamehub.model.Cliente;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteDAO clienteDAO;

    public ClienteController(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            return ResponseEntity.ok(clientes);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable int id) {
        try {
            Cliente c = clienteDAO.buscarPorId(id);
            if (c == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(c);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Cliente> createCliente(@RequestBody Cliente cliente) {
        try {
            clienteDAO.salvar(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable int id, @RequestBody Cliente cliente) {
        try {
            cliente.setIdCliente(id);
            clienteDAO.atualizar(cliente);
            return ResponseEntity.ok(cliente);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable int id) {
        try {
            clienteDAO.remover(id);
            return ResponseEntity.noContent().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String senha) {
        try {
            Cliente c = clienteDAO.buscarPorEmailSenha(email, senha);
            if (c == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos.");
            }
            return ResponseEntity.ok(c);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
