package com.honda.debrincar.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioCampFragment;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioDoaFragment;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioSolFragment;
import com.honda.debrincar.Activities.Fragments.preCadastro_dialog_anuncio;
import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.Utilitarios.AnunciosAdapter;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.honda.debrincar.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrincipalAnuncios extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mToolbar;

    private ListView listView;
    private DatabaseReference anunciosRef;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private Fragment fragmentoAtual;
    private Boolean isOnFragment = false;


    private CircleImageView menuImagemUser;
    private TextView menuNomeUser;



    //Encapsular?
    private DatabaseReference userRef;
    private FirebaseAuth userAuth;
    private String userID;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_anuncios);

        mToolbar = findViewById(R.id.app_bar_principal);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Anúncios");


        drawerLayout = findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(PrincipalAnuncios.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navegation_view);
        View headView = navigationView.inflateHeaderView(R.layout.menu_nav_header);
        menuImagemUser = headView.findViewById(R.id.menu_img_usuario);
        menuNomeUser = headView.findViewById(R.id.menu_header_username);

        userID = FirebaseMetodos.getUserId();

        userRef = FirebaseMetodos.getFirebaseData().child(getString(R.string.db_no_usuario));
        userRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //Salva em Strings o nome do usuário e o endereço da imagem do perfil
                    String nomeCompleto = dataSnapshot.child("nome").getValue().toString()
                            + dataSnapshot.child("sobrenome").getValue().toString();
                    String imagemPerfil = dataSnapshot.child("imagemUsuarioUrl").getValue().toString();

                    //Seta o nome e a imagem do usuário no menu de navegação
                    menuNomeUser.setText(nomeCompleto);
                    if (!imagemPerfil.equals("")) {
                        Picasso.get()
                                .load(imagemPerfil)
                                .fit()
                                .centerCrop()
                                .into(menuImagemUser);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //SETUP LISTA DOS ANÚNCIOS:
        {
            listView = findViewById(R.id.listview_principal);

            anunciosRef = FirebaseMetodos.getFirebaseData();
            anunciosRef.child(getString(R.string.db_no_anuncios)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Anuncio anuncio;
                    HashMap<String, Object> anuncioData;
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        anuncioData = (HashMap<String, Object>) ds.getValue();
                        anuncio = new Anuncio(
                                anuncioData.get("titulo").toString(),
                                anuncioData.get("descricao").toString(),
                                anuncioData.get("TipoAnuncio").toString(),
                                anuncioData.get("dataCriacao").toString());
                        anuncio.setAnuncioID(anuncioData.get("id").toString());
                        anuncio.setUserID(anuncioData.get("userid").toString());
                        listaAnuncios.add(anuncio);
                    }

                    AnunciosAdapter itemsAdapter = new AnunciosAdapter(PrincipalAnuncios.this, R.layout.item_anuncio_layout, listaAnuncios);
                    listView.setAdapter(itemsAdapter);
                    setupClickListeners(listaAnuncios);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }





        /*
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Inicializa o fragmento principal dos anúncios
        Lista_Anuncios_Fragment anunciosFragment = new Lista_Anuncios_Fragment();
        fragmentTransaction.add(R.id.container_principal, anunciosFragment);
        fragmentTransaction.commit();
        */


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                UserMenuSelector(menuItem);

                return false;
            }
        });
    }

    private void setupClickListeners(List<Anuncio> listaAnuncios) {

        final List<Anuncio> anuncios = listaAnuncios;



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String anunTipo = anuncios.get(position).getAnuncioType();
                final FragmentManager fm = getSupportFragmentManager();
                final FragmentTransaction fragmentTransaction = fm.beginTransaction();
                switch (anunTipo){
                    case "doacao":
                        isOnFragment = true;
                        PaginaAnuncioDoaFragment paginaAnuncioDoaFragment = new PaginaAnuncioDoaFragment();
                        paginaAnuncioDoaFragment.setAnuncio(anuncios.get(position));
                        fragmentoAtual = paginaAnuncioDoaFragment;
                        fragmentTransaction.replace(R.id.container_principal, paginaAnuncioDoaFragment)
                                .commit();
                        break;
                    case "solicitacao":
                        isOnFragment = true;
                        PaginaAnuncioSolFragment paginaAnuncioSolFragment =  new PaginaAnuncioSolFragment();
                        fragmentoAtual = paginaAnuncioSolFragment;
                        fragmentTransaction.replace(R.id.container_principal, paginaAnuncioSolFragment)
                                .commit();
                        break;
                    case "campanha":
                        isOnFragment = true;
                        PaginaAnuncioCampFragment paginaAnuncioCampFragment = new PaginaAnuncioCampFragment();
                        fragmentoAtual = paginaAnuncioCampFragment;
                        fragmentTransaction.replace(R.id.container_principal, paginaAnuncioCampFragment)
                                .commit();
                        break;
                    default:
                        Toast.makeText(PrincipalAnuncios.this, "Erro ao carregar anúncio.", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_anuncios:
                Intent intentTelaAnuncios = new Intent("TELA_ANUNCIOS_ACT");
                intentTelaAnuncios.addCategory("TELA_ANUNCIOS_CTG");
                intentTelaAnuncios.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentTelaAnuncios);
                finish();
                drawerLayout.closeDrawer(Gravity.START, true);
                break;

            case R.id.nav_ins_anuncios:
                preCadastro_dialog_anuncio dialog = new preCadastro_dialog_anuncio();
                dialog.show(getSupportFragmentManager(), "Opção");
                drawerLayout.closeDrawer(Gravity.START, true);
                break;

            case R.id.nav_favoritos:
                String targetFragmentfav = getString(R.string.targetfragment_favoritos);
                Bundle bundlefav = new Bundle();
                bundlefav.putString(getString(R.string.targetfragment), getString(R.string.targetfragment_favoritos));
                Intent favIntent = new Intent("TELA_BLANKACTIVITY_ACT");
                favIntent.addCategory("TELA_BLANKACTIVITY_ACT");
                favIntent.putExtras(bundlefav);
                startActivity(favIntent);
                break;

            case R.id.nav_seguidores:
                String targetFragmentseg = getString(R.string.targetfragment_seguidores);
                Bundle bundleseg = new Bundle();
                bundleseg.putString(getString(R.string.targetfragment), getString(R.string.targetfragment_seguidores));
                Intent segIntent = new Intent("TELA_BLANKACTIVITY_ACT");
                segIntent.addCategory("TELA_BLANKACTIVITY_ACT");
                segIntent.putExtras(bundleseg);
                startActivity(segIntent);
                break;

            case R.id.nav_minha_conta:
                Intent intentUserProfile = new Intent("TELA_USER_PROFILE_ACT");
                intentUserProfile.addCategory("TELA_USER_PROFILE_CTG");
                startActivity(intentUserProfile);
                drawerLayout.closeDrawer(Gravity.START, true);
                break;

            case R.id.nav_config:
                Intent configIntent = new Intent ("TELA_CONFIGURACOES_ACT");
                configIntent.addCategory("TELA_CONFIGURACOES_CTG");
                startActivity(configIntent);
                drawerLayout.closeDrawer(Gravity.START, true);
                break;

            case R.id.nav_logout:
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


    @Override
    public void onBackPressed() {
        if (isOnFragment){
            getSupportFragmentManager().beginTransaction().remove(fragmentoAtual).commit();
            getSupportFragmentManager().popBackStack();
        } else super.onBackPressed();
    }
}


