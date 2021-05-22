package com.gachon.dawaga;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs; //tab의 갯수

    public MyPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Main_fragment1 tab1 = new Main_fragment1();
                return tab1;
            case 1:
                Main_fragment2 tab2 = new Main_fragment2();
                return tab2;
            case 2:
                Main_fragment3 tab3 = new Main_fragment3();
                return tab3;
            case 3:
                Main_fragment4 tab4 = new Main_fragment4();
                return tab4;
            default:
                return null;
        }
        //return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    // #SH
    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }
}
