package com.honda.debrincar.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.honda.debrincar.Config.ConfiguraçaoFirebase;
import com.honda.debrincar.R;

public class FirstActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Intent intent;
        if (ConfiguraçaoFirebase.isLogged()){
            intent = new Intent("TELA_ANUNCIOS_ACT");
            intent.addCategory("TELA_ANUNCIOS_CTG");
            startActivity(intent);
        } else {
            intent = new Intent("TELA_LOGIN_ACT");
            intent.addCategory("TELA_LOGIN_CTG");
            startActivity(intent);
        }
    }
}
