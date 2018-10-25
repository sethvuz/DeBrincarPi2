package com.honda.debrincar.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.honda.debrincar.R;

public class PrincipalAnuncios extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView listaAnuncios;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_anuncios);

        mToolbar = findViewById(R.id.anuncios_list_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Anúncios");


        drawerLayout = findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(PrincipalAnuncios.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navegation_view);
        View headView = navigationView.inflateHeaderView(R.layout.menu_nav_header);


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
                Toast.makeText(this, "Sair", Toast.LENGTH_LONG).show();
                break;

                default:
                    break;
        }

    }
}
