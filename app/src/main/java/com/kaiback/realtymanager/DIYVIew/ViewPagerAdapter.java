package com.kaiback.realtymanager.DIYVIew;

//Move from WEI

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> list;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        // TODO Auto-generated constructor stub
        this.list = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);

        return fragment;
    }
}