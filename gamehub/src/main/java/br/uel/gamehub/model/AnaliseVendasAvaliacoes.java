package br.uel.gamehub.model;

public class AnaliseVendasAvaliacoes {
    private String jogo;
    private double mediaAvaliacao;
    private int totalVendido;

    public AnaliseVendasAvaliacoes(String jogo, double mediaAvaliacao, int totalVendido) {
        this.jogo = jogo;
        this.mediaAvaliacao = mediaAvaliacao;
        this.totalVendido = totalVendido;
    }

    // Getters e setters
    public String getJogo() {
        return jogo;
    }

    public void setJogo(String jogo) {
        this.jogo = jogo;
    }

    public double getMediaAvaliacao() {
        return mediaAvaliacao;
    }

    public void setMediaAvaliacao(double mediaAvaliacao) {
        this.mediaAvaliacao = mediaAvaliacao;
    }

    public int getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(int totalVendido) {
        this.totalVendido = totalVendido;
    }
}
