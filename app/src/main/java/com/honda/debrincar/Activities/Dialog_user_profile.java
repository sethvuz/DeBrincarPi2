package com.honda.debrincar.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dialog_user_profile extends DialogFragment {

    private static final String TAG = "Dialog_user_profile";


    private String usuarioId, userId, userName;
    private Boolean isSeguindo = false;
    private Button btnSeguir, btnVerAnuncios;
    private DatabaseReference userRef;


    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogl_user_profile, container, false);

        final TextView nomeUsuario = view.findViewById(R.id.dialog_nome_usuario);
        final TextView tipoUsuario = view.findViewById(R.id.dialog_tipo_usuario);
        final TextView localUsuario = view.findViewById(R.id.dialog_local_usuario);
        final CircleImageView imagemUsuario = view.findViewById(R.id.dialog_imagem_usuario);
        btnSeguir = view.findViewById(R.id.btn_seguir_usuario);
        btnVerAnuncios = view.findViewById(R.id.btn_ver_anuncios);


        userRef = FirebaseMetodos.getFirebaseData();
        userId = FirebaseMetodos.getUserId();
        userRef.child(getActivity().getString(R.string.db_no_usuario)).child(usuarioId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nome, local, tipo, imagemUrl;
                        nome = dataSnapshot.child("nome").getValue().toString() +
                                " " +
                                dataSnapshot.child("sobrenome").getValue().toString();
                        tipo = dataSnapshot.child("PessoaFisica").getValue().toString();
                        local = dataSnapshot.child("cidade").getValue().toString() +
                                " - " + dataSnapshot.child("estado").getValue().toString();
                        if(!tipo.equals("true")){
                            tipoUsuario.setText("Pessoa Jurídica");
                        }

                        userName = nome;
                        nomeUsuario.setText(nome);
                        localUsuario.setText(local);
                        imagemUrl = dataSnapshot.child("imagemUsuarioUrl").getValue().toString();
                        Picasso.get()
                                .load(imagemUrl)
                                .fit()
                                .centerCrop()
                                .into(imagemUsuario);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        setupJaSeguindo();
        btnSeguir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(!isSeguindo){
                    isSeguindo = true;
                    btnSeguir.setText(getString(R.string.seguindo_btn));
                    btnSeguir.setBackground(getActivity().getResources().getDrawable(R.drawable.fundo_botao_amarelo_anuncio));
                    btnSeguir.setTextColor(getActivity().getResources().getColor(R.color.azulEscuro));
                    userRef.child(getString(R.string.db_no_seguidores)).child(userId).child(usuarioId).setValue(usuarioId).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Seguindo " + userName + ".", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    isSeguindo = false;
                    btnSeguir.setText(getString(R.string.seguir_btn));
                    btnSeguir.setBackground(getActivity().getResources().getDrawable(R.drawable.fundo_botao_azul_anuncio));
                    btnSeguir.setTextColor(getActivity().getResources().getColor(R.color.amareloEscuro));
                    userRef.child(getString(R.string.db_no_seguidores)).child(userId).child(usuarioId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Deixou de seguir " + userName + ".", Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        });

        btnVerAnuncios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAnuncios = new Intent("TELA_LISTAFAVORITOS_ACT");
                intentAnuncios.addCategory("TELA_LISTAFAVORITOS_CTG");
                intentAnuncios.putExtra(getString(R.string.targetLista), getString(R.string.targetlista_anunciosdastrados));
                intentAnuncios.putExtra(getString(R.string.targetlista_userid), usuarioId);
                startActivity(intentAnuncios);
            }
        });

        return view;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    @SuppressLint("NewApi")
    private void setupJaSeguindo() {

        if(userId.equals(usuarioId)){
            isSeguindo = true;
            btnSeguir.setBackground(getActivity().getResources().getDrawable(R.drawable.fundo_botao_cinza_inativo_anuncio));
            btnSeguir.setTextColor(getActivity().getResources().getColor(R.color.cinzaEscuro));
            btnSeguir.setEnabled(false);
        } else {
            userRef.child(getString(R.string.db_no_seguidores)).child(userId).child(usuarioId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        isSeguindo = true;
                        btnSeguir.setText(getString(R.string.seguindo_btn));
                        btnSeguir.setBackground(getActivity().getResources().getDrawable(R.drawable.fundo_botao_amarelo_anuncio));
                        btnSeguir.setTextColor(getActivity().getResources().getColor(R.color.azulEscuro));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastEsception" + e.getMessage());
        }
    }
}
