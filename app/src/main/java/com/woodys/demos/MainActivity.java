package com.woodys.demos;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.woodys.demos.fragments.MoreFragment;
import com.woodys.demos.fragments.ViewPageFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.viewPager2)
    ViewPager viewPager;
    @InjectView(R.id.tab_layout)
    TabLayout tabLayout;
    private List<Fragment> fragmentList;
    private ViewPageFragmentStatePagerAdapter myFragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        fragmentList = new ArrayList<>();
        fragmentList.add(MoreFragment.newInstance(1,false));
        fragmentList.add(MoreFragment.newInstance(2,true));
        fragmentList.add(MoreFragment.newInstance(3,false));
        fragmentList.add(MoreFragment.newInstance(4,false));
        fragmentList.add(MoreFragment.newInstance(5,true));
        fragmentList.add(MoreFragment.newInstance(6,false));
        fragmentList.add(MoreFragment.newInstance(7,true));
        initFragments(fragmentList.toArray(new Fragment[7]),new String[]{"条目1","条目2","条目3","条目4","条目5","条目6","条目7"});
    }

    private void initFragments(Fragment[] fragmentItems, String[] titles) {
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode((titles != null && titles.length > 3) ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
        if (null == myFragmentPagerAdapter) {
            viewPager.setAdapter(myFragmentPagerAdapter = new ViewPageFragmentStatePagerAdapter(getSupportFragmentManager(), fragmentItems, titles));
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            tabLayout.setupWithViewPager(viewPager, true);
        } else {
            myFragmentPagerAdapter.swapFragments(fragmentItems, titles);
        }

    }

}
