package br.uel.gamehub.model;

public class ProdutoComMenosVendas {
    private String jogo;
    private int totalVendido;

    public ProdutoComMenosVendas(String jogo, int totalVendido) {
        this.jogo = jogo;
        this.totalVendido = totalVendido;
    }

    public String getJogo() {
        return jogo;
    }

    public void setJogo(String jogo) {
        this.jogo = jogo;
    }

    public int getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(int totalVendido) {
        this.totalVendido = totalVendido;
    }
}
