package com.honda.debrincar.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.honda.debrincar.Objetos.PessoaFisica;
import com.honda.debrincar.R;

public class CadastroUsuario extends AppCompatActivity {

    private PessoaFisica usuario = new PessoaFisica();

    private EditText nome;
    private EditText sobrenome;
    private EditText cpf;
    private EditText nascimento;
    private EditText endereço;
    private EditText cep;
    private EditText email;
    private EditText senha;
    private EditText confSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        Button btnCadastrar = findViewById(R.id.btn_cadastro);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //SALVA OS CAMPOS EM VARIÁVEIS.
               nome = findViewById(R.id.cad_nome);
               sobrenome = findViewById(R.id.cad_sobrenome);
               cpf = findViewById(R.id.cad_cpf);
               nascimento = findViewById(R.id.cad_nascimento);
               endereço = findViewById(R.id.cad_endereço);
               cep = findViewById(R.id.cad_cep);

                //CAMPOS PARA LOGIN
               email = findViewById(R.id.cad_email);
               senha = findViewById(R.id.cad_senha);
               confSenha = findViewById(R.id.cad_conf_senha);

                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                String confirmaSenha = confSenha.getText().toString();
                    if (usuario.getSenha().equals(confirmaSenha)){

                        //SETA OS DADOS DO USUÁRIO.
                        usuario.setNome(nome.getText().toString());
                        usuario.setSobrenome(sobrenome.getText().toString());
                        usuario.setCpf(cpf.getText().toString());
                        usuario.setDataNascimento(nascimento.getText().toString());
                        usuario.setEndereço(endereço.getText().toString());
                        usuario.setCep(cep.getText().toString());

                        //FUNÇÃO DE CADASTRO DO USUÁRIO NO FIREBASE.
                        cadastraUsuario(usuario.getEmail(), usuario.getSenha());
                    }else {
                        Toast.makeText(CadastroUsuario.this,
                                "Confirmação da senha não confere!",
                                 Toast.LENGTH_SHORT).show();
                    }
            }
        });
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
                    Toast.makeText(CadastroUsuario.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}


