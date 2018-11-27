package com.honda.debrincar.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.Activities.Fragments.EditProfileFragment;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.honda.debrincar.Utilitarios.ProfileFragmentsPageAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {
    private static final String TAG = "UserProfile";

    private ProfileFragmentsPageAdapter opcoesPageAdapter;
    private ViewPager fragmentsPager;
    private LinearLayout rootRelativeLayout;

    private Fragment fragmentoAtual;

    private TextView nomeProfile;
    private TextView tipoUserProfile;
    private CircleImageView fotoUserProfile;

    private String userId;
    private DatabaseReference userRef;
    private HashMap<String, Object> userMapa = new HashMap<>();
    private String isPessoaFisica;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        Toolbar mToolbar = findViewById(R.id.toolbar_com_backbtn);
        ImageView backBtn = mToolbar.findViewById(R.id.back_btn_appbar);
        TextView toolbarTitulo = mToolbar.findViewById(R.id.titulo_appbar_backbtn);
        toolbarTitulo.setText("Minha Conta");


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        nomeProfile = findViewById(R.id.nome_userprofile);
        tipoUserProfile = findViewById(R.id.tipo_userprofile);
        fotoUserProfile = findViewById(R.id.foto_userprofile);

        userId = FirebaseMetodos.getUserId();
        userRef = FirebaseMetodos.getFirebaseData();

        userRef.child(getString(R.string.db_no_usuario)).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    userMapa = (HashMap<String, Object>) dataSnapshot.getValue();
                    String nome = userMapa.get("nome").toString() + " " + userMapa.get("sobrenome").toString();
                    nomeProfile.setText(nome);
                    isPessoaFisica = userMapa.get("PessoaFisica").toString();
                    if(isPessoaFisica.equals("false")){
                        tipoUserProfile.setText(getString(R.string.profile_tipo_instituicao));
                    }
                    String imagemUser = userMapa.get("imagemUsuarioUrl").toString();
                    setUserImage(imagemUser);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //fragmentsPager = findViewById(R.id.profile_fragment_container);
        rootRelativeLayout = findViewById(R.id.root_editprofile_layout);
        setupListaOpcoes();


    }

    private void setUserImage(String imagemUsuario) {
        Picasso.get()
                .load(imagemUsuario)
                .fit()
                .centerCrop()
                .into(fotoUserProfile);
    }


    private void setupListaOpcoes() {

        ListView listView = findViewById(R.id.listview_for_include);
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

                switch (position){
                    case 0:
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        EditProfileFragment editProfileFragment = new EditProfileFragment();
                        fragmentoAtual = editProfileFragment;
                        rootRelativeLayout.setVisibility(View.GONE);
                        ft.replace(R.id.layout_profile_fragment_container, editProfileFragment).commit();
                        break;
                    case 1:
                        Intent intentAnuncios = new Intent("TELA_LISTAFAVORITOS_ACT");
                        intentAnuncios.addCategory("TELA_LISTAFAVORITOS_CTG");
                        intentAnuncios.putExtra(getString(R.string.targetLista), getString(R.string.targetlista_anunciosdastrados));
                        intentAnuncios.putExtra(getString(R.string.targetlista_userid), userId);
                        startActivity(intentAnuncios);
                        break;
                    case 2:
                        Intent intentFavoritos = new Intent("TELA_LISTAFAVORITOS_ACT");
                        intentFavoritos.addCategory("TELA_LISTAFAVORITOS_CTG");
                        intentFavoritos.putExtra(getString(R.string.targetLista), getString(R.string.targetlista_favoritos));
                        startActivity(intentFavoritos);
                        break;
                    case 3:
                        Intent intentSeguidores = new Intent("TELA_LISTAFAVORITOS_ACT");
                        intentSeguidores.addCategory("TELA_LISTAFAVORITOS_CTG");
                        intentSeguidores.putExtra(getString(R.string.targetLista), getString(R.string.targetlista_seguidores));
                        startActivity(intentSeguidores);
                        break;
                    case 4:
                        Intent configIntent = new Intent ("TELA_CONFIGURACOES_ACT");
                        configIntent.addCategory("TELA_CONFIGURACOES_CTG");
                        startActivity(configIntent);
                        break;
                    case 5:
                        FirebaseMetodos.getFirebaseAuth().signOut();
                        Intent intentLogout = new Intent("TELA_LOGIN_ACT");
                        intentLogout.addCategory("TELA_LOGIN_CTG");
                        startActivity(intentLogout);
                        finish();
                        break;
                        default:
                            break;
                }
            }
        });
    }




    @Override
    public void onBackPressed() {

       if (rootRelativeLayout.getVisibility() == View.GONE){
           getSupportFragmentManager().beginTransaction().remove(fragmentoAtual).commit();
           rootRelativeLayout.setVisibility(View.VISIBLE);
       } else super.onBackPressed();

    }
}
