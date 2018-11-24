package com.honda.debrincar.Utilitarios;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.honda.debrincar.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagemAdapter extends PagerAdapter {

    Activity activity;
    List<String> imagens;
    LayoutInflater inflater;

    public ImagemAdapter(Activity activity, List<String> imagens) {
        this.activity = activity;
        this.imagens = imagens;
    }

    @Override
    public int getCount() {
        return imagens.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_imagem_anuncio, container, false);

        ImageView imagem = itemView.findViewById(R.id.item_imagem_pager);


        Picasso.get()
                .load(imagens.get(position))
                .into(imagem);

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }
}


