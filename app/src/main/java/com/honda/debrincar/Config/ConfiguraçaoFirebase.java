package com.honda.debrincar.Config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguraçaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth firebaseAuth;

    public static DatabaseReference getFirebaseData(){

        if (referenciaFirebase == null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
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
