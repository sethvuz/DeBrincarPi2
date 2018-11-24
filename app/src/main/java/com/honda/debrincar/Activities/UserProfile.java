package com.honda.debrincar.Activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.honda.debrincar.Activities.Fragments.EditProfileFragment;
import com.honda.debrincar.Activities.Fragments.ListaAnunciosFavoritosFragment;
import com.honda.debrincar.Activities.Fragments.ListaSeguidoresFragment;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.honda.debrincar.Utilitarios.ProfileFragmentsPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {
    private static final String TAG = "UserProfile";

    private ProfileFragmentsPageAdapter opcoesPageAdapter;
    private ViewPager fragmentsPager;
    private LinearLayout rootRelativeLayout;

    private int fragmentoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fragmentsPager = findViewById(R.id.profile_fragment_container);
        rootRelativeLayout = findViewById(R.id.root_editprofile_layout);
        setupFragmentsOpcoes();

        setupListaOpcoes();


    }

    private void setFragmentsPager(int fragmentNumber) {
        rootRelativeLayout.setVisibility(View.GONE);
        fragmentsPager.setAdapter(opcoesPageAdapter);
        fragmentsPager.setCurrentItem(fragmentNumber);
    }

    private void setupFragmentsOpcoes() {
        opcoesPageAdapter = new ProfileFragmentsPageAdapter(getSupportFragmentManager());
        opcoesPageAdapter.addFragment(new EditProfileFragment(), getString(R.string.op_profile_editprofile)); //fragmento 0
        opcoesPageAdapter.addFragment(new ListaAnunciosFavoritosFragment(), getString(R.string.op_profile_anuncios)); //fragmento 1
        opcoesPageAdapter.addFragment(new ListaAnunciosFavoritosFragment(), getString(R.string.op_profile_favoritos)); //fragmento 2
        opcoesPageAdapter.addFragment(new ListaSeguidoresFragment(), getString(R.string.op_profile_seguidores)); //fragmento 3
    }

    private void setupListaOpcoes() {

        ListView listView = findViewById(R.id.lista_anuncios_view);
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Editar cadastro");
        opcoes.add("Anúncios cadastrados");
        opcoes.add("Favoritos");
        opcoes.add("Seguidores");
        opcoes.add("Configurações");
        opcoes.add("Fazer logout");

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.item_opcao_edit_profile, R.id.text_opcao_userprofile,opcoes);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //limita a posição até 3, pois as opções 4 e 5 não direcionam a fragmentos do ViewPager
                if (position <= 3){
                    fragmentoAtual = position;
                    setFragmentsPager(position);
                }
                if (position == 4){
                    Intent configIntent = new Intent ("TELA_CONFIGURACOES_ACT");
                    configIntent.addCategory("TELA_CONFIGURACOES_CTG");
                    startActivity(configIntent);

                }
                if (position == 5){
                    FirebaseMetodos.getFirebaseAuth().signOut();
                    Intent intentLogout = new Intent("TELA_LOGIN_ACT");
                    intentLogout.addCategory("TELA_LOGIN_CTG");
                    startActivity(intentLogout);
                    finish();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {

       if (rootRelativeLayout.getVisibility() == View.GONE){
           Fragment fragment = opcoesPageAdapter.getFragment(fragmentoAtual);
           getSupportFragmentManager().beginTransaction().remove(fragment).commit();
           rootRelativeLayout.setVisibility(View.VISIBLE);
       } else super.onBackPressed();

    }
}
