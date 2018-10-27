package com.honda.debrincar.Activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.honda.debrincar.Config.Anun_Adapter;
import com.honda.debrincar.Config.ConfiguraçaoFirebase;
import com.honda.debrincar.R;

public class PrincipalAnuncios extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView listaAnuncios;
    private Toolbar mToolbar;

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

        TabLayout tabLayout = findViewById(R.id.tabs_anuncios);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.setElevation(30.0f);
        }

        ViewPager viewPager = findViewById(R.id.viewpager_anuncios);
        Anun_Adapter anunAdapter = new Anun_Adapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tab_titulos));

                //Adicionar o FragmentAdapter ao ViewPager
                viewPager.setAdapter(anunAdapter);
                //Vincula o TabLayout e o ViewPager para que trabalhem juntos
                tabLayout.setupWithViewPager(viewPager);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                UserMenuSelector(menuItem);

                return false;
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
                Toast.makeText(this, "Anúncios", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_ins_anuncios:
                Toast.makeText(this, "Inserir Anúncio", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_favoritos:
                Toast.makeText(this, "Favoritos", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_seguidores:
                Toast.makeText(this, "Seguidores", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_minha_conta:
                Toast.makeText(this, "Minha Conta", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_config:
                Toast.makeText(this, "Configurações", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_logout:
                ConfiguraçaoFirebase.getFirebaseAuth().signOut();
                Intent intent = new Intent("TELA_LOGIN_ACT");
                intent.addCategory("TELA_LOGIN_CTG");
                startActivity(intent);
                finish();
                break;

                default:
                    break;
        }

    }
}
