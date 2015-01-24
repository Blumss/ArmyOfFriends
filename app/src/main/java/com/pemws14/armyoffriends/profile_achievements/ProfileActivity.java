package com.pemws14.armyoffriends.profile_achievements;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pemws14.armyoffriends.R;
import com.pemws14.armyoffriends.drawer.BaseActivity;


public class ProfileActivity extends BaseActivity {
    private View view;

    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //<-- IN EVERY ACTIVITY WITH DRAWER
        super.set();
        // catches Frame, where to insert actual ActivityView
        ViewGroup parent = (ViewGroup) findViewById(R.id.content_frame);
        //View of new activity
        view = LayoutInflater.from(this).inflate(R.layout.activity_profile, parent, false);
        parent.addView(view);
        //--> IN EVERY ACTIVITY WITH DRAWER

        mCustomPagerAdapter = new CustomPagerAdapter(getFragmentManager(), this);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }

    class CustomPagerAdapter extends FragmentPagerAdapter {

        Context mContext;

        public CustomPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ProfileFragment();
            if(position==0){
            }else if (position == 1){
                fragment = new AchievementsFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0){
                return "Details";
            }else{
                return "Achievements";
            }
        }
    }
}