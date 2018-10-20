package com.honda.debrincar.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.honda.debrincar.Objetos.PessoaFisica;
import com.honda.debrincar.Objetos.Usuario;
import com.honda.debrincar.R;

public class CadastroUsuario extends AppCompatActivity {

    private PessoaFisica usuario = new PessoaFisica();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        Button btnCadastrar = findViewById(R.id.btn_cadastro);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView cadNome = findViewById(R.id.cad_nome);
                TextView cadSobrenome = findViewById(R.id.cad_sobrenome);
                TextView cadCPF = findViewById(R.id.cad_cpf);
                TextView cadNascimento = findViewById(R.id.cad_nascimento);
                TextView cadEndereço = findViewById(R.id.cad_endereço);
                TextView cadCEP = findViewById(R.id.cad_cep);

                TextView cadEmail = findViewById(R.id.cad_email);
                TextView cadSenha = findViewById(R.id.cad_senha);
                TextView cadConfSenha = findViewById(R.id.cad_conf_senha);

                usuario.setEmail(cadEmail.getText().toString());
                usuario.setSenha(cadSenha.getText().toString());
                String confSenha = cadConfSenha.getText().toString();
                    if (usuario.getSenha() == confSenha){
                        cadastraUsuario(usuario.getEmail(), usuario.getSenha());
                    }else {
                        Toast.makeText(CadastroUsuario.this,
                                "Confirmação da senha não confere!",
                                 Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
    public void cadastraUsuario(String email, String senha){

        Task<AuthResult> tarefa = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha);
        tarefa.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent("TELA_ANUNCIOS_ACT");
                    intent.addCategory("TELA_ANUNCIOS_CTG");
                    startActivity(intent);
                } else {
                    Toast.makeText(CadastroUsuario.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}


