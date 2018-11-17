package com.honda.debrincar.Activities.Fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastroDoacaoFragment extends Fragment implements View.OnClickListener {


    private int cont = 0;
    private Anuncio anuncio = new Anuncio();
    private List<String> imagens = new ArrayList<>();
    private LinearLayout addImageContainer;
    private ConstraintLayout addImagem;

    public CadastroDoacaoFragment() {
        // Required empty public constructor
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cadatro_doacao, container, false);

        final EditText titulo = view.findViewById(R.id.cad_titulo_anun_doa);
        final EditText endereco = view.findViewById(R.id.cad_endereco_anuncio_doa);
        final EditText telefone = view.findViewById(R.id.cad_telefone_anuncio_doa);
        final EditText descricao = view.findViewById(R.id.cad_descricao_anuncio_doa);
        final ScrollView scrollView = view.findViewById(R.id.scroll_layout_cad_doa);

        descricao.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    scrollView.requestDisallowInterceptTouchEvent(false);
                } else {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        TextView addLocal = view.findViewById(R.id.add_location_cad_doa);

        Button cadBtn = view.findViewById(R.id.cad_anuncio_doa_btn);

        addImageContainer = view.findViewById(R.id.container_principal_addImage_cad_doa);
        addImagem = view.findViewById(R.id.container_add_imagem_cad_Doa);

        addImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cont++;

                if(cont <= 6) {
                    //Gera um Inflater para criar a view com a imagem
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    //Cria a view e adiciona ao Linearlayout
                    final View imageView = inflater.inflate(R.layout.addimage_layout_model, null);
                    addImageContainer.addView(imageView);

                    //Define os parâmetros da view criada, alinhando-a no centro
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    lp.gravity = Gravity.CENTER;
                    lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    imageView.setLayoutParams(lp);

                    // Define o OnClick do botão de deletar a imagem
                    ImageView cancelBtn = imageView.findViewById(R.id.delete_image_cad_anun_btn);
                    cancelBtn.setOnClickListener(this);


                } else {
                    Toast.makeText(getContext(), "Limite de 06 fotos por anúncio!", Toast.LENGTH_LONG).show();
                }

                //Faz desaparecer e aparecer o botão de adicionar imagens caso o limite seja passado
                //ou imagens sejam deletadas
                if(cont>6) {
                    addImagem.setVisibility(View.GONE);
                } else if (cont <= 6 && addImagem.getVisibility() == View.GONE){
                    addImagem.setVisibility(View.VISIBLE);
                }
            }
        });

        cadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                anuncio.setTitulo(titulo.getText().toString());
                anuncio.setEndereco(endereco.getText().toString());
                anuncio.setTelefone(telefone.getText().toString());
                anuncio.setDescricao(descricao.getText().toString());

                LocalDate date = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    date = LocalDate.now();
                }
                anuncio.setDataCriacao(date.toString());


            }
        });



        return  view;
    }



    public void onDelete(View view){

        addImageContainer.removeView((View) view.getParent());
        cont--;

    }

    @Override
    public void onClick(View view) {
        onDelete(view);
    }
}
