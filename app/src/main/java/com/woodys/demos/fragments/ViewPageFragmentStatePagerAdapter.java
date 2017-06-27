package com.woodys.demos.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.woodys.lazyfragment.adapter.FixedFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by woodys on 2016/1/12.
 */
public class ViewPageFragmentStatePagerAdapter extends FixedFragmentStatePagerAdapter {
    private final List<Fragment> fragments;
    private final List<CharSequence> titles;

    public ViewPageFragmentStatePagerAdapter(FragmentManager fm, Fragment[] fragments, CharSequence[] titles) {
        this(fm, fragments, titles, null);
    }

    public ViewPageFragmentStatePagerAdapter(FragmentManager fm, Fragment[] fragments, CharSequence[] titles, int[] positions) {
        super(fm);
        this.fragments = new ArrayList<>();
        this.fragments.addAll(Arrays.asList(fragments));
        this.titles = new ArrayList<>();
        if (null != titles) {
            this.titles.addAll(Arrays.asList(titles));
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return !titles.isEmpty() ? titles.get(position) : super.getPageTitle(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * 替换数据源
     *
     * @param fragments
     */
    public void swapFragments(Fragment[] fragments) {
        if (null != fragments) {
            this.fragments.clear();
            this.fragments.addAll(Arrays.asList(fragments));
            notifyDataSetChanged();
        }
    }

    public void swapFragments(Fragment[] fragments, CharSequence[] titles) {
        if (null != fragments) {
            this.fragments.clear();
            this.fragments.addAll(Arrays.asList(fragments));
        }
        if (null != titles) {
            this.titles.clear();
            this.titles.addAll(Arrays.asList(titles));
        }
        notifyDataSetChanged();
    }

}
