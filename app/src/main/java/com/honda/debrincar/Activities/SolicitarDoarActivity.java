package com.honda.debrincar.Activities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;

import java.util.HashMap;

public class SolicitarDoarActivity extends AppCompatActivity {

    private int quantidadeNum = 1;
    private String anuncioId, anuncioUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_doar);

        Toolbar mToolbar = findViewById(R.id.toolbar_com_backbtn);
        ImageView backBtn = mToolbar.findViewById(R.id.back_btn_appbar);
        TextView toolbarTitulo = mToolbar.findViewById(R.id.titulo_appbar_backbtn);
        toolbarTitulo.setText("Solicitação");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Intent comingIntent = getIntent();
        anuncioId = comingIntent.getStringExtra("anuncioID");

        ConstraintLayout anuncioSolicitado = findViewById(R.id.link_anuncio_solicitado);
        final TextView tituloAnuncio = findViewById(R.id.titulo_anuncio_solicitado);
        final EditText msgSolicitacao = findViewById(R.id.msg_envia_solicitacao);
        Button btnEnviaSolicitacao = findViewById(R.id.btn_envia_solicitacao);

        final DatabaseReference anuncioRef = FirebaseMetodos.getFirebaseData();
        anuncioRef.child(getString(R.string.db_no_anuncios)).child(anuncioId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String titulo = dataSnapshot.child("titulo").getValue().toString();
                            tituloAnuncio.setText(titulo);
                            String userId = dataSnapshot.child("userid").getValue().toString();
                            anuncioUserId = userId;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });






        //SETUP DOS BOTÕES PARA DEFINIR QUANTIDADE
        {
            Button btnMais = findViewById(R.id.btn_mais);
            Button btnMenos = findViewById(R.id.btn_menos);
            final TextView numQuantidade = findViewById(R.id.num_quantidade_solicitacao);
            btnMais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantidadeNum++;
                    numQuantidade.setText(String.valueOf(quantidadeNum));
                }
            });

            btnMenos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantidadeNum>1){
                        quantidadeNum--;
                        numQuantidade.setText(String.valueOf(quantidadeNum));
                    } else {
                        Toast.makeText(SolicitarDoarActivity.this, "É preciso pelo menos 01 item!", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

        btnEnviaSolicitacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mensagem = msgSolicitacao.getText().toString();
                String userId = FirebaseMetodos.getUserId();
                final HashMap<String, Object> solicitaMap = new HashMap<>();
                solicitaMap.put("mensagem", mensagem);
                solicitaMap.put("quantidade", quantidadeNum);
                solicitaMap.put("autor_anuncio", anuncioUserId);
                solicitaMap.put("anuncioId", anuncioId);
                solicitaMap.put("solicitante", userId);

                anuncioRef.child(getString(R.string.db_no_solicitacoes)).child("enviada").child("anuncioId")
                        .child(userId).setValue(solicitaMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        anuncioRef.child(getString(R.string.db_no_solicitacoes)).child("recebida").child(anuncioUserId)
                                .child(anuncioId).setValue(solicitaMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent finalizaIntent = new Intent("TELA_FINALIZASOLICITACAO_ACT");
                                finalizaIntent.addCategory("TELA_FINALIZASOLICITACAO_CTG");
                                finalizaIntent.putExtra("isSolicitacaoRecebida", "false");
                                finalizaIntent.putExtra("anuncioUserId", anuncioUserId);
                                startActivity(finalizaIntent);
                            }
                        });


                    }
                });





            }
        });






    }
}
