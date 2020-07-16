package com.bytedance.androidcamp.network.dou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

public class VideoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.i("TAGv", "onCreateView: ");
        return inflater.inflate(R.layout.video_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Log.i("TAGv", "onActivityCreated: ");
        ViewPager viewPager = Objects.requireNonNull(getView()).findViewById(R.id.view_pager);
        TabLayout tabLayout = getView().findViewById(R.id.tabLayout);
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return new PlaceholderFragment(i);
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if(position == 0) {
                    return "所有人";
                }else{
                    return "我的上传";
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }


}
