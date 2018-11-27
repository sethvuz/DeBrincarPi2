package com.honda.debrincar.Activities;

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

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dialog_user_profile extends DialogFragment {

    private static final String TAG = "Dialog_user_profile";


    private String usuarioId;


    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogl_user_profile, container, false);

        final TextView nomeUsuario = view.findViewById(R.id.dialog_nome_usuario);
        final TextView tipoUsuario = view.findViewById(R.id.dialog_tipo_usuario);
        final TextView localUsuario = view.findViewById(R.id.dialog_local_usuario);
        final CircleImageView imagemUsuario = view.findViewById(R.id.dialog_imagem_usuario);
        final Button btnSeguir = view.findViewById(R.id.btn_seguir_usuario);
        final Button btnVerAnuncios = view.findViewById(R.id.btn_ver_anuncios);




        btnSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        btnVerAnuncios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
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
