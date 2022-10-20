package com.example.e_commarce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class ChatActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        tabLayout = findViewById(R.id.chat_tab);
        viewPager = findViewById(R.id.chat_pager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        /*FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.chat_pager,new ChatFragment()).commit();*/

        viewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));

    }

    private class TabPagerAdapter extends FragmentPagerAdapter {
        public TabPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Chat";
                case 1:
                    return "Status";
                case 2:
                    return "Call";

            }
            //return super.getPageTitle(position);
            return null;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ChatFragment();
                case 1:
                    return new StatusFragment();
                case 2:
                    return new CallFragment();

            }
            //return null;
            return null;
        }


        @Override
        public int getCount() {
            return 3;
        }
    }
}