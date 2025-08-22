package br.uel.gamehub.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.uel.gamehub.dao.RelatorioDAO;
import br.uel.gamehub.model.AnaliseVendasAvaliacoes;
import br.uel.gamehub.model.ComprasPorFaixaHoraria;
import br.uel.gamehub.model.DesempenhoPorCategoria;
import br.uel.gamehub.model.ProdutoComMenosVendas;
import br.uel.gamehub.model.ProdutoMaisVendido;
import br.uel.gamehub.model.VendasPorPeriodo;
import br.uel.gamehub.model.VendasPorPlataforma;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioDAO relatorioDAO;

    public RelatorioController(RelatorioDAO relatorioDAO) {
        this.relatorioDAO = relatorioDAO;
    }

    @GetMapping("/produtos-mais-vendidos")
    public ResponseEntity<List<ProdutoMaisVendido>> getProdutosMaisVendidos() {
        List<ProdutoMaisVendido> produtosMaisVendidos = relatorioDAO.getProdutosMaisVendidos();
        return ResponseEntity.ok(produtosMaisVendidos);
    }

    @GetMapping("/vendas-por-periodo")
    public ResponseEntity<VendasPorPeriodo> getVendasPorPeriodo(@RequestParam String dataInicio, @RequestParam String dataFim) {
        VendasPorPeriodo vendas = relatorioDAO.getVendasPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/desempenho-por-categoria")
    public ResponseEntity<List<DesempenhoPorCategoria>> getDesempenhoPorCategoria() {
        List<DesempenhoPorCategoria> desempenho = relatorioDAO.getDesempenhoPorCategoria();
        return ResponseEntity.ok(desempenho);
    }

    @GetMapping("/analise-vendas-avaliacoes")
    public ResponseEntity<List<AnaliseVendasAvaliacoes>> getAnaliseVendasAvaliacoes() {
        List<AnaliseVendasAvaliacoes> analise = relatorioDAO.getAnaliseVendasAvaliacoes();
        return ResponseEntity.ok(analise);
    }

    @GetMapping("/compras-por-faixa-horaria")
    public ResponseEntity<List<ComprasPorFaixaHoraria>> getComprasPorFaixaHoraria() {
        List<ComprasPorFaixaHoraria> compras = relatorioDAO.getComprasPorFaixaHoraria();
        return ResponseEntity.ok(compras);
    }

    @GetMapping("/produtos-menos-vendidos")
    public ResponseEntity<List<ProdutoComMenosVendas>> getProdutosMenosVendidos() {
        List<ProdutoComMenosVendas> produtosMenosVendidos = relatorioDAO.getProdutosComMenosVendas();
        return ResponseEntity.ok(produtosMenosVendidos);
    }

    @GetMapping("/vendas-por-plataforma")
    public ResponseEntity<List<VendasPorPlataforma>> getVendasPorPlataforma() {
        List<VendasPorPlataforma> vendasPlataforma = relatorioDAO.getVendasPorPlataforma();
        return ResponseEntity.ok(vendasPlataforma);
    }
}
