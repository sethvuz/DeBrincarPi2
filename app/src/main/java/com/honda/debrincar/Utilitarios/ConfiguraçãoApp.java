package com.honda.debrincar.Utilitarios;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

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

    public static HashMap<String, Integer> mapaEstados = new HashMap<>();
    public static SparseArray<List<String>> mapaCidades = new SparseArray<>();
    public static List<String> estados = new ArrayList<>();


    public static void setupEstadosCidades(){
        WebServiceData webServiceData = new WebServiceData();
        webServiceData.execute();

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
