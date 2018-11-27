package com.honda.debrincar.Activities.Fragments;

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

import com.honda.debrincar.R;

public class preCadastro_dialog_anuncio extends DialogFragment {

    private static final String TAG = "preCad_dialog_anuncio";

    //Var
    private Boolean isPessoaFisica = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_precadastro_anuncio, container, false);

        Button btnDoacao = view.findViewById(R.id.btn_cad_doacao);
        Button btnSolicitacao = view.findViewById(R.id.btn_cad_solicitacao);
        Button btnCampanha = view.findViewById(R.id.btn_cad_campanha);
        TextView msgCamp = view.findViewById(R.id.msg_cannot_camp);

        if(isPessoaFisica){
            btnCampanha.setBackground(getActivity().getResources().getDrawable(R.drawable.fundo_botao_cinza_inativo_anuncio));
            btnCampanha.setEnabled(false);
            msgCamp.setVisibility(View.VISIBLE);

        }

        btnDoacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("TELA_CAD_ANUNCIOS_ACT");
                intent.addCategory("TELA_CAD_ANUNCIOS_CTG");
                Bundle bundle = new Bundle();
                bundle.putString("anuncioType", getActivity().getString(R.string.anun_doacao));
                intent.putExtras(bundle);
                startActivity(intent);
                getDialog().dismiss();
            }
        });

        btnSolicitacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("TELA_CAD_ANUNCIOS_ACT");
                intent.addCategory("TELA_CAD_ANUNCIOS_CTG");
                Bundle bundle = new Bundle();
                bundle.putString("anuncioType", getActivity().getString(R.string.anun_solicitacao));
                intent.putExtras(bundle);
                startActivity(intent);
                getDialog().dismiss();
            }
        });

        btnCampanha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("TELA_CAD_ANUNCIOS_ACT");
                intent.addCategory("TELA_CAD_ANUNCIOS_CTG");
                Bundle bundle = new Bundle();
                bundle.putString("anuncioType", getActivity().getString(R.string.anun_campanha));
                intent.putExtras(bundle);
                startActivity(intent);
                getDialog().dismiss();
            }
        });


        return view;
    }

    public void setPessoaFisica(Boolean pessoaFisica) {
        isPessoaFisica = pessoaFisica;
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
