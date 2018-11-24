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

import android.support.v4.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.ConfiguraçãoApp;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.honda.debrincar.Utilitarios.ImagemManipulations;
import com.honda.debrincar.Utilitarios.Permissoes;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastroDoacaoFragment extends Fragment {

    private static final String TAG = "CADASTRO_DOACAO";

    private int cont = 0;
    private Anuncio anuncio = new Anuncio();
    private LinearLayout addImageContainer;
    private ConstraintLayout addImagem;
    private List<Uri> anuncioURIs = new ArrayList<>();
    private List<View> itemImageList = new ArrayList<>();

    public ProgressBar progressBar;


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

        progressBar = view.findViewById(R.id.progress_bar_cad_doa);

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


                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                iniciaLoading();



                anuncio.setAnuncioType(getString(R.string.anun_doacao));
                anuncio.setTitulo(titulo.getText().toString());
                anuncio.setEndereco(endereco.getText().toString());
                anuncio.setTelefone(telefone.getText().toString());
                anuncio.setDescricao(descricao.getText().toString());

                anuncio.setDataCriacao(ConfiguraçãoApp.getData());

                anuncio.setAnuncioID(FirebaseMetodos.getAnuncioId(getActivity()));

                FirebaseMetodos.uploadImagemAnuncio(anuncioURIs, anuncio, FirebaseMetodos.FIREBASE_IMAGE_STORAGE, getActivity(), progressBar);

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



        itemImageList.add(imageView);
        anuncioURIs.add(imagemUri);

        // Define o OnClick do botão de deletar a imagem
        final ImageView cancelBtn = imageView.findViewById(R.id.delete_image_cad_anun_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove view do conteiner de imagens do anúncio
                onDelete(imageView);
            }
        });

        //Faz desaparecer o botão de adicionar imagem
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

    //FAZ DESAPARECER O BOTÃO DE ADICIONAR IMAGEM QUANDO CHEGADO O LIMITE DE IMAGENS
    public void setAddImagemBtnGone(){
        addImagem.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Limite de 06 fotos por anúncio!", Toast.LENGTH_LONG).show();
    }

    void iniciaLoading(){
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}
