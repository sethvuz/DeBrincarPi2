package com.honda.debrincar.Activities.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.honda.debrincar.Objetos.PessoaFisica;
import com.honda.debrincar.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PessoaFragment extends Fragment {


    private PessoaFisica usuario = new PessoaFisica();

    private EditText nome;
    private EditText sobrenome;
    private EditText cpf;
    private EditText nascimento;
    private EditText endereco;
    private EditText cep;
    private EditText email;
    private EditText senha;
    private EditText confSenha;

    final static int GALLERY_PICK = 1;


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
                acessarGaleria();
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
                    usuario.setEndereço(endereco.getText().toString());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_PICK && resultCode== RESULT_OK && data!=null){

            Uri imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getContext(), this);
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (requestCode == RESULT_OK){

                Uri resultUri = result.getUri();
            }
        }
    }

    private void acessarGaleria() {
        Intent imageIntent = new Intent();
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent, GALLERY_PICK);
    }

    //FUNÇÃO DE CADASTRO DO USUÁRIO NO FIREBASE
    public void cadastraUsuario(String email, String senha){

        //Cria um novo usuário no Firebase
        Task<AuthResult> tarefa = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha);
        tarefa.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    //Resgata o ID do usuário cadastrado.
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuario.setId(usuarioFirebase.getUid());
                    usuario.salvarDados();//Função para salvar dados do usuário no Firebase

                    //Direciona para a página de anúncios.
                    Intent intent = new Intent("TELA_LOGIN_ACT");
                    intent.addCategory("TELA_LOGIN_CTG");
                    startActivity(intent);
                } else {
                    //Apresenta mensagem em casos de erro no cadastro.
                    Toast.makeText(getActivity(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
