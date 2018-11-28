package com.honda.debrincar.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.Utilitarios.ConfiguraçãoApp;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.WebServiceData;

public class FirstActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);



        ConfiguraçãoApp.getArquivoEstados(FirstActivity.this);
        ConfiguraçãoApp.setupEstadosCidades();

        Intent intent;
        if (FirebaseMetodos.isLogged()){
            String userId = FirebaseMetodos.getUserId();
            DatabaseReference userRef = FirebaseMetodos.getFirebaseData();
            userRef.child(getString(R.string.db_no_solicitacoes)).child("recebida").child(userId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                ConfiguraçãoApp.temSolicitacao = true;
                                String mensagem = dataSnapshot.child("mensagem").getValue().toString();
                                String solicitanteId = dataSnapshot.child("solicitante").getValue().toString();
                                String anuncioId = dataSnapshot.child("anuncioId").getValue().toString();
                                Intent intent = new Intent("TELA_ANUNCIOS_ACT");
                                intent.addCategory("TELA_ANUNCIOS_CTG");
                                intent.putExtra("mensagem", mensagem);
                                intent.putExtra("solicitanteId", solicitanteId);
                                intent.putExtra("anuncioId", anuncioId);
                                intent.putExtra("tem_solicitacao", true);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent("TELA_ANUNCIOS_ACT");
                                intent.addCategory("TELA_ANUNCIOS_CTG");
                                intent.putExtra("tem_solicitacao", false);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } else {
            intent = new Intent("TELA_LOGIN_ACT");
            intent.addCategory("TELA_LOGIN_CTG");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
