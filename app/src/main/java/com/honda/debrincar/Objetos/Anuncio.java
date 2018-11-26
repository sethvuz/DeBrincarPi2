package com.honda.debrincar.Objetos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Anuncio {




    private String anuncioID, userID;
    private String titulo, descricao, categoria;
    private int quantidade = 1, prazo = 0;
    private String dataCriacao, dataFim;
    private String telefone, endereco, cidade;

    private String anuncioType;

    private List<String> imagensUrls = new ArrayList<>();


    public Anuncio(){

    }

    public Anuncio (String titulo, String descricao, String tipo){
        this.titulo = titulo;
        this.descricao = descricao;
        this.anuncioType = tipo;
    }

    public Anuncio (String titulo, String descricao, String tipo, String dataCriacao){
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.anuncioType = tipo;
    }



    public String getAnuncioID() {
        return anuncioID;
    }

    public void setAnuncioID(String anuncioID) {
        this.anuncioID = anuncioID;
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

    public String getCidade() { return cidade; }

    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getAnuncioType() { return anuncioType; }

    public void setAnuncioType(String anuncioType) { this.anuncioType = anuncioType; }

    public String getDataFim() { return dataFim; }

    public void setDataFim(String dataFim) { this.dataFim = dataFim; }

    public List<String> getImagensUrls() { return imagensUrls; }

    public void setImagensUrls(List<String> imagensUrls) { this.imagensUrls = imagensUrls; }

    public String getCategoria() { return categoria; }

    public void setCategoria(String categoria) { this.categoria = categoria;  }

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

    public Map<String, Object> getMapaDados(){
        HashMap<String, Object> dados = new HashMap<>();
        dados.put("titulo", titulo);
        dados.put("categoria", categoria);
        dados.put("TipoAnuncio", anuncioType);
        dados.put("descricao", descricao);
        dados.put("endereco", endereco);
        //dados.put("cidade", cidade);
        dados.put("dataCriacao", dataCriacao);
        dados.put("id", anuncioID);
        dados.put("userid",userID);

        if(anuncioType.equals("campanha")){
            dados.put("prazo", prazo);
        }

        return dados;
    }
}
