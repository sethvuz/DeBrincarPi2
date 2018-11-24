package com.honda.debrincar.Activities.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.Utilitarios.AnunciosAdapter;
import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Lista_Anuncios_Fragment extends Fragment {


    public Lista_Anuncios_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.lista_anuncios_layout, container, false);

        ArrayList<Anuncio> anuncios = new ArrayList<>();

        final ListView listView = view.findViewById(R.id.lista_anuncios_view);


        DatabaseReference anunciosRef = FirebaseMetodos.getFirebaseData();
        final List<Anuncio> listaAnuncios = new ArrayList<>();


        anunciosRef.child(getActivity().getString(R.string.db_no_anuncios)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Anuncio anuncio;
                HashMap<String, Object> anuncioData;
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    anuncioData = (HashMap<String, Object>) ds.getValue();
                    anuncio = new Anuncio(
                            anuncioData.get("titulo").toString(),
                            anuncioData.get("descricao").toString(),
                            anuncioData.get("TipoAnuncio").toString(),
                            anuncioData.get("dataCriacao").toString());
                            anuncio.setAnuncioID(anuncioData.get("id").toString());
                    listaAnuncios.add(anuncio);
                }

                AnunciosAdapter itemsAdapter = new AnunciosAdapter(getActivity(), R.layout.item_anuncio_layout, listaAnuncios);
                listView.setAdapter(itemsAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
