package com.maksimik.weather.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.maksimik.weather.fragments.PageFragment;
import com.maksimik.weather.model.DayWeather;
import com.maksimik.weather.model.WeatherHour;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> mFragmentTitleList = new ArrayList<>();

    private ArrayList<DayWeather> dayWeathers;

    private final SparseArray<Fragment> mFragmentList = new SparseArray<>();

    public ViewPagerAdapter(FragmentManager manager, ArrayList<DayWeather> dayWeathers) {
        super(manager);

        SimpleDateFormat format1 = new SimpleDateFormat("EEE MMM dd");
        SimpleDateFormat format2 = new SimpleDateFormat("EEE dd");
        //TODO make cycle
        mFragmentTitleList.add("Сегодня \n" + format1.format(new Date()));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        mFragmentTitleList.add(format2.format(new Date(c.getTimeInMillis())));
        c.add(Calendar.DAY_OF_MONTH, 1);
        mFragmentTitleList.add(format2.format(new Date(c.getTimeInMillis())));
        c.add(Calendar.DAY_OF_MONTH, 1);
        mFragmentTitleList.add(format2.format(new Date(c.getTimeInMillis())));
        c.add(Calendar.DAY_OF_MONTH, 1);
        mFragmentTitleList.add(format2.format(new Date(c.getTimeInMillis())));

        this.dayWeathers = dayWeathers;
    }

    public void setDayWeathers(ArrayList<DayWeather> dayWeathers) {

        this.dayWeathers = dayWeathers;

        mFragmentList.clear();

    }

    @Override
    public Fragment getItem(int position) {

        if (mFragmentList.get(position) == null) {

            if (dayWeathers == null || dayWeathers.size() <= position) {

                mFragmentList.put(position, PageFragment.newInstance(null));

            } else {

                mFragmentList.put(position, PageFragment.newInstance(dayWeathers.get(position)));
            }
        }
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        // return mFragmentList.size();
        return mFragmentTitleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);

    }
}
