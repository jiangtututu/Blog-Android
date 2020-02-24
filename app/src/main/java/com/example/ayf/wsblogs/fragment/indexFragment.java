package com.example.ayf.wsblogs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ayf.wsblogs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2018/12/13.
 */

public class indexFragment extends Fragment {
    Context context;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Fragment> fragments;
    private MainAdapter adapter;
    String[] titile = {"热门", "推荐", "关注"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_index,container,false);
        mTabLayout = (TabLayout) v.findViewById(R.id.tablayout);
        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        fragments = new ArrayList<>();
        fragments.add(new fragment1());
        fragments.add(new fragment2());
        fragments.add(new fragment3());
        adapter = new MainAdapter(getChildFragmentManager(), fragments, titile);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        context = getActivity();
        return  v;
    }
}
