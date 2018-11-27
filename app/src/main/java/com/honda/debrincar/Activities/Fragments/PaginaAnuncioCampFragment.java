package com.honda.debrincar.Activities.Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.honda.debrincar.Utilitarios.ImagemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaginaAnuncioCampFragment extends Fragment {
    private static final String TAG = "PaginaAnuncioCampFragment";

    private Anuncio anuncio;

    private DatabaseReference databaseReference;
    private String userId;
    private String userAnuncioId;

    private TextView anuncioAutor;
    private TextView anuncioType;
    private TextView anuncioTitulo;
    private TextView anuncioDescricao;
    private TextView anuncioData;
    private TextView anuncioPrazo;
    private ImageView favoritoView;
    private Button seguirBtn;
    private Drawable coracaoBranco, coracaoAmarelo;

    private String nomeUsuario;
    private String dataAnuncio;
    private boolean isFavorito = false;
    private boolean isSeguindo = false;

    private ViewPager imagensPager;
    private ImagemAdapter imagemAdapter;
    private Activity activityAtual;

    private List<String> imagensUrl = new ArrayList<>();

    public PaginaAnuncioCampFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pagina_anuncio_camp, container, false);

        final Anuncio anuncioAtual = getAnuncio();
        userId = FirebaseMetodos.getUserId();
        userAnuncioId = anuncioAtual.getUserID();
        databaseReference = FirebaseMetodos.getFirebaseData();



        anuncioType = view.findViewById(R.id.pagina_anuncio_type_camp);
        anuncioType.setText("SOLICITAÇÃO");

        anuncioTitulo = view.findViewById(R.id.pagina_titulo_anuncio_camp);
        anuncioTitulo.setText(anuncioAtual.getTitulo());

        anuncioDescricao = view.findViewById(R.id.descricao_label_pagina_anuncio_camp);
        anuncioDescricao.setText(anuncioAtual.getDescricao());

        //Define a data do anúncio na página
        anuncioData = view.findViewById(R.id.data_criacao_anuncio_camp);
        dataAnuncio = anuncioAtual.getDataCriacao().replace('-', '/');
        final String[] dataAnuncioformat = dataAnuncio.split("T");
        dataAnuncio = "Em " + dataAnuncioformat[0];
        anuncioData.setText(dataAnuncio);

        //Define o prazo final para a campanha
        anuncioPrazo = view.findViewById(R.id.prazo_anuncio_camp);



        //Recupera o nome o usuário criador do anúncio
        anuncioAutor = view.findViewById(R.id.nome_autor_anuncio_camp);
        recuperaUserAnuncioData();



        //DEFINE AÇÕES DE CLIQUE PARA ADICIONAR OU EXCLUIR ANÚNCIO DOS FAVORITOS
        favoritoView = view.findViewById(R.id.coracao_fav_pag_camp);
        coracaoAmarelo = getContext().getResources().getDrawable(R.drawable.ic_favorite_amarelo_24dp);
        coracaoBranco = getContext().getResources().getDrawable(R.drawable.ic_favorite_branco_24dp);
        //Define se o anúncio está na lista de favoritos do usuário
        setupIsFavorito();
        {
            favoritoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isFavorito) {
                        favoritoView.setImageDrawable(coracaoAmarelo);
                        isFavorito = true;
                        databaseReference.child(getString(R.string.db_no_favoritos)).child(userId)
                                .child(anuncio.getAnuncioID()).setValue(anuncio.getAnuncioID()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Anuncio salvo em Favoritos!", Toast.LENGTH_LONG).show();

                            }
                        });
                    } else {
                        favoritoView.setImageDrawable(coracaoBranco);
                        isFavorito = false;
                        databaseReference.child(getString(R.string.db_no_favoritos)).child(userId).child(anuncio.getAnuncioID())
                                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Anúncio excluído dos Favoritos!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
        }

        //DEFINE AÇÕES DE CLIQUE PARA SEGUIR OU DEIXAR DE SEGUIR USUÁRIO CRIADOR DO ANÚNCIO
        seguirBtn = view.findViewById(R.id.btn_seguir_anuncio_camp);
        setupJaSeguindo();
        seguirBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if(!isSeguindo){
                    isSeguindo = true;
                    seguirBtn.setText(getString(R.string.seguindo_btn));
                    seguirBtn.setBackground(getActivity().getResources().getDrawable(R.drawable.fundo_botao_amarelo_anuncio));
                    seguirBtn.setTextColor(getActivity().getResources().getColor(R.color.azulEscuro));
                    databaseReference.child(getString(R.string.db_no_seguidores)).child(userId).child(userAnuncioId).setValue(userAnuncioId).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Seguindo " + nomeUsuario + ".", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    isSeguindo = false;
                    seguirBtn.setText(getString(R.string.seguir_btn));
                    seguirBtn.setBackground(getActivity().getResources().getDrawable(R.drawable.fundo_botao_azul_anuncio));
                    seguirBtn.setTextColor(getActivity().getResources().getColor(R.color.amareloEscuro));
                    databaseReference.child(getString(R.string.db_no_seguidores)).child(userId).child(userAnuncioId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Deixou de seguir " + nomeUsuario + ".", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        //EXIBIR AS FOTOS DO ANÚNCIO
        {
            imagensPager = view.findViewById(R.id.image_pager_camp);
            activityAtual = getActivity();
            //Recupera as fotos do anúncio
            databaseReference.child(getString(R.string.db_no_anuncios_imagens)).child(anuncioAtual.getAnuncioID())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String url = ds.getValue().toString();
                                imagensUrl.add(url);
                            }
                            imagemAdapter = new ImagemAdapter(activityAtual, imagensUrl);
                            imagensPager.setAdapter(imagemAdapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        //SETUP DAS QUANTIDADES
        {
            TextView quantidadeAnun = view.findViewById(R.id.num_meta_camp);
            TextView arrecadadoAnun = view.findViewById(R.id.num_arrecadados_sol);
            int quantidade = anuncioAtual.getQuantidade();
            int arrecadado = anuncioAtual.getArrecadado();
            arrecadadoAnun.setText(String.valueOf(arrecadado));
            quantidadeAnun.setText(String.valueOf(quantidade));
        }



        return view;
    }

    @SuppressLint("NewApi")
    private void setupJaSeguindo() {
        if(userId.equals(userAnuncioId)){
            isSeguindo = true;
            seguirBtn.setBackground(getActivity().getResources().getDrawable(R.drawable.fundo_botao_cinza_inativo_anuncio));
            seguirBtn.setTextColor(getActivity().getResources().getColor(R.color.cinzaEscuro));
            seguirBtn.setEnabled(false);
        } else {
            databaseReference.child(getString(R.string.db_no_seguidores)).child(userId).child(userAnuncioId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        isSeguindo = true;
                        seguirBtn.setText(getString(R.string.seguindo_btn));
                        seguirBtn.setBackground(getActivity().getResources().getDrawable(R.drawable.fundo_botao_amarelo_anuncio));
                        seguirBtn.setTextColor(getActivity().getResources().getColor(R.color.azulEscuro));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void setupIsFavorito() {
        databaseReference.child(getString(R.string.db_no_favoritos)).child(userId).child(anuncio.getAnuncioID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            isFavorito = true;
                            favoritoView.setImageDrawable(coracaoAmarelo);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void recuperaUserAnuncioData() {

        databaseReference.child(getString(R.string.db_no_usuario)).child(userAnuncioId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nomeUsuario = dataSnapshot.child(getString(R.string.campo_nome_usuario)).getValue().toString()
                        + " "
                        + dataSnapshot.child(getString(R.string.campo_sobrenome_usuario)).getValue().toString();
                anuncioAutor.setText(nomeUsuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }



}
