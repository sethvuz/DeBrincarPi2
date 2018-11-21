package com.honda.debrincar.Activities.Fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.media.ExifInterface;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.google.firebase.storage.StorageReference;
import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.ImagemManipulations;
import com.honda.debrincar.Utilitarios.Permissoes;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastroDoacaoFragment extends Fragment {

    private static final String TAG = "CADASTRO_DOACAO";

    private int cont = 0, addImageCont = 0;
    private Anuncio anuncio = new Anuncio();
    private List<String> imagens = new ArrayList<>();
    private LinearLayout addImageContainer;
    private ConstraintLayout addImagem;

    private List<Uri> anuncioURIs = new ArrayList<>();
    private List<View> itemImageList = new ArrayList<>();

    /*private Uri[] anuncioURIs = new Uri[6];

    private ConstraintLayout[] imagemAnuncio = new ConstraintLayout[6];
    private CircleImageView[] itemImagem = new CircleImageView[6];
    private ImageView[] cancelBtn = new ImageView[6];*/




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


        //Permite a rolagem do campo de descrição
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

        /*
                imagemAnuncio[0] = view.findViewById(R.id.container_add_imagem_Doa_01);
                imagemAnuncio[1] = view.findViewById(R.id.container_add_imagem_Doa_02);
                imagemAnuncio[2] = view.findViewById(R.id.container_add_imagem_Doa_03);
                imagemAnuncio[3] = view.findViewById(R.id.container_add_imagem_Doa_04);
                imagemAnuncio[4] = view.findViewById(R.id.container_add_imagem_Doa_05);
                imagemAnuncio[5] = view.findViewById(R.id.container_add_imagem_Doa_06);


                itemImagem[0] = view.findViewById(R.id.cad_imagem_doa_01);
                itemImagem[1] = view.findViewById(R.id.cad_imagem_doa_02);
                itemImagem[2] = view.findViewById(R.id.cad_imagem_doa_03);
                itemImagem[3] = view.findViewById(R.id.cad_imagem_doa_04);
                itemImagem[4] = view.findViewById(R.id.cad_imagem_doa_05);
                itemImagem[5] = view.findViewById(R.id.cad_imagem_doa_06);


                cancelBtn[0] = view.findViewById(R.id.delete_image_cad_doa_01);
                        cancelBtn[0].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               onDelete(0);
                            }
                        });
                cancelBtn[1] = view.findViewById(R.id.delete_image_cad_doa_02);
                        cancelBtn[1].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onDelete(1);
                            }
                        });
                cancelBtn[2] = view.findViewById(R.id.delete_image_cad_doa_03);
                        cancelBtn[2].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onDelete(2);
                            }
                        });
                cancelBtn[3] = view.findViewById(R.id.delete_image_cad_doa_04);
                        cancelBtn[3].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onDelete(3);
                            }
                        });
                cancelBtn[4] = view.findViewById(R.id.delete_image_cad_doa_05);
                        cancelBtn[4].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onDelete(4);
                            }
                        });
                cancelBtn[5] = view.findViewById(R.id.delete_image_cad_doa_06);
                        cancelBtn[5].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onDelete(5);
                            }
                        });*/



        addImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cont < 6) {
                    checkVersaoAndroid();
                } else {
                    setAddImagemBtnGone();
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

    public void checkVersaoAndroid() {
        //PEDE PERMISSÃO SE A VERSÃO DO ANDROID FOR MENOR QUE A M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                requestPermissions(Permissoes.permissoesGetImagem, 555);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Erro de permissão!", Toast.LENGTH_LONG).show();
            }
        } else {
            showGetImagensDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 555 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showGetImagensDialog();
        } else {
            checkVersaoAndroid();
        }
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

        if (requestCode == GALERIA && resultCode == getActivity().RESULT_OK){
            if (data != null){
                Uri contentUri = data.getData();
                try{
                    criaImagemItem(contentUri);
                }catch (Exception e){

                }
            }
        }

        if (requestCode == CAMERA && resultCode == getActivity().RESULT_OK){
            if(data != null){
                try {
                    Bitmap itemImagem = (Bitmap) data.getExtras().get("data");
                    Uri contentUri = ImagemManipulations.getImageUri(getContext(), itemImagem);
                    criaImagemItem(contentUri);
                }catch (Exception e){

                }
            }
        }
    }

    public void criaImagemItem(Uri imagemUri){

        cont++;

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

        final CircleImageView itemImagem = imageView.findViewById(R.id.cad__model_imagem_anun_doa);

        //Verifica e corrige a rotação da imagem
        Float rotation = ImagemManipulations.setupRotation(ImagemManipulations.getImageBytes(getActivity(), imagemUri));

            Picasso.get()
                    .load(imagemUri)
                    .rotate(rotation)
                    .fit()
                    .centerCrop()
                    .into(itemImagem);

        // Define o OnClick do botão de deletar a imagem
        final ImageView cancelBtn = imageView.findViewById(R.id.delete_image_cad_anun_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove view do conteiner de imagens do anúncio
                onDelete(imageView);
            }
        });

        itemImageList.add(imageView);
        anuncioURIs.add(imagemUri);




        if(cont>= 6) {
            setAddImagemBtnGone();
        }
    }


    //Remove view do conteiner de imagens do anúncio
    public void onDelete(View view){



        int num = itemImageList.indexOf(view);
        itemImageList.remove(num);
        anuncioURIs.remove(num);

        addImageContainer.removeView(view);

        cont--;

        //Torna o botão visivel novamente para adicionar imagens
        if (cont < 6 && addImagem.getVisibility() == View.GONE){
            addImagem.setVisibility(View.VISIBLE);
        }

    }

    public void setAddImagemBtnGone(){
        addImagem.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Limite de 06 fotos por anúncio!", Toast.LENGTH_LONG).show();
    }

}
