package com.honda.debrincar.Activities.Fragments;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastroDoacaoFragment extends Fragment {


    private Anuncio anuncio = new Anuncio();
    private List<String> imagens = new ArrayList<>();

    public CadastroDoacaoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cadatro_doacao, container, false);

        final EditText titulo = view.findViewById(R.id.cad_titulo_anun_doa);
        final EditText endereco = view.findViewById(R.id.cad_endereco_anuncio_doa);
        final EditText telefone = view.findViewById(R.id.cad_telefone_anuncio_doa);
        final EditText descricao = view.findViewById(R.id.cad_descricao_anuncio_doa);

        TextView addLocal = view.findViewById(R.id.add_location_cad_doa);

        Button cadBtn = view.findViewById(R.id.cad_anuncio_doa_btn);

        LinearLayout addImageContainer = view.findViewById(R.id.container_principal_addImage_cad_doa);
        ConstraintLayout addImagem = view.findViewById(R.id.container_add_imagem_cad_Doa);

        addImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                anuncio.setTitulo(titulo.getText().toString());
                anuncio.setEndereco(endereco.getText().toString());
                anuncio.setTelefone(telefone.getText().toString());
                anuncio.setDescricao(descricao.getText().toString());

                LocalDate date = LocalDate.now();
                anuncio.setDataCriacao(date.toString());


            }
        });



        return  view;
    }

}
