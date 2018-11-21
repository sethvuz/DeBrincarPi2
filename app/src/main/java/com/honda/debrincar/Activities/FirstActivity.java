package com.honda.debrincar.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.honda.debrincar.R;

public class FirstActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Intent intent;
        if (FirebaseMetodos.isLogged()){
            intent = new Intent("TELA_ANUNCIOS_ACT");
            intent.addCategory("TELA_ANUNCIOS_CTG");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            intent = new Intent("TELA_LOGIN_ACT");
            intent.addCategory("TELA_LOGIN_CTG");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
