package com.example.musicapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.musicapp.fragments.fragment_chi_tiet_bh.BaiHatFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BinhLuanFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.LoiBaiHatFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.ThongTinBaiHatFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new BaiHatFragment();
            case 2:
                return new LoiBaiHatFragment();
            case 3:
                return new BinhLuanFragment();
            default:
                return new ThongTinBaiHatFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "•";
                break;
            case 1:
                title = "•";
                break;
            case 2:
                title = "•";
                break;
            case 3:
                title = "•";
                break;
        }
        return title;
    }
}
