package com.honda.debrincar;

import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
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

public class TelaLogin extends AppCompatActivity {

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
                Intent in = new Intent("TELA_CADASTRO_ACT");
                in.addCategory("TELA_CADASTRO_CTG");
                startActivity(in);
            }
        });

    }

    private void login() {
        TextView emailText = findViewById(R.id.emailLogin);
        TextView senhaText = findViewById(R.id.senhaLogin);

        String email = emailText.getText().toString();
        String senha = senhaText.getText().toString();

        Task<AuthResult> tarefa = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha);

        tarefa.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(TelaLogin.this, "Login efetuado!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TelaLogin.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
