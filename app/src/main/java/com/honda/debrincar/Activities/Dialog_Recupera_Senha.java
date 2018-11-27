package com.honda.debrincar.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.R;
import com.honda.debrincar.Utilitarios.FirebaseMetodos;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Dialog_Recupera_Senha extends DialogFragment {

    private static final String TAG = "Dialog_user_profile";





    @SuppressLint("NewApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogl_recupera_senha, container, false);

        final FirebaseAuth auth = FirebaseMetodos.getFirebaseAuth();
        final EditText emailUsuario = view.findViewById(R.id.email_recupera_senha);
        final Button btnRecuperaSenha = view.findViewById(R.id.btn_recupera_senha);

        btnRecuperaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailUsuario.getText().toString();
                if(!email.equals("")){
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getActivity(), "Um e-mail para resetar a senha foi enviado.", Toast.LENGTH_LONG).show();
                                dismiss();
                            } else
                            {
                                Toast.makeText(getActivity(),"Falha ao enviar e-mail: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(getActivity(), "Por favor, informe o e-mail.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastEsception" + e.getMessage());
        }
    }
}
