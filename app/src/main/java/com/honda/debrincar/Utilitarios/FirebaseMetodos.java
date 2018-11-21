package com.honda.debrincar.Utilitarios;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.honda.debrincar.R;

public final class FirebaseMetodos {

    private static DatabaseReference referenciaFirebaseData;
    private static FirebaseAuth firebaseAuth;
    private static StorageReference referenciaFirebaseStorage;

    //caminho para salvar as fotos
    public final static String FIREBASE_IMAGE_STORAGE = "imagens/usuarios/";


    public static DatabaseReference getFirebaseData(){

        if (referenciaFirebaseData == null){
            referenciaFirebaseData = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebaseData;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    public static StorageReference getFirebaseStorage(){

        if (referenciaFirebaseStorage == null){
            referenciaFirebaseStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenciaFirebaseStorage;
    }

    public static boolean isLogged(){
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
            if (firebaseAuth.getCurrentUser() != null){
                return true;
            } else {
                return false;
            }

    }

    public static String getUserId(){
        String userId = getFirebaseAuth().getCurrentUser().getUid();
        return userId;
    }

    public static String getAnuncioId(Context context){
        String anuncioId = getFirebaseData().child(context.getString(R.string.db_no_anuncios)).push().getKey();
        return anuncioId;
    }

    public static void uploadImagem(){

    }


}
