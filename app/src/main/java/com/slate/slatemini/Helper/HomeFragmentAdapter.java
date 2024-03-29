package com.slate.slatemini.Helper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.slate.slatemini.Fragments.HomeFragment;
import com.slate.slatemini.Fragments.WidgetFragment;

public class HomeFragmentAdapter extends FragmentPagerAdapter {

    public HomeFragmentAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
       switch (position)
       {
           case 0:return new HomeFragment();
           case 1: return new WidgetFragment();
           default:return new HomeFragment();
       }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
