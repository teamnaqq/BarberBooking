package com.teamnaqq.androidbaberbooking.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.teamnaqq.androidbaberbooking.Fragments.BookingStep1Fragment;
import com.teamnaqq.androidbaberbooking.Fragments.BookingStep2Fragment;
import com.teamnaqq.androidbaberbooking.Fragments.BookingStep3Fragment;
import com.teamnaqq.androidbaberbooking.Fragments.BookingStep4Fragment;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return BookingStep1Fragment.getInstance();
            case 1:
                return BookingStep2Fragment.getInstance();
            case 2:
                return BookingStep3Fragment.getInstance();
            case 3:
                return BookingStep4Fragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
