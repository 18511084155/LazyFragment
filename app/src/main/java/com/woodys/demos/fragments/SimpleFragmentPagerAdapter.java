package com.woodys.demos.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 可编辑fragment条目的viewpager的adapter
 *
 * @author Administrator
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragments;
    private final List<CharSequence> titles;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Fragment[] fragments) {
        this(fm, fragments, null, null);
    }

    public SimpleFragmentPagerAdapter(FragmentManager fm, Fragment[] fragments, CharSequence[] titles) {
        this(fm, fragments, titles, null);
    }

    public SimpleFragmentPagerAdapter(FragmentManager fm, Fragment[] fragments, CharSequence[] titles, int[] positions) {
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

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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


    @Override
    public void finishUpdate(ViewGroup container) {
        try {
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException) {
            System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
