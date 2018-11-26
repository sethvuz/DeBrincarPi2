package com.honda.debrincar.Utilitarios;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioCampFragment;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioDoaFragment;
import com.honda.debrincar.Activities.Fragments.PaginaAnuncioSolFragment;
import com.honda.debrincar.Objetos.Anuncio;
import com.honda.debrincar.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnunciosAdapter extends ArrayAdapter<Anuncio> {


    public AnunciosAdapter(@NonNull Context context, int resource, @NonNull List<Anuncio> objects) {
        super(context, resource, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null){

            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_anuncio_layout, parent, false);

        }

        Anuncio anuncioAtual = getItem(position);
        String anuncioType = anuncioAtual.getAnuncioType();
        anuncioAtual.setAnuncioType(anuncioType);

        TextView anuncioTitulo = listItemView.findViewById(R.id.item_anun_titulo);
        anuncioTitulo.setText(anuncioAtual.getTitulo());

        TextView anuncioDescricao = listItemView.findViewById(R.id.item_anun_desc);
        anuncioDescricao.setText(anuncioAtual.getDescricao());

        TextView textTipoAnuncio = listItemView.findViewById(R.id.tipo_anuncio);

        CircleImageView imagemItem = listItemView.findViewById(R.id.item_anun_imagem);


        switch (anuncioType){
            case "doacao":
                textTipoAnuncio.setText("DOAÇÃO");
                textTipoAnuncio.setTextColor(ContextCompat.getColor(getContext(), R.color.amareloEscuro));
                textTipoAnuncio.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.azulMedio));
                break;
            case "solicitacao":
                textTipoAnuncio.setText("SOLICITAÇÃO");
                textTipoAnuncio.setTextColor(ContextCompat.getColor(getContext(), R.color.azulMedio));
                textTipoAnuncio.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.amareloEscuro));
                break;
            case "campanha":
                textTipoAnuncio.setText("CAMPANHA");
                textTipoAnuncio.setTextColor(ContextCompat.getColor(getContext(), R.color.amareloClaro));
                textTipoAnuncio.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.azulClaro));
                break;
            default:
                Toast.makeText(getContext(), "Erro ao carregar anuncios.", Toast.LENGTH_LONG).show();
                break;
        }

        return listItemView;
    }
}
