package com.honda.debrincar.Activities.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.honda.debrincar.BuildConfig;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.honda.debrincar.Objetos.Usuario;
import com.honda.debrincar.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PessoaFragment extends Fragment {

    private final static String TAG = "CADASTRO_USUARIO";

    private Usuario usuario = new Usuario();

    private EditText nome;
    private EditText sobrenome;
    private EditText cpf;
    private EditText nascimento;
    private EditText endereco;
    private EditText cep;
    private EditText email;
    private EditText senha;
    private EditText confSenha;
    private Uri imagemUsuario;

    private StorageReference ImagemContaUsuarioRef;

    private ProgressBar progressBar;




    public PessoaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and save it on a View
        View view =  inflater.inflate(R.layout.fragment_pessoa, container, false);


        //CARREGAR IMAGEM
        CircleImageView userImage = view.findViewById(R.id.set_imagem);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVersaoAndroid();
            }
        });

        //Funções de cadastro quando o botão de cadastrar é pressionado
        Button btnCadastrar = view.findViewById(R.id.btn_cadastro);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SALVA OS CAMPOS EM VARIÁVEIS.
                nome = getActivity().findViewById(R.id.cad_nome);
                sobrenome = getActivity().findViewById(R.id.cad_sobrenome);
                cpf = getActivity().findViewById(R.id.cad_cpf);
                nascimento = getActivity().findViewById(R.id.cad_nascimento);
                endereco = getActivity().findViewById(R.id.cad_endereco);
                cep = getActivity().findViewById(R.id.cad_cep);

                //CAMPOS PARA LOGIN
                email = getActivity().findViewById(R.id.cad_email);
                senha = getActivity().findViewById(R.id.cad_senha);
                confSenha = getActivity().findViewById(R.id.cad_conf_senha);

                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                String confirmaSenha = confSenha.getText().toString();
                if (usuario.getSenha().equals(confirmaSenha)){

                    //SETA OS DADOS DO USUÁRIO.
                    usuario.setNome(nome.getText().toString());
                    usuario.setSobrenome(sobrenome.getText().toString());
                    usuario.setCpf(cpf.getText().toString());
                    usuario.setDataNascimento(nascimento.getText().toString());
                    usuario.setEndereco(endereco.getText().toString());
                    usuario.setCep(cep.getText().toString());

                    //FUNÇÃO DE CADASTRO DO USUÁRIO NO FIREBASE.
                    cadastraUsuario(usuario.getEmail(), usuario.getSenha());
                }else {
                    Toast.makeText(getActivity(),
                            "Confirmação da senha não confere!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void checkVersaoAndroid(){
        //PEDE PERMISSÃO SE A VERSÃO DO ANDROID FOR MENOR QUE A M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
            }catch (Exception e){

            }
        } else {
            pickImage();
        }

    }

    //MÉTODO PARA PEGAR A IMAGEM
    private void pickImage() {

        CropImage.startPickImageActivity(getContext(), this);

    }

    //MÉTODO PARA REQUERIR O CORTE DA IMAGEM
    private void croprequest(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .setMultiTouchEnabled(true)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       super.onActivityResult(requestCode, resultCode, data);
        //RESULT FROM SELECTED IMAGE
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
            croprequest(imageUri);
        }

        //RESULT FROM CROPING ACTIVITY
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getUri());
                    imagemUsuario = result.getUri();
                    //Coloca a imagem no fragmento de cadastro, visivel ao usuário
                    ((ImageView)getActivity().findViewById(R.id.set_imagem)).setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 555 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            checkVersaoAndroid();
        }
    }


    //FUNÇÃO DE CADASTRO DO USUÁRIO NO FIREBASE
    public void cadastraUsuario(String email, String senha){

        //INICIALIZA A BARRA DE CARREGAMENTO
        progressBar = getActivity().findViewById(R.id.pb_pessoafisica);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        //Cria um novo usuário no Firebase
        final Task<AuthResult> tarefa = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha);
        tarefa.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    //Resgata o ID do usuário cadastrado.
                    String userID = FirebaseMetodos.getUserId();
                    usuario.setId(userID);
                    FirebaseMetodos.cadastraDadosUsuario(getActivity(), usuario);//Função para salvar dados do usuário no Firebase

                    //Salva imagem do usuário no FireStorage
                        ImagemContaUsuarioRef = FirebaseMetodos.getFirebaseStorage().child(FirebaseMetodos.FIREBASE_IMAGE_STORAGE);
                        StorageReference pastaStorage = ImagemContaUsuarioRef.child(usuario.getId()).child("imagemUsuario.jpg");

                    if(imagemUsuario != null) {
                        pastaStorage.putFile(imagemUsuario).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Imagem do usuário salva com sucesso!");
                                    //Toast.makeText(getActivity(), "Imagem do usuário salva com sucesso!", Toast.LENGTH_LONG).show();
                                    Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                usuario.setImagemUsuarioUrl(task.getResult().toString());
                                                FirebaseMetodos.salvaImagemUsuarioDataBase(getActivity(), usuario);
                                                Log.d(TAG, "Url da imagem salva com sucesso!");
                                                //Toast.makeText(getActivity(), "Url salva com sucesso!", Toast.LENGTH_LONG).show();

                                                progressBar.setVisibility(View.GONE);

                                                //Direciona para a página anúncios
                                                Intent intent = new Intent("TELA_ANUNCIOS_ACT");
                                                intent.addCategory("TELA_ANUNCIOS_CTG");
                                                startActivity(intent);
                                            } else {
                                                Log.d(TAG, "Falha ao salvar a URL da imagem: " + task.getException().getMessage());
                                                //Toast.makeText(getActivity(), "Falha em salvar a Url da imagem", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "Falha ao salvar a imagem so usuário: " + task.getException().getMessage());
                                    //Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    } else {

                        FirebaseMetodos.salvaImagemUsuarioDataBase(getActivity(), usuario);

                        progressBar.setVisibility(View.GONE);

                        //Direciona para a página anúncios
                        Intent intent = new Intent("TELA_ANUNCIOS_ACT");
                        intent.addCategory("TELA_ANUNCIOS_CTG");
                        startActivity(intent);
                    }




                } else {
                    //Apresenta mensagem em casos de erro no cadastro.
                    Log.d(TAG, "Erro ao cadastrar usuário: " + task.getException().getMessage());
                    //Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /*
    private Uri getUriresource() {
        Uri uri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.drawable.carinha_adc_foto);
        return uri;
    }*/

}
