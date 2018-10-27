package com.honda.debrincar.Config;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.honda.debrincar.Activities.Fragments.Anun_CampanhaFragment;
import com.honda.debrincar.Activities.Fragments.Anun_DoacaoFragment;
import com.honda.debrincar.Activities.Fragments.Anun_SolicitacaoFragment;

public class Anun_Adapter extends FragmentPagerAdapter {

    private String[] tabTitulos;

    public Anun_Adapter(FragmentManager fm, String[] tabTitulos){
        super(fm);
        this.tabTitulos = tabTitulos;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new Anun_DoacaoFragment();
            case 1:
                return new Anun_SolicitacaoFragment();
            case 2:
                return new Anun_CampanhaFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitulos.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitulos[position];
    }
}
