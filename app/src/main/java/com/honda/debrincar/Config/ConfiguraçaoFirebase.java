package com.honda.debrincar.Config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public final class ConfiguraçaoFirebase {

    private static DatabaseReference referenciaFirebaseData;
    private static FirebaseAuth firebaseAuth;
    private static StorageReference referenciaFirebaseStorage;

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


}
