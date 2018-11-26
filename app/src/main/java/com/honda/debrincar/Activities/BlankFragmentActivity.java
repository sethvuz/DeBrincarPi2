package com.honda.debrincar.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.honda.debrincar.Activities.Fragments.ListaFavoritosFragment;
import com.honda.debrincar.Activities.Fragments.ListaSeguidoresFragment;
import com.honda.debrincar.R;

public class BlankFragmentActivity extends AppCompatActivity {
    private static final String TAG = "BlankFragmentActivity";

    private String targetFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank_fragment);



        //Captura o dado passado pela intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        targetFragment = bundle.getString("targetFragment");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Define qual fragmente será inflado: Pessoa Física ou Instituição
        if(targetFragment.equals(getString(R.string.targetfragment_favoritos))){
            ListaFavoritosFragment favoritosFragment = new ListaFavoritosFragment();
            fragmentTransaction.add(R.id.blank_activity_container, favoritosFragment);
            fragmentTransaction.commit();
        } else {
            ListaSeguidoresFragment seguidoresFragment = new ListaSeguidoresFragment();
            fragmentTransaction.add(R.id.blank_activity_container, seguidoresFragment);
            fragmentTransaction.commit();
        }

    }
}
