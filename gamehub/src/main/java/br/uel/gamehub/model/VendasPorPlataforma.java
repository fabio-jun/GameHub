package br.uel.gamehub.model;

public class VendasPorPlataforma {
    private String nomePlataforma;
    private int totalVendido;

    public VendasPorPlataforma(String nomePlataforma, int totalVendido) {
        this.nomePlataforma = nomePlataforma;
        this.totalVendido = totalVendido;
    }

    // Getters e setters
    public String getNomePlataforma() {
        return nomePlataforma;
    }

    public void setNomePlataforma(String nomePlataforma) {
        this.nomePlataforma = nomePlataforma;
    }

    public int getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(int totalVendido) {
        this.totalVendido = totalVendido;
    }
}
