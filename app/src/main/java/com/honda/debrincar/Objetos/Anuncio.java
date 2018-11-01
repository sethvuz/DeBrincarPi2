package com.honda.debrincar.Objetos;

public class Anuncio {




    private String anumID, userID;
    private String titulo, descricao;
    private int quantidade;
    private String dataCriacao;
    private String telefone, endereco;


    public Anuncio(){

    }

    public Anuncio (String titulo, String descricao){
        this.titulo = titulo;
        this.descricao = descricao;
    }



    public String getAnumID() {
        return anumID;
    }

    public void setAnumID(String anumID) {
        this.anumID = anumID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }




}
