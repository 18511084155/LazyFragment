package com.woodys.demos.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.woodys.lazyfragment.adapter.FixedFragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by woodys on 2016/1/12.
 */
public class MyFragmentStatePagerAdapter extends FixedFragmentStatePagerAdapter {
    private List<Fragment> fragmentList;

    public MyFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
