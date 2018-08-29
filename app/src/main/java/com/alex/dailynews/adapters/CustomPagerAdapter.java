package com.alex.dailynews.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.alex.dailynews.fragments.Favorite;
import com.alex.dailynews.fragments.ShowMyNews;
import com.alex.dailynews.fragments.TopHeadlines;

public class CustomPagerAdapter extends FragmentStatePagerAdapter {

    private final int mNumOfTabs;

    public CustomPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TopHeadlines();
            case 1:
                return new ShowMyNews();
            case 2:
                return new Favorite();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
