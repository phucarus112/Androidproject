package com.ygaps.travelapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ygaps.travelapp.Adapter.PageAdapter;
import com.ygaps.travelapp.R;


import androidx.appcompat.app.ActionBar;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

public class Detail_Review extends AppCompatActivity implements Detail.OnFragmentInteractionListener,Review.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_review2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        TabLayout tabLayout= (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Detail"));
        tabLayout.addTab(tabLayout.newTab().setText("Review"));
        final ViewPager viewPager=(ViewPager)findViewById(R.id.view_pager);
        final PageAdapter pageAdapter=new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);



        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                Log.e("Response", "kkk");
                switch(tab.getPosition()) {
                    case 0:

                        break;
                    case 1:

                        break;

                    default:

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}