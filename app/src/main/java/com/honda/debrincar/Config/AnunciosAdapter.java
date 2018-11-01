package com.honda.debrincar.Config;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnunciosAdapter extends ArrayAdapter<Anuncio> {
    public AnunciosAdapter(@NonNull Context context, int resource, @NonNull List<Anuncio> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null){

            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_anuncio_layout, parent, false);

        }

        Anuncio anuntioAtual = getItem(position);

        TextView anuncioTitulo = listItemView.findViewById(R.id.item_anun_titulo);
        anuncioTitulo.setText(anuntioAtual.getTitulo());

        TextView anuncioDescricao = listItemView.findViewById(R.id.item_anun_desc);
        anuncioDescricao.setText(anuntioAtual.getDescricao());

        LinearLayout layout_dados = listItemView.findViewById(R.id.item_anun_dados);
        layout_dados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("PAGINA_ANUNCIO_ACT");
                intent.addCategory("PAGINA_ANUNCIO_CTG");
                getContext().startActivity(intent);
            }
        });

        CircleImageView imagemItem = listItemView.findViewById(R.id.item_anun_imagem);
        imagemItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("PAGINA_ANUNCIO_ACT");
                intent.addCategory("PAGINA_ANUNCIO_CTG");
                getContext().startActivity(intent);
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

        Button botaoAcao = listItemView.findViewById(R.id.action_btn_anuncio);





        return listItemView;
    }
}
