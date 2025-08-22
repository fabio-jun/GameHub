package br.uel.gamehub.model;

public class Categoria {

    private String nomeCategoria;

    private int idJogo;
    
    public String getNomeCategoria() {
        return nomeCategoria;
    }
    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
    public int getIdJogo() {
        return idJogo;
    }
    public void setIdJogo(int idJogo) {
        this.idJogo = idJogo;
    }
}