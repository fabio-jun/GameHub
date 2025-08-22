package br.uel.gamehub.model;

public class ItemCompra {
    private int idItemComp;
    private int itemIdComp;
    private int itemIdJogo;
    private String itemNomeJogo;
    private double itemPrecoJogo;
    private int itemQtd;

    public int getIdItemComp() { return idItemComp; }
    public void setIdItemComp(int idItemComp) { this.idItemComp = idItemComp; }
    public int getItemIdComp() { return itemIdComp; }
    public void setItemIdComp(int itemIdComp) { this.itemIdComp = itemIdComp; }
    public int getItemIdJogo() { return itemIdJogo; }
    public void setItemIdJogo(int itemIdJogo) { this.itemIdJogo = itemIdJogo; }
    public String getItemNomeJogo() { return itemNomeJogo; }
    public void setItemNomeJogo(String itemNomeJogo) { this.itemNomeJogo = itemNomeJogo; }
    public double getItemPrecoJogo() { return itemPrecoJogo; }
    public void setItemPrecoJogo(double itemPrecoJogo) { this.itemPrecoJogo = itemPrecoJogo; }
    public int getItemQtd() { return itemQtd; }
    public void setItemQtd(int itemQtd) { this.itemQtd = itemQtd; }
}
