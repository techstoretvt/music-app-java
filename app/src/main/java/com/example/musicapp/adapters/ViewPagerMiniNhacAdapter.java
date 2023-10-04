package com.example.musicapp.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.musicapp.fragments.fragment_mini_nhac.CurrentMiniNhacFragment;
import com.example.musicapp.fragments.fragment_mini_nhac.NextMiniNhacFragment;

public class ViewPagerMiniNhacAdapter extends FragmentStatePagerAdapter {

    public ViewPagerMiniNhacAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new NextMiniNhacFragment();
            default:
                return new CurrentMiniNhacFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
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
        }
        return title;
    }
}
