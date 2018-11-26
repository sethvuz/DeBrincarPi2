package com.honda.debrincar.Activities.Fragments;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honda.debrincar.R;


public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        Toolbar mToolbar = view.findViewById(R.id.toolbar_com_backbtn);
        ImageView backBtn = mToolbar.findViewById(R.id.back_btn_appbar);
        TextView toolbarTitulo = mToolbar.findViewById(R.id.titulo_appbar_backbtn);
        toolbarTitulo.setText("Editar Cadastro");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        final TextView alteraEndereco = view.findViewById(R.id.altera_endereco_edit_profile);
        TextView cancelaAlteraEndereco = view.findViewById(R.id.cancel_altera_edit_profile);
        final ConstraintLayout alteraEnderecoContainer = view.findViewById(R.id.altera_endereco_container_editprofile);
        final LinearLayout enderecoContainer = view.findViewById(R.id.endereco_container_editprofile);
        alteraEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enderecoContainer.setVisibility(View.GONE);
                alteraEnderecoContainer.setVisibility(View.VISIBLE);
            }
        });

        cancelaAlteraEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enderecoContainer.setVisibility(View.VISIBLE);
                alteraEnderecoContainer.setVisibility(View.GONE);
            }
        });

        final TextView alteraSenhaBtn = view.findViewById(R.id.altera_senha_btn_editprofile);
        final TextView cancelAlteraSenha = view.findViewById(R.id.cancel_altera_senha_editprofile);
        final LinearLayout alteraSenhaContainer = view.findViewById(R.id.altera_senha_container_editprofile);
        alteraSenhaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alteraSenhaBtn.setVisibility(View.GONE);
                alteraSenhaContainer.setVisibility(View.VISIBLE);
            }
        });

        cancelAlteraSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alteraSenhaBtn.setVisibility(View.VISIBLE);
                alteraSenhaContainer.setVisibility(View.GONE);
            }
        });



        return view;
    }

}
