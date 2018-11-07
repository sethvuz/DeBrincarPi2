package com.honda.debrincar.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.honda.debrincar.Activities.Fragments.CadastroCampanhaFragment;
import com.honda.debrincar.Activities.Fragments.CadastroSolicitacaoFragment;
import com.honda.debrincar.Activities.Fragments.CadastroDoacaoFragment;
import com.honda.debrincar.R;

public class CadastroAnuncio extends AppCompatActivity {

    private String anuncioType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_anuncio);

        //Captura o dado passado pela intent do DialogFragment e salva
        //na variável userType
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        anuncioType = bundle.getString("anuncioType");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Define qual fragmento será inflado
        switch (anuncioType){

            case "DOAÇÃO":
                CadastroDoacaoFragment cadatroDoacaoFragment = new CadastroDoacaoFragment();
                fragmentTransaction.replace(R.id.container_cadastro_anuncio, cadatroDoacaoFragment)
                        .commit();
                break;
            case "SOLICITAÇÃO":
                CadastroSolicitacaoFragment cadastroSolicitacaoFragment = new CadastroSolicitacaoFragment();
                fragmentTransaction.replace(R.id.container_principal, cadastroSolicitacaoFragment)
                        .commit();
                break;
            case "CAMPANHA":
                CadastroCampanhaFragment cadastroCampanhaFragment = new CadastroCampanhaFragment();
                fragmentTransaction.replace(R.id.container_principal, cadastroCampanhaFragment)
                        .commit();
                break;
            default:
                Toast.makeText(this, "Erro ao carregar página de cadastro.", Toast.LENGTH_LONG).show();
                break;

        }
    }
}
