package com.honda.debrincar.Activities.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.honda.debrincar.Config.AnunciosAdapter;
import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;

import java.util.ArrayList;

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

        anuncios.add(new Anuncio("Ursinho", "Ursinho de pelúcia", "DOAÇÃO"));
        anuncios.add(new Anuncio("Carrinho", "Carrinho super novo", "SOLICITAÇÃO"));
        anuncios.add(new Anuncio("Peão", "Peão bem conservado", "DOAÇÃO"));
        anuncios.add(new Anuncio("Boneca Barbie", "Como nova...", "DOAÇÃO"));
        anuncios.add(new Anuncio("Caminhãozinho", "Meu filho não brinca mais", "CAMPANHA"));
        anuncios.add(new Anuncio("Chocalho", "Chocalho ainda muito novo", "CAMPANHA"));
        anuncios.add(new Anuncio("Bola de futebol", "Bem conservada", "SOLICITAÇÃO"));
        anuncios.add(new Anuncio("Jogo de quebra Cabeça", "Ótimo para o desenvolvimento", "SOLICITAÇÃO"));
        anuncios.add(new Anuncio("Jogo de xadrez", "Disponível pra quem precisar", "DOAÇÃO"));
        anuncios.add(new Anuncio("Bicicleta", "Para quem precisar", "CAMPANHA"));
        anuncios.add(new Anuncio("Skate", "Em bom estado", "DOAÇÃO"));
        anuncios.add(new Anuncio("Patins", "Em bom estado", "DOAÇÃO"));
        anuncios.add(new Anuncio("Patinete", "Em bom estado", "DOAÇÃO"));

        //INICIA O ADAPTER E A LISTVIEW E SETA O ADAPTER NA LISTVIEW
        AnunciosAdapter itemsAdapter = new AnunciosAdapter(getActivity(), R.layout.item_anuncio_layout, anuncios);
        ListView listView = view.findViewById(R.id.lista_anuncios_view);
        listView.setAdapter(itemsAdapter);

        return view;
    }

}
