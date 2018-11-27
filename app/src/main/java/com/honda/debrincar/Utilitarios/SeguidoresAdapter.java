package com.honda.debrincar.Utilitarios;

import android.content.ContentResolver;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.Objetos.Usuario;
import com.honda.debrincar.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeguidoresAdapter extends ArrayAdapter<String> {

    private DatabaseReference dataRef = FirebaseMetodos.getFirebaseData();
    private Context contextoAtual;


    public SeguidoresAdapter(@NonNull Context context, int resource, @NonNull  List<String> objects) {
        super(context, resource, objects);
        contextoAtual = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,  @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null){

            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_anuncio_layout, parent, false);

        }

        final TextView nomeSeguidor = listItemView.findViewById(R.id.item_nome_seguidor);
        final TextView localSeguidor = listItemView.findViewById(R.id.item_seguidor_cidade);
        final CircleImageView imagemSeguidor = listItemView.findViewById(R.id.item_seguidor_imagem);


        String seguidorId = getItem(position);
        dataRef.child(contextoAtual.getString(R.string.db_no_usuario)).child(seguidorId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String nome, local, imagemUrl;
                    nome = dataSnapshot.child("nome").getValue().toString() +
                            " " +
                            dataSnapshot.child("sobrenome").getValue().toString();
                    nomeSeguidor.setText(nome);
                    imagemUrl = dataSnapshot.child("imagemUsuarioUrl").getValue().toString();
                    Picasso.get()
                            .load(imagemUrl)
                            .fit()
                            .centerCrop()
                            .into(imagemSeguidor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return listItemView;
    }
}
