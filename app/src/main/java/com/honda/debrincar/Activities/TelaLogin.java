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
import com.honda.debrincar.R;

public class TelaLogin extends AppCompatActivity {

    public String userMod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        Button btnLogin = findViewById(R.id.loginBtn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        TextView btnSemCadastro = findViewById(R.id.btnSemCadastro);
        btnSemCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent("CADASTRO_ACT");
               intent.addCategory("CADASTRO_CTG");
               startActivity(intent);
            }
        });

    }

    private void login() {
        TextView emailText = findViewById(R.id.emailLogin);
        TextView senhaText = findViewById(R.id.senhaLogin);

        String email = "";
                if (emailText!= null)
                    email = emailText.getText().toString();
        String senha = "";
                if (senhaText != null)
                     senha = senhaText.getText().toString();

        if ( email != "" && senha != "") {
            Task<AuthResult> tarefa = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha);

            tarefa.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent intent = new Intent("TELA_ANUNCIOS_ACT");
                        intent.addCategory("TELA_ANUNCIOS_CTG");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(TelaLogin.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(TelaLogin.this, "É necessário preencher e-mail e senha!", Toast.LENGTH_LONG).show();
        }



    }
}
