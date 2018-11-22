package com.honda.debrincar.Utilitarios;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.Objetos.Usuario;
import com.honda.debrincar.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FirebaseMetodos {
    private static final String TAG = "FIREBASE_METODOS";

    private static DatabaseReference referenciaFirebaseData;
    private static FirebaseAuth firebaseAuth;
    private static StorageReference referenciaFirebaseStorage;

    //caminho para salvar as fotos
    public final static String FIREBASE_IMAGE_STORAGE = "usuarios/";


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

    public static void cadastraDadosUsuario(Context context, Usuario usuario){

        DatabaseReference dataref = getFirebaseData();

        String userId = usuario.getId();
        Map<String, Object> dataMap =  usuario.getMapaDados();

        dataref.child(context.getString(R.string.db_no_usuario)).child(userId).updateChildren(dataMap);

        if(usuario.getPessoaFisica()){
            dataref.child(context.getString(R.string.db_no_pessoafisica)).child(userId).setValue(userId);
        } else {
            dataref.child(context.getString(R.string.db_no_instituicao)).child(userId).setValue(userId);
        }

    }

    public static void salvaImagemUsuarioDataBase(Context context, Usuario usuario){

        DatabaseReference dataref = getFirebaseData();
        String userId = usuario.getId();

        dataref.child(context.getString(R.string.db_no_usuario)).child(userId).child("imagemUsuarioUrl").setValue(usuario.getImagemUsuarioUrl());

    }


    public static void uploadImagemAnuncio  (final List<Uri> URIs, final Anuncio anuncio, String storagePath, Context context, ProgressBar progressBar){

        final List<String> imagensURLs = new ArrayList<>();
        final List<Uri> anuncioURIs = URIs;
        final String anunID = anuncio.getAnuncioID();
        final Anuncio novoAnuncio = anuncio;
        final Context mContext = context;
        final ProgressBar pb = progressBar;

        String userId = getUserId();
        StorageReference imageStorageRef = getFirebaseStorage().child(FIREBASE_IMAGE_STORAGE).child(userId).child(anunID);
        StorageReference imageRef;

        for (int j = 0; j<URIs.size(); j++){

            final int i = j;

            imageRef = imageStorageRef.child("imagem" + (i+1) + ".jpg");
            imageRef.putFile(URIs.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "Imagem " + i + ": salva com sucesso!");
                    final Task<Uri> imageUrl = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d(TAG, "URL da imagem " + i + " salva com sucesso!");
                            imagensURLs.add(uri.toString());
                            if (i == (URIs.size()-1)){
                                anuncio.setImagensUrls(imagensURLs);
                                postarAnuncio(anuncio, mContext, pb);
                            }
                        }
                    });
                }
            });
        }
    }


    /**
     * Cria os seguintes nós no Firebase Database para os anúncios:
     * - anúncios
     * - imagens dos anúncios
     * - anuncios por usuário
     * - anuncios por categoria
     * - anuncios por tipo
     * @param anuncio
     * @param context
     */

    public static void postarAnuncio(Anuncio anuncio, Context context, ProgressBar progressBar) {

        final Context mContext = context;
        final ProgressBar pb = progressBar;

        DatabaseReference dataRef = FirebaseMetodos.getFirebaseData();
        String anuncioId = anuncio.getAnuncioID();
        String userId = getUserId();

        Map<String, Object> anuncioData = anuncio. getMapaDados();

        Map<String, Object> toFirebase = new HashMap<>();
        toFirebase.put(context.getString(R.string.db_no_anuncios) + "/" + anuncioId, anuncioData);
        toFirebase.put(context.getString(R.string.db_no_anuncios_imagens) + "/" + anuncioId, anuncio.getImagensUrls());
        toFirebase.put(context.getString(R.string.db_no_usuario_anuncios) + "/" + userId + "/" + anuncioId, anuncioId);
        toFirebase.put(context.getString(R.string.anun_doacao) + "/" + anuncioId, anuncioId);

        dataRef.updateChildren(toFirebase).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.d(TAG,"Anúncio cadastrado com sucesso!");

                    pb.setVisibility(View.GONE);

                    //Direciona para a página anúncios
                    Intent intent = new Intent("TELA_ANUNCIOS_ACT");
                    intent.addCategory("TELA_ANUNCIOS_CTG");
                    mContext.startActivity(intent);

                } else {
                    Log.d(TAG, "Falha em cadastrar o anúncio: " + task.getException().getMessage());
                }
            }
        });



    }

    public static void criaListaAnuncios(Context context){

        DatabaseReference anunciosRef = getFirebaseData();
        final List<Anuncio> listaAnuncios = new ArrayList<>();
        boolean listaPronta = false;

        anunciosRef.child(context.getString(R.string.db_no_anuncios)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Anuncio anuncio;
                HashMap<String, Object> anuncioData;
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    anuncioData = (HashMap<String, Object>) ds.getValue();
                    anuncio = new Anuncio(
                            anuncioData.get("titulo").toString(),
                            anuncioData.get("descricao").toString(),
                            anuncioData.get("TipoAnuncio").toString());
                    listaAnuncios.add(anuncio);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
