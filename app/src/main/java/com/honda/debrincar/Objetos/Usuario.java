package com.honda.debrincar.Objetos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;

import java.util.HashMap;
import java.util.Map;

public class Usuario {



    //dados de todos usuários
    private String id;
    private String nome;
    private String endereco;
    private String cep;
    private String cidade;
    private String estado;
    private String telefone;

    private Boolean isPessoaFisica = true;

    //dados de pessoa física
    private String sobrenome;
    private String cpf;


    //dados de instituição
    private String cnpj;
    private String descricao;


    private String email;
    private String senha;

    private String imagemUsuarioUrl = "";

    public Usuario(){

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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getPessoaFisica() {
        return isPessoaFisica;
    }

    public void setPessoaFisica(Boolean pessoaFisica) {
        isPessoaFisica = pessoaFisica;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Map<String, Object> getMapaDados(){
        HashMap<String, Object> dados = new HashMap<>();

        if(isPessoaFisica){
            dados.put("nome", nome);
            dados.put("sobrenome", sobrenome);
            dados.put("cpf", cpf);
            dados.put("estado", estado);
            dados.put("cidade", cidade);
            dados.put("endereco", endereco);
            dados.put("telefone", telefone);
            dados.put("PessoaFisica", isPessoaFisica);
        } else {
            dados.put("nome", nome);
            dados.put("descricao", descricao);
            dados.put("cnpj", cnpj);
            dados.put("estado", estado);
            dados.put("cidade", cidade);
            dados.put("endereco", endereco);
            dados.put("telefone", telefone);
            dados.put("PessoaFisica", isPessoaFisica);
        }
        return dados;
    }

    public Map<String, Object> getMapaDadosSeguidor(){
        HashMap<String, Object> dados = new HashMap<>();

        if(isPessoaFisica){
            dados.put("nome", nome);
            dados.put("sobrenome", sobrenome);
            dados.put("PessoaFisica", isPessoaFisica);
        } else {
            dados.put("nome", nome);
            dados.put("descricao", descricao);
            dados.put("PessoaFisica", isPessoaFisica);
        }
        return dados;
    }

    //Salva os dados do usuário
    public void salvarDados(){
        //Salva os dados do usuário no Firebase no nó RAIZ/USUÁRIOS/PF/ID DO USUÁRIO
        DatabaseReference referenciaFirebase = FirebaseMetodos.getFirebaseData();

            referenciaFirebase.child("usuario").child(id).updateChildren(getMapaDados());

    }

    //salva imagem do usuário
    public void salvarDados(String imageUrl){
        //Seta url da imagem do usuário no Firebase
        DatabaseReference referenciaFirebase = FirebaseMetodos.getFirebaseData();

            referenciaFirebase.child("Usuário").child(id).child("imagemUsuarioUrl").setValue(imageUrl);

    }
}
