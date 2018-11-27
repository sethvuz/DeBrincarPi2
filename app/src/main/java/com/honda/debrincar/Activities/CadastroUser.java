package com.honda.debrincar.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.honda.debrincar.Objetos.Usuario;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastroUser extends AppCompatActivity {

    //Variável que define se o usuário é Pessoa Física ou Instituição
    public String userType;

    private final static String TAG = "CADASTRO_USUARIO";

    private Usuario usuario = new Usuario();

    private EditText nome;
    private EditText sobrenome;
    private EditText cpf;
    private EditText cnpj;
    private EditText descricao;
    private EditText endereco;
    private EditText cep;
    private EditText telefone;
    private EditText email;
    private EditText senha;
    private EditText confSenha;
    private Uri imagemUsuario;

    private TextInputLayout sobrenomeContainer,
            cpfContainer,
            cnpjContainer,
            descricaoContainer;

    private StorageReference ImagemContaUsuarioRef;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_user);

        RadioButton radioButtonPF = findViewById(R.id.rb_pessoafisica);
        radioButtonPF.setChecked(true);

        //SETUP DOS CAMPOS
        //Todos
        nome = findViewById(R.id.cad_nome);
        endereco = findViewById(R.id.cad_endereco);
        cep = findViewById(R.id.cad_cep);
        telefone = findViewById(R.id.cad_telefone);


        //Pessoa Física
        sobrenome = findViewById(R.id.cad_sobrenome);
        sobrenomeContainer = findViewById(R.id.cad_sobrenome_container);
        cpf = findViewById(R.id.cad_cpf);
        cpfContainer = findViewById(R.id.cad_cpf_container);

        //Pessoa Jurídica
        cnpj = findViewById(R.id.cad_cnpj);
        cnpjContainer = findViewById(R.id.cad_cnpj_container);
        descricao = findViewById(R.id.cad_descricao);
        descricaoContainer = findViewById(R.id.cad_descricao_container);



        //CAMPOS PARA LOGIN
        email = findViewById(R.id.cad_email);
        senha = findViewById(R.id.cad_senha);
        confSenha = findViewById(R.id.cad_conf_senha);

        //CARREGAR IMAGEM
        CircleImageView userImage = findViewById(R.id.set_imagem);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkVersaoAndroid();
            }
        });

        //Funções de cadastro quando o botão de cadastrar é pressionado
        Button btnCadastrar = findViewById(R.id.btn_cadastro);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                String confirmaSenha = confSenha.getText().toString();
                if (usuario.getSenha().equals(confirmaSenha)){

                    //SETA OS DADOS DO USUÁRIO.
                    if(userType.equals(getString(R.string.profile_tipo_pessoa))) {
                        usuario.setPessoaFisica(true);
                        usuario.setNome(nome.getText().toString());
                        usuario.setSobrenome(sobrenome.getText().toString());
                        usuario.setCpf(cpf.getText().toString());
                        usuario.setEndereco(endereco.getText().toString());
                        usuario.setCep(cep.getText().toString());
                        usuario.setTelefone(telefone.getText().toString());
                    }else {
                        usuario.setPessoaFisica(false);
                        usuario.setNome(nome.getText().toString());
                        usuario.setCnpj(cnpj.getText().toString());
                        usuario.setDescricao(descricao.getText().toString());
                        usuario.setEndereco(endereco.getText().toString());
                        usuario.setCep(cep.getText().toString());
                        usuario.setTelefone(telefone.getText().toString());
                    }

                    //FUNÇÃO DE CADASTRO DO USUÁRIO NO FIREBASE.
                    cadastraUsuario(usuario.getEmail(), usuario.getSenha());
                }else {
                    Toast.makeText(CadastroUser.this,
                            "Confirmação da senha não confere!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void setPessoaFisica(View view){
        sobrenomeContainer.setVisibility(View.VISIBLE);
        cpfContainer.setVisibility(View.VISIBLE);
        cnpjContainer.setVisibility(View.GONE);
        descricaoContainer.setVisibility(View.GONE);
        userType = getString(R.string.profile_tipo_pessoa);
    }

    public void setPessoaJuridica(View view){
        sobrenomeContainer.setVisibility(View.GONE);
        cpfContainer.setVisibility(View.GONE);
        cnpjContainer.setVisibility(View.VISIBLE);
        descricaoContainer.setVisibility(View.VISIBLE);
        userType = getString(R.string.profile_tipo_instituicao);
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

        CropImage.startPickImageActivity(CadastroUser.this);

    }

    //MÉTODO PARA REQUERIR O CORTE DA IMAGEM
    private void croprequest(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .setMultiTouchEnabled(true)
                .start(CadastroUser.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //RESULT FROM SELECTED IMAGE
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(CadastroUser.this, data);
            croprequest(imageUri);
        }

        //RESULT FROM CROPING ACTIVITY
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
                    imagemUsuario = result.getUri();
                    //Coloca a imagem no fragmento de cadastro, visivel ao usuário
                    ((ImageView)CadastroUser.this.findViewById(R.id.set_imagem)).setImageBitmap(bitmap);

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
        progressBar = findViewById(R.id.pb_pessoafisica);
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
                    FirebaseMetodos.cadastraDadosUsuario(CadastroUser.this, usuario);//Função para salvar dados do usuário no Firebase

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
                                                FirebaseMetodos.salvaImagemUsuarioDataBase(CadastroUser.this, usuario);
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

                        FirebaseMetodos.salvaImagemUsuarioDataBase(CadastroUser.this, usuario);

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
}
