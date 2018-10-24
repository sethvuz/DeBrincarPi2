package com.honda.debrincar.Activities.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.honda.debrincar.Objetos.Instituicao;
import com.honda.debrincar.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstituicaoFragment extends Fragment {


    private Instituicao usuario = new Instituicao();

    private EditText nome;
    private EditText cnpj;
    private EditText descricao;
    private EditText endereco;
    private EditText cep;

    private EditText nomeResponsavel;
    private EditText cpfResponsavel;
    private EditText enderecoResponsavel;

    private EditText email;
    private EditText senha;
    private EditText confSenha;

    public InstituicaoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_instituicao, container, false);

        //Funções de cadastro quando o botão de cadastrar é pressionado
        Button btnCadastrar = view.findViewById(R.id.btn_cadastro);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //SALVA OS CAMPOS EM VARIÁVEIS.
                nome = view.findViewById(R.id.cad_nome);
                cnpj = view.findViewById(R.id.cad_cnpj);
                descricao = view.findViewById(R.id.cad_descricao);
                endereco = view.findViewById(R.id.cad_endereço);
                cep = view.findViewById(R.id.cad_cep);

                //DADOS DO RESPONSÁVEL
                nomeResponsavel = view.findViewById(R.id.cad_nome_responsavel);
                cpfResponsavel = view.findViewById(R.id.cad_cpf_responsavel);
                enderecoResponsavel = view.findViewById(R.id.cad_endereco_responsavel);

                //CAMPOS PARA LOGIN
                email = view.findViewById(R.id.cad_email);
                senha = view.findViewById(R.id.cad_senha);
                confSenha = view.findViewById(R.id.cad_conf_senha);

                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                String confirmaSenha = confSenha.getText().toString();
                if (usuario.getSenha().equals(confirmaSenha)){

                    //SETA OS DADOS DO USUÁRIO.
                    usuario.setNome(nome.getText().toString());
                    usuario.setCnpj(cnpj.getText().toString());
                    usuario.setDescricao(descricao.getText().toString());
                    usuario.setEndereco(endereco.getText().toString());
                    usuario.setCep(cep.getText().toString());
                    usuario.setNomeResponsavel(nomeResponsavel.getText().toString());
                    usuario.setCpfResponsavel(cpfResponsavel.getText().toString());
                    usuario.setEnderecoResponsavel(enderecoResponsavel.getText().toString());

                    //FUNÇÃO DE CADASTRO DO USUÁRIO NO FIREBASE.
                    cadastraUsuario(usuario.getEmail(), usuario.getSenha());
                }else {
                    Toast.makeText(getActivity(),
                            "Confirmação da senha não confere!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    //FUNÇÃO DE CADASTRO DO USUÁRIO NO FIREBASE
    public void cadastraUsuario(String email, String senha){

        //Cria um novo usuário no Firebase
        Task<AuthResult> tarefa = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha);
        tarefa.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    //Resgata o ID do usuário cadastrado.
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(usuarioFirebase.getUid());
                    usuario.salvarDados();//Função para salvar dados do usuário no Firebase

                    //Direciona para a página de anúncios.
                    Intent intent = new Intent("TELA_LOGIN_ACT");
                    intent.addCategory("TELA_LOGIN_CTG");
                    startActivity(intent);
                } else {
                    //Apresenta mensagem em casos de erro no cadastro.
                    Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
