package com.maksimik.weather.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.maksimik.weather.fragments.PageFragment;
import com.maksimik.weather.model.DayWeather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> mFragmentTitleList = new ArrayList<>();

    private List<DayWeather> dayWeathers;

    private final SparseArray<Fragment> mFragmentList = new SparseArray<>();

    public ViewPagerAdapter(final FragmentManager manager, final List<DayWeather> dayWeathers) {
        super(manager);

        final SimpleDateFormat format1 = new SimpleDateFormat("EEE MMM dd", Locale.getDefault());
        final SimpleDateFormat format2 = new SimpleDateFormat("EEE dd", Locale.getDefault());

        mFragmentTitleList.add("Сегодня \n" + format1.format(new Date()));

        final Calendar c = Calendar.getInstance();

        for (int i = 0; i < 4; i++) {
            c.add(Calendar.DAY_OF_MONTH, 1);
            mFragmentTitleList.add(format2.format(new Date(c.getTimeInMillis())));
        }

        this.dayWeathers = dayWeathers;
    }

    public void setDayWeathers(final List<DayWeather> dayWeathers) {

        this.dayWeathers = dayWeathers;

        mFragmentList.clear();

    }

    @Override
    public Fragment getItem(final int position) {

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

        return mFragmentTitleList.size();

    }

    @Override
    public CharSequence getPageTitle(final int position) {

        return mFragmentTitleList.get(position);

    }
}
