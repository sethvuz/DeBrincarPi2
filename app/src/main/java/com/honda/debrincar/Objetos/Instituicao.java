package com.honda.debrincar.Objetos;

import com.google.firebase.database.DatabaseReference;
import com.honda.debrincar.Config.ConfiguraçaoFirebase;

public class Instituicao {

    private String id;

    private String nome;
    private String cnpj;
    private String endereco;
    private String cep;
    private String cidade;
    private String estado;
    private String descricao;

    private String nomeResponsavel;
    private String cpfResponsavel;
    private String enderecoResponsavel;

    private String email;
    private String senha;

    public Instituicao() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereço) {
        this.endereco = endereço;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descriçao) {
        this.descricao = descriçao;
    }

    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getCpfResponsavel() {
        return cpfResponsavel;
    }

    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    public String getEnderecoResponsavel() {
        return enderecoResponsavel;
    }

    public void setEnderecoResponsavel(String endereçoResponsavel) {
        this.enderecoResponsavel = endereçoResponsavel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void salvarDados(){
        //Salva os dados do usuário no Firebase no nó RAIZ/USUÁRIOS/PF/ID DO USUÁRIO
        DatabaseReference referenciaFirebase = ConfiguraçaoFirebase.getFirebaseData();
        referenciaFirebase.child("Usuário").child("INST").child(id).setValue(this);

    }
}
