package com.honda.debrincar.Config;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.honda.debrincar.Activities.Fragments.Lista_Anuncios_Fragment;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioCampFragment;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioDoaFragment;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioSolFragment;
import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnunciosAdapter extends ArrayAdapter<Anuncio> {

    private Context atualContext;
    private Anuncio anuncioAtual;

    public AnunciosAdapter(@NonNull Context context, int resource, @NonNull List<Anuncio> objects) {
        super(context, resource, objects);

        atualContext = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null){

            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_anuncio_layout, parent, false);

        }

        anuncioAtual = getItem(position);

        TextView anuncioTitulo = listItemView.findViewById(R.id.item_anun_titulo);
        anuncioTitulo.setText(anuncioAtual.getTitulo());

        TextView anuncioDescricao = listItemView.findViewById(R.id.item_anun_desc);
        anuncioDescricao.setText(anuncioAtual.getDescricao());

        LinearLayout layout_dados = listItemView.findViewById(R.id.item_anun_dados);
        layout_dados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaFragmento();
            }
        });

        CircleImageView imagemItem = listItemView.findViewById(R.id.item_anun_imagem);
        imagemItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaFragmento();
            }
        });

        final ImageView coracaoFav = listItemView.findViewById(R.id.coracao_fav);
        coracaoFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(coracaoFav.getDrawable().getConstantState().equals(getContext().getResources().getDrawable(R.drawable.coracao_fav_branco).getConstantState()))
                {
                    Drawable imagem = getContext().getResources().getDrawable(R.drawable.coracao_fav_amarelo);
                    coracaoFav.setImageDrawable(imagem);
                }else
                    {
                    Drawable imagem = getContext().getResources().getDrawable(R.drawable.coracao_fav_branco);
                    coracaoFav.setImageDrawable(imagem);

                    }
            }
        });

        TextView textTipoAnuncio = listItemView.findViewById(R.id.tipo_anuncio);

        return listItemView;
    }

    private void chamaFragmento() {

        String anunType = anuncioAtual.getAnuncioType();

        FragmentManager fm = ((AppCompatActivity) atualContext).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        switch (anunType){
            case "DOAÇÃO":
                PaginaAnuncioDoaFragment paginaAnuncioDoaFragment = new PaginaAnuncioDoaFragment();
                fragmentTransaction.replace(R.id.container_principal, paginaAnuncioDoaFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case "SOLICITAÇÃO":
                PaginaAnuncioSolFragment paginaAnuncioSolFragment =  new PaginaAnuncioSolFragment();
                fragmentTransaction.replace(R.id.container_principal, paginaAnuncioSolFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case "CAMPANHA":
                PaginaAnuncioCampFragment paginaAnuncioCampFragment = new PaginaAnuncioCampFragment();
                fragmentTransaction.replace(R.id.container_principal, paginaAnuncioCampFragment)
                        .addToBackStack(null)
                        .commit();
                break;
                default:
                    Toast.makeText(getContext(), "Erro ao carregar anúncio.", Toast.LENGTH_LONG).show();
                    break;
        }



    }
}
