package com.honda.debrincar.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
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

public class CadastroAnuncio extends AppCompatActivity {

    private static final String TAG = "CadastroAnuncio";

    private String tipoAnuncio;

    private int cont = 0;
    private Anuncio anuncio = new Anuncio();
    private LinearLayout addImageContainer;
    private ConstraintLayout addImagem;
    private List<Uri> anuncioURIs = new ArrayList<>();
    private List<View> itemImageList = new ArrayList<>();

    private String categoriaAnuncio;
    private int quantidadeNum = 1;
    private int prazoCamp = 0;

    public ProgressBar progressBar;


    private int GALERIA = 1, CAMERA = 2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_anuncio);

        //Captura o dado passado pela intent do DialogFragment e salva
        //na variável userType
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tipoAnuncio = bundle.getString("anuncioType");

        progressBar = findViewById(R.id.progress_bar_cad_doa);

        final EditText titulo = findViewById(R.id.cad_titulo_anun_doa);
        final EditText endereco = findViewById(R.id.cad_endereco_anuncio_doa);
        final EditText descricao = findViewById(R.id.cad_descricao_anuncio_doa);
        final ScrollView scrollView = findViewById(R.id.scroll_layout_cad_doa);

        //SETUP DOS SPINNERS
        {
            final Spinner spinnerCategoria = findViewById(R.id.spinner_categoria_item_doa);
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(CadastroAnuncio.this, R.array.categorias_item, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategoria.setAdapter(spinnerAdapter);

            spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    categoriaAnuncio = parent.getItemAtPosition(position).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        //SETUP DOS BOTÕES PARA DEFINIR QUANTIDADE
        {
            final TextView quantidadeContainer = findViewById(R.id.num_quantidade_cad_doa);
            final Button quantMais = findViewById(R.id.btn_mais_cad_doa);
            final Button quantMenos = findViewById(R.id.btn_menos_cad_doa);

            quantMais.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantidadeNum++;
                    quantidadeContainer.setText(String.valueOf(quantidadeNum));
                }
            });

            quantMenos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quantidadeNum>1){
                        quantidadeNum--;
                        quantidadeContainer.setText(String.valueOf(quantidadeNum));
                    } else {
                        Toast.makeText(CadastroAnuncio.this, "É preciso pelo menos 01 item!", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

        //SETUP SPINNER PARA PRAZO, NOS CASOS DE CAMPANHAS
        if (tipoAnuncio.equals(getString(R.string.anun_campanha))){

            final Spinner spinnerPrazoCamp = findViewById(R.id.spinner_prazo_camp);
            final ConstraintLayout spinnerPrazoContainer = findViewById(R.id.spinner_prazo_camp_container);
            spinnerPrazoContainer.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(CadastroAnuncio.this, R.array.prazo_campanha, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerPrazoCamp.setAdapter(spinnerAdapter);

            spinnerPrazoCamp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    prazoCamp = Integer.parseInt(parent.getItemAtPosition(position).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }



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

        Button cadBtn = findViewById(R.id.cad_anuncio_doa_btn);

        //Container com as imagens do anúncio
        addImageContainer = findViewById(R.id.container_principal_addImage_cad_doa);

        //ImageView com onClick para adicionar imagem
        addImagem = findViewById(R.id.container_add_imagem_cad_Doa);
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



                anuncio.setAnuncioType(tipoAnuncio);
                anuncio.setTitulo(titulo.getText().toString());
                anuncio.setEndereco(endereco.getText().toString());
                anuncio.setDescricao(descricao.getText().toString());
                anuncio.setDataCriacao(ConfiguraçãoApp.getData());
                anuncio.setUserID(FirebaseMetodos.getUserId());
                anuncio.setAnuncioID(FirebaseMetodos.getAnuncioId(CadastroAnuncio.this));
                anuncio.setCategoria(categoriaAnuncio);

                if (tipoAnuncio.equals(getString(R.string.anun_campanha))){
                    anuncio.setPrazo(prazoCamp);
                }

                FirebaseMetodos.uploadImagemAnuncio(anuncioURIs, anuncio, FirebaseMetodos.FIREBASE_IMAGE_STORAGE, CadastroAnuncio.this, progressBar);
            }
        });
    }


    public void checkVersaoAndroid() {
        //PEDE PERMISSÃO SE A VERSÃO DO ANDROID FOR MENOR QUE A M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                requestPermissions(Permissoes.permissoesGetImagem, 555);
            } catch (Exception e) {
                Toast.makeText(CadastroAnuncio.this, "Erro de permissão!", Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder getImagensDialog = new AlertDialog.Builder(CadastroAnuncio.this);
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

        if (resultCode == RESULT_CANCELED){
            return;
        }

        if (requestCode == GALERIA && resultCode == RESULT_OK){
            if (data != null){
                Uri contentUri = data.getData();
                try{
                    criaImagemItem(contentUri);
                }catch (Exception e){

                }
            }
        }

        if (requestCode == CAMERA && resultCode == RESULT_OK){
            if(data != null){
                try {
                    Bitmap itemImagem = (Bitmap) data.getExtras().get("data");
                    Uri contentUri = ImagemManipulations.getImageUri(CadastroAnuncio.this, itemImagem);
                    criaImagemItem(contentUri);
                }catch (Exception e){

                }
            }
        }
    }

    public void criaImagemItem(Uri imagemUri){

        cont++;

        //Gera um Inflater para criar a view com a imagem
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        Float rotation = ImagemManipulations.setupRotation(ImagemManipulations.getImageBytes(CadastroAnuncio.this, imagemUri));


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
        Toast.makeText(CadastroAnuncio.this, "Limite de 06 fotos por anúncio!", Toast.LENGTH_LONG).show();
    }

    void iniciaLoading(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
