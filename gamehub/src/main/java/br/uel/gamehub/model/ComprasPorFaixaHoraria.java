package br.uel.gamehub.model;

public class ComprasPorFaixaHoraria {
    private String faixaHoraria;
    private int totalVendas;

    public ComprasPorFaixaHoraria(String faixaHoraria, int totalVendas) {
        this.faixaHoraria = faixaHoraria;
        this.totalVendas = totalVendas;
    }

    // Getters e setters
    public String getFaixaHoraria() {
        return faixaHoraria;
    }

    public void setFaixaHoraria(String faixaHoraria) {
        this.faixaHoraria = faixaHoraria;
    }

    public int getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(int totalVendas) {
        this.totalVendas = totalVendas;
    }
}
