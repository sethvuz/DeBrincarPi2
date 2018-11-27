package com.honda.debrincar.Activities.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.Objetos.Usuario;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";

    private String userId;
    private DatabaseReference userRef;
    private Usuario usuario;
    private HashMap<String, Object> userMapa = new HashMap<>();

    private TextView nomeProfile;
    private TextView emailProfile;
    private TextView enderecoProfile;
    private EditText novaSenha;
    private EditText confirmaNovasenha;
    private EditText novoEndereco;
    private CircleImageView profileFoto;

    private Boolean changeEndereco = false;
    private Boolean changeSenha = false;




    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        Toolbar mToolbar = view.findViewById(R.id.toolbar_com_backbtn);
        ImageView backBtn = mToolbar.findViewById(R.id.back_btn_appbar);
        TextView toolbarTitulo = mToolbar.findViewById(R.id.titulo_appbar_backbtn);
        toolbarTitulo.setText("Editar Cadastro");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        nomeProfile = view.findViewById(R.id.nome_edit_profile);
        emailProfile = view.findViewById(R.id.email_edit_profile);
        profileFoto = view.findViewById(R.id.edit_foto_profile);
        enderecoProfile = view.findViewById(R.id.endereco_edit_profile);
        novaSenha = view.findViewById(R.id.cad_nova_senha_edit_profile);
        confirmaNovasenha = view.findViewById(R.id.confirma_senha_edit_profile);
        novoEndereco = view.findViewById(R.id.textview_novo_email_profile);

        userId = FirebaseMetodos.getUserId();
        userRef = FirebaseMetodos.getFirebaseData();

        userRef.child(getString(R.string.db_no_usuario)).child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    userMapa = (HashMap<String, Object>) dataSnapshot.getValue();
                    String nome = userMapa.get("nome").toString() + " " + userMapa.get("sobrenome").toString();
                    nomeProfile.setText(nome);
                    //String email = userMapa.get("email").toString();
                    //emailProfile.setText(email);
                    String endereco = userMapa.get("endereco").toString();
                    enderecoProfile.setText(endereco);
                    String imagemUsuario = userMapa.get("imagemUsuarioUrl").toString();
                    setUserImage(imagemUsuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final TextView alteraEndereco = view.findViewById(R.id.altera_endereco_edit_profile);
        TextView cancelaAlteraEndereco = view.findViewById(R.id.cancel_altera_edit_profile);
        final ConstraintLayout alteraEnderecoContainer = view.findViewById(R.id.altera_endereco_container_editprofile);
        final LinearLayout enderecoContainer = view.findViewById(R.id.endereco_container_editprofile);
        alteraEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enderecoContainer.setVisibility(View.GONE);
                alteraEnderecoContainer.setVisibility(View.VISIBLE);
                changeEndereco = true;
            }
        });

        cancelaAlteraEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enderecoContainer.setVisibility(View.VISIBLE);
                alteraEnderecoContainer.setVisibility(View.GONE);
                changeEndereco = false;
            }
        });

        final TextView alteraSenhaBtn = view.findViewById(R.id.altera_senha_btn_editprofile);
        final TextView cancelAlteraSenha = view.findViewById(R.id.cancel_altera_senha_editprofile);
        final LinearLayout alteraSenhaContainer = view.findViewById(R.id.altera_senha_container_editprofile);
        alteraSenhaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alteraSenhaBtn.setVisibility(View.GONE);
                alteraSenhaContainer.setVisibility(View.VISIBLE);
                changeSenha = true;
            }
        });

        cancelAlteraSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alteraSenhaBtn.setVisibility(View.VISIBLE);
                alteraSenhaContainer.setVisibility(View.GONE);
                changeSenha = false;
            }
        });

        final Button aplicaBtn = view.findViewById(R.id.btn_edit_profile);
        aplicaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> changesMapa = new HashMap<>();
                Boolean senhoOk = false;

                if (changeEndereco){
                    String endereco = novoEndereco.getText().toString();
                    changesMapa.put("endereco", endereco);
                    userRef.child(getString(R.string.db_no_usuario)).child(userId).child("endereco").setValue(endereco).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Endereço alterado com sucesso!", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                if(changeSenha){
                    String senha = novaSenha.getText().toString();
                    String confSenha = confirmaNovasenha.getText().toString();
                    if(senha.equals(confSenha)){
                        senhoOk = true;
                    } else {
                        Toast.makeText(getActivity(), "Senhas não conferem!", Toast.LENGTH_LONG).show();
                    }

                    if(senhoOk){
                        FirebaseMetodos.getFirebaseAuth().getCurrentUser().updatePassword(senha).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getActivity(), "Senha alterada com sucesso!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }


            }
        });

        return view;
    }

    private void setUserImage(String imagemUsuario) {
        Picasso.get()
                .load(imagemUsuario)
                .fit()
                .centerCrop()
                .into(profileFoto);
    }

}
