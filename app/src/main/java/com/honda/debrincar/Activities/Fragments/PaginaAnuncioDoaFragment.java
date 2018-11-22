package com.honda.debrincar.Activities.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;

public class PaginaAnuncioDoaFragment extends Fragment {


    private Anuncio anuncio;

    private TextView anuncioAutor;
    private String nomeUsuario;



    public PaginaAnuncioDoaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pagina_anuncio_doa, container, false);

        final Anuncio anuncioAtual = getAnuncio();

        TextView anuncioType = view.findViewById(R.id.pagina_anuncio_type_doa);
        anuncioType.setText("DOAÇÃO");

        TextView anuncioTitulo = view.findViewById(R.id.pagina_titulo_anuncio_doa);
        anuncioType.setText(anuncioAtual.getTitulo());

        TextView anuncioDescricao = view.findViewById(R.id.descricao_pagina_anuncio_doa);
        anuncioType.setText(anuncioAtual.getDescricao());

        anuncioAutor = view.findViewById(R.id.pagina_autor_anuncio_doa);
        String userId = FirebaseMetodos.getUserId();
        DatabaseReference userRef = FirebaseMetodos.getFirebaseData();

        //Recupera o nome o usuário criador do anúncio
        userRef.child(getString(R.string.db_no_usuario)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nomeUsuario = dataSnapshot.child(getString(R.string.campo_nome_usuario)).toString()
                        + " "
                        + dataSnapshot.child(getString(R.string.campo_sobrenome_usuario)).toString();
                anuncioAutor.setText(nomeUsuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Recupera as fotos do anúncio
        userRef.child(getString(R.string.db_no_anuncios_imagens)).child(anuncioAtual.getAnuncioID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });











        return view;

    }



    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }



}
