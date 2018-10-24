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

import com.honda.debrincar.R;

public class preCadastro_dialog extends DialogFragment {

    private static final String TAG = "preCadastro_dialog";


    //Widgets
    private Button btnPF,btnINST;

    //Var
    private String userType;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_precadastro, container, false);

        btnPF = view.findViewById(R.id.btn_pf);
        btnINST = view.findViewById(R.id.btn_inst);

        btnPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Chama a tela de cadastro passando o tipo de usuário.
                userType = "pessoafisica";
                Intent intent = new Intent("CADASTRO_ACT");
                //intent.addCategory("CADASTRO_CTG");
                Bundle bundle = new Bundle();
                bundle.putString("userType", userType);
                intent.putExtras(bundle);
                startActivity(intent);
                getDialog().dismiss();
            }
        });

        btnINST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Chama a tela de cadastro passando o tipo de usuário.
                userType = "instiuição";
                Intent intent = new Intent("CADASTRO_ACT");
                //intent.addCategory("CADASTRO_CTG");
                Bundle bundle = new Bundle();
                bundle.putString("userType", userType);
                intent.putExtras(bundle);
                startActivity(intent);
                getDialog().dismiss();
            }
        });

        return view;
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
