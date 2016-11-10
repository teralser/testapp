package testapp.acceptic.alext.testapp.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import testapp.acceptic.alext.testapp.fragments.BaseFragment;

/**
 * Created by Alex Tereshchenko on 09.11.2016.
 */

public class DummyPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> fragments;
    private String[] titles;

    public DummyPagerAdapter(FragmentManager fm,
                             @NonNull ArrayList<BaseFragment> fragments,
                             @NonNull String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
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
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
