package com.example.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.os.Bundle;


import com.example.musicapp.R;
import com.example.musicapp.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class TestActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

    }

}