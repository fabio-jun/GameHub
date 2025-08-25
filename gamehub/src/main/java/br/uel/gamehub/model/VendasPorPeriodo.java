package br.uel.gamehub.model;

public class VendasPorPeriodo {
    private int totalVendas;
    private double totalArrecadado;

    public VendasPorPeriodo(int totalVendas, double totalArrecadado) {
        this.totalVendas = totalVendas;
        this.totalArrecadado = totalArrecadado;
    }

    public int getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(int totalVendas) {
        this.totalVendas = totalVendas;
    }

    public double getTotalArrecadado() {
        return totalArrecadado;
    }

    public void setTotalArrecadado(double totalArrecadado) {
        this.totalArrecadado = totalArrecadado;
    }
}
