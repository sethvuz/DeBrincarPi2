package com.honda.debrincar.Activities.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastroDoacaoFragment extends Fragment {


    private int cont = 0;
    private Anuncio anuncio = new Anuncio();
    private List<String> imagens = new ArrayList<>();
    private LinearLayout addImageContainer;
    private ConstraintLayout addImagem;
    private Bitmap itemBitmap = null;

    private CircleImageView addImageCircle;



    private int GALERIA = 1, CAMERA = 2;

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

        //Container com as imagens do anúncio
        addImageContainer = view.findViewById(R.id.container_principal_addImage_cad_doa);
        //ImageView com onClick para adicionar imagem
        addImagem = view.findViewById(R.id.container_add_imagem_cad_Doa);


        addImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cont++;

                if(cont <= 6) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        try {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 555);
                            showGetImagensDialog();
                        } catch (Exception e) {

                        }
                    }


                    /*

                    //Gera um Inflater para criar a view com a imagem
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    //Cria a view e adiciona ao Linearlayout
                    final View imageView = inflater.inflate(R.layout.addimage_layout_model, null);
                    addImageContainer.addView(imageView);
                    final CircleImageView itemImagem = imageView.findViewById(R.id.cad__model_imagem_anun_doa);

                    //Define os parâmetros da view criada, alinhando-a no centro
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    lp.gravity = Gravity.CENTER;
                    lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
                    imageView.setLayoutParams(lp);


                    if (itemBitmap != null){
                        itemImagem.setImageBitmap(itemBitmap);
                    }
                    imageView.setVisibility(View.VISIBLE);
                    itemBitmap = null;


                    // Define o OnClick do botão de deletar a imagem
                    final ImageView cancelBtn = imageView.findViewById(R.id.delete_image_cad_anun_btn);
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //Remove view do conteiner de imagens do anúncio
                            onDelete(cancelBtn);
                        }
                    });*/
                }

                /*
                //Faz desaparecer o botão de adicionar imagens caso o limite seja passado
                if(cont>=6) {
                    addImagem.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Limite de 06 fotos por anúncio!", Toast.LENGTH_LONG).show();
                }*/


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

    private void showGetImagensDialog() {
        AlertDialog.Builder getImagensDialog = new AlertDialog.Builder(getContext());
        getImagensDialog.setTitle(R.string.titulo_getimage_dialog);
        String[] getImagensItens = {
                "Selecionar foto da galeria",
                "Capturar foto com a câmera"
        };

        getImagensDialog.setItems(getImagensItens, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){
                    case 0:
                        getImagemGaleria();
                        break;
                    case 1:
                        getImagemCamera();
                        break;

                }

            }
        });
        getImagensDialog.show();
    }

    private void getImagemGaleria() {
        Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intentGaleria, GALERIA);
    }

    private void getImagemCamera(){
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentCamera, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_CANCELED){
            return;
        }

        if (requestCode == GALERIA){
            if (data != null){
                Uri contentUri = data.getData();
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentUri);
                    itemBitmap = bitmap;
                    criaImagemItem(bitmap);
                }catch (IOException e){

                }
            }
        }
    }

    public void criaImagemItem(Bitmap bitmap){

        //Gera um Inflater para criar a view com a imagem
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Cria a view e adiciona ao Linearlayout
        View imageView = inflater.inflate(R.layout.addimage_layout_model, null);
        addImageContainer.addView(imageView);

        //Define os parâmetros da view criada, alinhando-a no centro
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        lp.gravity = Gravity.CENTER;
        lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
        imageView.setLayoutParams(lp);

        CircleImageView itemImagem = imageView.findViewById(R.id.cad__model_imagem_anun_doa);
        itemImagem.setImageBitmap(bitmap);


    }


    //Remove view do conteiner de imagens do anúncio
    public void onDelete(View view){

        addImageContainer.removeView((View) view.getParent());
        cont--;
        //Torna o botão visivel novamente para adicionar imagens
        if (cont <= 6 && addImagem.getVisibility() == View.GONE){
            addImagem.setVisibility(View.VISIBLE);
        }

    }

}
