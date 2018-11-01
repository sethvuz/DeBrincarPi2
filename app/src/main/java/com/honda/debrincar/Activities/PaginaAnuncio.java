package com.honda.debrincar.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.Config.ConfiguraçaoFirebase;
import com.honda.debrincar.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PaginaAnuncio extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView listaAnuncios;
    private Toolbar mToolbar;

    private CircleImageView menuImagemUser;
    private TextView menuNomeUser;

    //Encapsular?
    private DatabaseReference userRef;
    private FirebaseAuth userAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_anuncio);


        mToolbar = findViewById(R.id.app_bar_pagina_anuncio);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Anúncio");


        /*drawerLayout = findViewById(R.id.anun_pagina_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(PaginaAnuncio.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        navigationView = findViewById(R.id.anun_pagina_navegation_view);
        View headView = navigationView.inflateHeaderView(R.layout.menu_nav_header);
        menuImagemUser = headView.findViewById(R.id.menu_img_usuario);
        menuNomeUser = headView.findViewById(R.id.menu_header_username);

        userAuth = ConfiguraçaoFirebase.getFirebaseAuth();
        userID = userAuth.getCurrentUser().getUid();

        userRef = ConfiguraçaoFirebase.getFirebaseData().child("Usuário").child("PF");
        userRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Salva em Strings o nome do usuário e o endereço da imagem do perfil
                    String nomeCompleto = dataSnapshot.child("nome").getValue().toString()
                            + dataSnapshot.child("sobrenome").getValue().toString();
                    String imagemPerfil = dataSnapshot.child("imagemUsuarioUrl").getValue().toString();

                    //Seta o nome e a imagem do usuário no menu de navegação
                    menuNomeUser.setText(nomeCompleto);
                    Picasso.get().load(imagemPerfil).into(menuImagemUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
