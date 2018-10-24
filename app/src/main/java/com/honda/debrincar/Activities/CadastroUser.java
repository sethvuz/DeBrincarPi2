package com.honda.debrincar.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.honda.debrincar.Activities.Fragments.InstituicaoFragment;
import com.honda.debrincar.Activities.Fragments.PessoaFragment;
import com.honda.debrincar.R;

public class CadastroUser extends AppCompatActivity {

    //Variável que define se o usuário é Pessoa Física ou Instituição
    public String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_user);

        //Captura o dado passado pela intent do DialogFragment e salva
        //na variável userType
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userType = bundle.getString("userType");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Define qual fragmente será inflado: Pessoa Física ou Instituição
        if(userType.equals("pessoafisica")){
            PessoaFragment pessoaFragment = new PessoaFragment();
            fragmentTransaction.add(R.id.container_cadastro, pessoaFragment);
            fragmentTransaction.commit();
        } else {
            InstituicaoFragment instituicaoFragment = new InstituicaoFragment();
            fragmentTransaction.add(R.id.container_cadastro, instituicaoFragment);
            fragmentTransaction.commit();
        }
    }
}
