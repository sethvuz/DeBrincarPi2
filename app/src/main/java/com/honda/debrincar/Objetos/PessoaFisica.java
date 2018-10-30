package com.honda.debrincar.Objetos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.honda.debrincar.Config.ConfiguraçaoFirebase;

public class PessoaFisica {

    private String id;

    private String nome;
    private String sobrenome;
    private String cpf;
    private String dataNascimento;
    private String endereço;
    private String cep;
    private String cidade;
    private String estado;

    private String email;
    private String senha;

    private String imagemUsuarioUrl = "";

    public PessoaFisica(){

    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String codigo) {
        this.id = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEndereço() {
        return endereço;
    }

    public void setEndereço(String endereço) {
        this.endereço = endereço;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Exclude
    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Exclude
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Exclude
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImagemUsuarioUrl() {
        return imagemUsuarioUrl;
    }

    public void setImagemUsuarioUrl(String imagemUsuarioUrl) {
        this.imagemUsuarioUrl = imagemUsuarioUrl;
    }


    public void salvarDados(){
        //Salva os dados do usuário no Firebase no nó RAIZ/USUÁRIOS/PF/ID DO USUÁRIO
        DatabaseReference referenciaFirebase = ConfiguraçaoFirebase.getFirebaseData();
        referenciaFirebase.child("Usuário").child("PF").child(id).setValue(this);
    }

    public void salvarDados(String imageUrl){
        //Seta url da imagem do usuário no Firebase
        DatabaseReference referenciaFirebase = ConfiguraçaoFirebase.getFirebaseData();
        referenciaFirebase.child("Usuário").child("PF").child(id).child("imagemUsuarioUrl").setValue(imageUrl);
    }
}
