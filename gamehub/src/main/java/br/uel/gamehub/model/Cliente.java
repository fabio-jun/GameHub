package br.uel.gamehub.model;

public class Cliente {
    private int idCliente;
    private String pnome;
    private String snome;
    private String endereco;
    private String telefone;
    private String email;
    private String senha;

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getPnome() { return pnome; }
    public void setPnome(String pnome) { this.pnome = pnome; }
    public String getSnome() { return snome; }
    public void setSnome(String snome) { this.snome = snome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
