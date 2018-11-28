package com.honda.debrincar.Utilitarios;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Grafo {


    private List<String> usuarios = new ArrayList<>();
    private HashMap<String, List<String>> listasSeguidores = new HashMap<>();
    private Queue<String> fila = new LinkedList<>();
    private String usuarioInicio;
    private DatabaseReference seguidoresRef = FirebaseMetodos.getFirebaseData();
    private HashMap<String, String> distancia = new HashMap<>();
    private HashMap<String, String> visitados = new HashMap<>();


    public Grafo(String userCentro) {
        this.usuarioInicio = userCentro;
    }


    public void buscaLargura() {
        seguidoresRef.child("seguidores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    listasSeguidores = (HashMap<String, List<String>>) dataSnapshot.getValue();
                    percorreListas();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void percorreListas() {
        String v;
        List<String> usuarioSeguidores = new LinkedList<>();
        fila.add(usuarioInicio);
        int auxFilhos = 0, contDistancia = 0, posDistancia = 0, filhos =0;

        while (!fila.isEmpty()) {

            v = fila.poll();
            usuarioSeguidores = listasSeguidores.get("v");
            if (filhos != 0) {
                filhos--;
            }
            if (filhos == 0) {
                //1distancia[posDistancia] = contDistancia;
                posDistancia++;
            }
/*
            for (int i = 0; i<usuarioSeguidores.size(); i++) {
                if (visitados[usuarioSeguidores.get(i).getNum()] == -1) {
                    fila.add(usuarioSeguidores.get(i));
                    visitados[usuarioSeguidores.get(i).getNum()] = cont++;
                    auxFilhos++;
                }
            }*/
            if (filhos == 0) {
                filhos = auxFilhos;
                contDistancia = auxFilhos;
                auxFilhos = 0;
            }
        }
    }

    /*
    public void showListaAdj() {
        for (int i = 0; i<vertices.size(); i++)
            vertices.get(i).showAdj();
    }

    public Vertex getInicio() {
        return this.inicio;
    }

    public int getNumeroVertices() {
        return verticesEntrada.length;
    }*/

}
