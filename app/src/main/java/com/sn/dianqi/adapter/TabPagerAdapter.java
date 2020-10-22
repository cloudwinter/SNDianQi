package com.sn.dianqi.adapter;

import android.content.Context;

import com.sn.dianqi.base.BaseFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager fragmentManager;

    private List<BaseFragment> fragmentList;


    public TabPagerAdapter(FragmentManager fragmentManager,List<BaseFragment> fragmentList) {
        super(fragmentManager);
        this.fragmentList = fragmentList;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
