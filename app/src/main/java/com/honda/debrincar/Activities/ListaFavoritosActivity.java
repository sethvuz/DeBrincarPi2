package com.honda.debrincar.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioCampFragment;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioDoaFragment;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioSolFragment;
import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.AnunciosAdapter;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.honda.debrincar.Utilitarios.SeguidoresAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListaFavoritosActivity extends AppCompatActivity {
    private static final String TAG = "ListaFavoritosActivity";

    private ListView listView;
    private DatabaseReference anunciosRef;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private List<String> listaseguidores = new ArrayList<>();
    private String userId,targetUserId;
    private String listaAtual;
    private Boolean isOnFragment = false;
    private Fragment fragmentoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_favoritos);

        Intent intent = getIntent();
        listaAtual = intent.getStringExtra(getString(R.string.targetLista));
        if(listaAtual.equals(getString(R.string.targetlista_anunciosdastrados))){
            targetUserId = intent.getStringExtra(getString(R.string.targetlista_userid));
        }

        Toolbar mToolbar = findViewById(R.id.toolbar_com_backbtn);
        ImageView backBtn = mToolbar.findViewById(R.id.back_btn_appbar);
        TextView toolbarTitulo = mToolbar.findViewById(R.id.titulo_appbar_backbtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //SETUP LISTA DOS ANÚNCIOS:
        {
            listView = findViewById(R.id.listview_for_include);
            userId = FirebaseMetodos.getUserId();
            anunciosRef = FirebaseMetodos.getFirebaseData();

            switch (listaAtual){
                case "favoritos":
                    toolbarTitulo.setText("Favoritos");
                    setupListaAnunciosFavoritos();
                    break;
                case "anuncioscadastrados":
                    toolbarTitulo.setText("Anúncios Cadastrados");
                    setupAnunciosCadastrados();
                    break;
                case "seguidores":
                    toolbarTitulo.setText("Usuário Salvos");
                    setupListaSeguidores();
                    break;
                    default:
                        Toast.makeText(ListaFavoritosActivity.this, "Não foi possível carregar o conteúdo!", Toast.LENGTH_LONG).show();
            }


        }


    }

    private void setupListaSeguidores() {

        anunciosRef.child(getString(R.string.db_no_seguidores)).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String seguidorId = ds.getValue().toString();
                    listaseguidores.add(seguidorId);
                }
                SeguidoresAdapter seguidoresAdapter = new SeguidoresAdapter(ListaFavoritosActivity.this, R.layout.item_usuario_seguidor, listaseguidores);
                listView.setAdapter(seguidoresAdapter);
                setupClickListenersSeguidores(listaseguidores);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setupClickListenersSeguidores(List<String> listaseguidores) {

        final List<String> seguidores = listaseguidores;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog_user_profile dialogUser = new Dialog_user_profile();
                dialogUser.setUsuarioId(seguidores.get(position));
                dialogUser.show(getSupportFragmentManager(), "usuario_profile");
            }
        });
    }

    private void setupListaAnunciosFavoritos() {
        anunciosRef.child(getString(R.string.db_no_favoritos)).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    anuncio.setQuantidade(Integer.parseInt(anuncioData.get("quantidade").toString()));
                    anuncio.setArrecadado(Integer.parseInt(anuncioData.get("arrecadado").toString()));
                    anuncio.setPrazo(Integer.parseInt(anuncioData.get("prazo").toString()));

                    listaAnuncios.add(anuncio);
                }

                AnunciosAdapter itemsAdapter = new AnunciosAdapter(ListaFavoritosActivity.this, R.layout.item_anuncio_layout, listaAnuncios);
                listView.setAdapter(itemsAdapter);
                setupClickListenersAnuncios(listaAnuncios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupAnunciosCadastrados(){
        anunciosRef.child(getString(R.string.db_no_usuario_anuncios)).child(targetUserId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    anuncio.setQuantidade(Integer.parseInt(anuncioData.get("quantidade").toString()));
                    anuncio.setArrecadado(Integer.parseInt(anuncioData.get("arrecadado").toString()));
                    anuncio.setPrazo(Integer.parseInt(anuncioData.get("prazo").toString()));

                    listaAnuncios.add(anuncio);
                }

                AnunciosAdapter itemsAdapter = new AnunciosAdapter(ListaFavoritosActivity.this, R.layout.item_anuncio_layout, listaAnuncios);
                listView.setAdapter(itemsAdapter);
                setupClickListenersAnuncios(listaAnuncios);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void setupClickListenersAnuncios(List<Anuncio> listaAnuncios) {

        final List<Anuncio> anuncios = listaAnuncios;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String anunTipo = anuncios.get(position).getAnuncioType();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                switch (anunTipo){
                    case "doacao":
                        isOnFragment = true;
                        PaginaAnuncioDoaFragment paginaAnuncioDoaFragment = new PaginaAnuncioDoaFragment();
                        paginaAnuncioDoaFragment.setAnuncio(anuncios.get(position));
                        fragmentoAtual = paginaAnuncioDoaFragment;
                        fragmentTransaction.replace(R.id.layout_profile_fragment_container, paginaAnuncioDoaFragment)
                                .commit();
                        break;
                    case "solicitacao":
                        isOnFragment = true;
                        PaginaAnuncioSolFragment paginaAnuncioSolFragment =  new PaginaAnuncioSolFragment();
                        paginaAnuncioSolFragment.setAnuncio(anuncios.get(position));
                        fragmentoAtual = paginaAnuncioSolFragment;
                        fragmentTransaction.replace(R.id.layout_profile_fragment_container, paginaAnuncioSolFragment)
                                .commit();
                        break;
                    case "campanha":
                        isOnFragment = true;
                        PaginaAnuncioCampFragment paginaAnuncioCampFragment = new PaginaAnuncioCampFragment();
                        paginaAnuncioCampFragment.setAnuncio(anuncios.get(position));
                        fragmentoAtual = paginaAnuncioCampFragment;
                        fragmentTransaction.replace(R.id.layout_profile_fragment_container, paginaAnuncioCampFragment)
                                .commit();
                        break;
                    default:
                        Toast.makeText(ListaFavoritosActivity.this, "Erro ao carregar anúncio.", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isOnFragment){
            getSupportFragmentManager().beginTransaction().remove(fragmentoAtual).commit();
            getSupportFragmentManager().popBackStack();
            isOnFragment = false;
        } else super.onBackPressed();
    }
}
