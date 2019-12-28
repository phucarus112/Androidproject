package com.example.project.Adapter;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.project.Activity.Detail;
import com.example.project.Activity.Review;

public class PageAdapter extends FragmentPagerAdapter {

    int mbehavior;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm,behavior);
        this.mbehavior=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0:

                Detail d=new Detail();

                return d;
            case 1:
                Review r=new Review();
                return r;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.mbehavior;
    }
}