package com.honda.debrincar.Objetos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Anuncio {




    private String anumID, userID;
    private String titulo, descricao;
    private int quantidade = 1;
    private String dataCriacao, dataFim;
    private String telefone, endereco, cidade;

    private String anuncioType;

    private List<String> imagensUrls = new ArrayList<>();


    public Anuncio(){

    }

    public Anuncio (String titulo, String descricao, String tipo){
        this.titulo = titulo;
        this.descricao = descricao;
        anuncioType = tipo;
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

    public String getCidade() { return cidade; }

    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getAnuncioType() { return anuncioType; }

    public void setAnuncioType(String anuncioType) { this.anuncioType = anuncioType; }

    public String getDataFim() { return dataFim; }

    public void setDataFim(String dataFim) { this.dataFim = dataFim; }

    public List<String> getImagensUrls() { return imagensUrls; }

    public void setImagensUrls(List<String> imagensUrls) { this.imagensUrls = imagensUrls; }

    public Map<String, Object> MapaDados(){
        HashMap<String, Object> dados = new HashMap<>();
        dados.put("titulo", titulo);
        dados.put("TipoAnuncio", anuncioType);
        dados.put("descricao", descricao);
        dados.put("endereco", endereco);
        dados.put("telefone", telefone);
        dados.put("cidade", cidade);
        dados.put("dataCriacao", dataCriacao);
        dados.put("dataFim", dataFim);

        return dados;
    }
}
