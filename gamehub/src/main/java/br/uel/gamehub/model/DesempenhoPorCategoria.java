package br.uel.gamehub.model;

public class DesempenhoPorCategoria {
    private String nomeCategoria;
    private int totalVendido;

    public DesempenhoPorCategoria(String nomeCategoria, int totalVendido) {
        this.nomeCategoria = nomeCategoria;
        this.totalVendido = totalVendido;
    }

    // Getters e setters
    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public int getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(int totalVendido) {
        this.totalVendido = totalVendido;
    }
}
