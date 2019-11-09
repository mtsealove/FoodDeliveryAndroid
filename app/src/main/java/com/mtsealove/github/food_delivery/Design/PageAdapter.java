package com.mtsealove.github.food_delivery.Design;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.mtsealove.github.food_delivery.Fragments.ItemListFragment;

public class PageAdapter extends FragmentStatePagerAdapter {
    private int count;

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ItemListFragment itemListFragment = new ItemListFragment();
                return itemListFragment;
            case 1:
                ItemListFragment fragment = new ItemListFragment();
                return fragment;
        }
        return new ItemListFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
