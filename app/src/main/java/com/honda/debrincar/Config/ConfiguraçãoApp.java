package com.honda.debrincar.Config;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

public final class ConfiguraçãoApp {




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
    }

}
