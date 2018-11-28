package com.honda.debrincar.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;

public class FinalizaSolicitacaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaliza_solicitacao);

        final DatabaseReference userRef = FirebaseMetodos.getFirebaseData();

        Intent comingIntent = getIntent();
        String isSolicitacaorecebida = comingIntent.getStringExtra("isSolicitacaoRecebida");

        if(isSolicitacaorecebida.equals("true")){

           String mensagem = comingIntent.getStringExtra("mensagem");
           String solicitanteId = comingIntent.getStringExtra("solicitanteId");
           String anuncioId = comingIntent.getStringExtra("anuncioId");

           TextView textoInicial = findViewById(R.id.text_recebeu_solicitacao);
           TextView msgSolicitante = findViewById(R.id.mensagem_finalizacao);
           TextView obrigado = findViewById(R.id.obrigado);

           textoInicial.setText("Você recebeu uma solicitação de");
           msgSolicitante.setText("Mensagem:\n\n" + mensagem + "\n\n Aceitar solicitação?");





            final TextView nomeAutorAnuncio = findViewById(R.id.nome_autor_anuncio);
            userRef.child(getString(R.string.db_no_usuario)).child(solicitanteId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String nome = dataSnapshot.child("nome").getValue().toString() +
                                        " " + dataSnapshot.child("sobrenome").getValue().toString();
                                nomeAutorAnuncio.setText(nome);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            Button btnAceitar = findViewById(R.id.btn_voltar);
            btnAceitar.setText("Aceitar");
            btnAceitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentTelaAnuncios = new Intent("TELA_ANUNCIOS_ACT");
                    intentTelaAnuncios.addCategory("TELA_ANUNCIOS_CTG");
                    intentTelaAnuncios.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentTelaAnuncios);
                    finish();
                }
            });

        }else {

            final String anuncioUserId = comingIntent.getStringExtra("anuncioUserId");

            final TextView nomeAutorAnuncio = findViewById(R.id.nome_autor_anuncio);

            userRef.child(getString(R.string.db_no_usuario)).child(anuncioUserId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String nome = dataSnapshot.child("nome").getValue().toString() +
                                        " " + dataSnapshot.child("sobrenome").getValue().toString();
                                nomeAutorAnuncio.setText(nome);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            Button btnVoltar = findViewById(R.id.btn_voltar);
            btnVoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentTelaAnuncios = new Intent("TELA_ANUNCIOS_ACT");
                    intentTelaAnuncios.addCategory("TELA_ANUNCIOS_CTG");
                    intentTelaAnuncios.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentTelaAnuncios);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intentTelaAnuncios = new Intent("TELA_ANUNCIOS_ACT");
        intentTelaAnuncios.addCategory("TELA_ANUNCIOS_CTG");
        intentTelaAnuncios.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentTelaAnuncios);
        finish();
    }
}
