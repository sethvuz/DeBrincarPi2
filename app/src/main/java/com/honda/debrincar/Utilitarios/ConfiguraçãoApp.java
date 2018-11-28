package com.honda.debrincar.Utilitarios;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public final class ConfiguraçãoApp {
    private static final String TAG = "ConfiguraçãoApp";

    public static HashMap<String, Integer> mapaEstados = new HashMap<>();
    public static HashMap<Integer, List<String>> mapaCidades = new HashMap<>();
    public static List<String> estados = new ArrayList<>();

    public static Boolean temSolicitacao = false;


    public static void setupEstadosCidades(){
        WebServiceData webServiceData = new WebServiceData();
        webServiceData.execute();

    }

    public void setArquivosEstados(Context context){
        try  {
            //FileOutputStream fileOutputStream = openFileOutput("lista_estados.txt", Context.MODE_PRIVATE);
            //FileOutputStream fileOutputStream = openFileOutput("mapa_estados.txt", Context.MODE_PRIVATE);
            FileOutputStream fileOutputStream = context.openFileOutput("mapa_cidades.txt", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(mapaCidades);
            out.close();
            fileOutputStream.close();

        }catch (IOException e){
            Log.d(TAG, "Erro ao salvar arquivo: " + e.getMessage());

        }
    }
    public static void getArquivoEstados(Context context){
        try {
            FileInputStream inputStreamEstados = context.openFileInput("lista_estados.txt");
            ObjectInputStream OISEstados = new ObjectInputStream(inputStreamEstados);
            estados = (ArrayList<String>) OISEstados.readObject();
            OISEstados.close();
            inputStreamEstados.close();

            FileInputStream inputStreamMapaEstados = context.openFileInput("mapa_estados.txt");
            ObjectInputStream OISMapaEstados = new ObjectInputStream(inputStreamMapaEstados);
            mapaEstados = (HashMap<String, Integer>) OISMapaEstados.readObject();
            OISMapaEstados.close();
            inputStreamMapaEstados.close();

            FileInputStream inputStreamMapaCidades = context.openFileInput("mapa_cidades.txt");
            ObjectInputStream OISMapaCidades = new ObjectInputStream(inputStreamMapaCidades);
            mapaCidades = (HashMap<Integer, List<String>>) OISMapaCidades.readObject();
            OISMapaCidades.close();
            inputStreamMapaCidades.close();

            //Toast.makeText(context, "Listas de estados gerada com sucesso!", Toast.LENGTH_LONG).show();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }




/*-------------------------------FUNÇÕES COM FRAGMENTOS------------------------------------------------------*/

    /*
    public static void carregaFragmento(FragmentManager fm, Fragment fragmento, int container ){
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(container, fragmento)
                .addToBackStack(null)
                .commit();
    }


    //FUNÇÃO PARA HABILITAR O ACESSO AO FRAGMENTO ANTERIOR POR MEIO DO BOTÃO DE VOLTAR.
    public static boolean defineFragmentoDeRetorno(FragmentManager fm){

        List<Fragment> fragments = fm.getFragments();
        Boolean resultado;

        if(fragments != null && !fragments.isEmpty()){
            for (Fragment fragment : fragments){
                if (fragment != null && fragment.isAdded() && fragment.getChildFragmentManager() != null){
                    //Vista os fragmentos filhos do atual.
                    resultado = defineFragmentoDeRetorno(fragment.getChildFragmentManager());

                    if(resultado)
                        return true;
                }
            }
        }

        if (fm.getBackStackEntryCount()>0){
            fm.popBackStack();
            fm.executePendingTransactions();
            return true;
        }

        return false;
    }*/

    /*-----------------------------FUNÇÕES DIVERSAS---------------------------------------*/

    //Função para retornar data

    public static String getData(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss'Z'", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        return sdf.format(new Date());
    }



}
