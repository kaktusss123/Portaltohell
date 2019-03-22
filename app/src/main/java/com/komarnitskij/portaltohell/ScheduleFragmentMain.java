package com.komarnitskij.portaltohell;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.prolificinteractive.materialcalendarview.CalendarDay;
//import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

public class ScheduleFragmentMain extends Fragment {

    ViewPager pager;
    PagerAdapter pagerAdapter;
    MaterialCalendarView calendarView;
    int current_year;
    boolean current_year_leap;

    public static ScheduleFragmentMain newInstance() {
        return new ScheduleFragmentMain();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.schedule_fragment_main_layout, container, false);

        pager = root.findViewById(R.id.pager);
        pagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager());
        pager.setAdapter(pagerAdapter);

        Calendar calendar = new GregorianCalendar();
        current_year = calendar.get(calendar.YEAR);
        pager.setCurrentItem(calendar.get(calendar.DAY_OF_YEAR));

        pager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    current_year--;
                    current_year_leap = ScheduleFragment.isLeapYear(current_year);
                    pager.setCurrentItem(current_year_leap ? 366 : 365);
                }
                if (position == 366 + (current_year_leap ? 1 : 0)) {
                    current_year++;
                    current_year_leap = ScheduleFragment.isLeapYear(current_year);
                    pager.setCurrentItem(1);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        calendarView = root.findViewById(R.id.calendarView);

        calendarView.setTopbarVisible(false);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                pager.setCurrentItem(date.getCalendar().get(Calendar.DAY_OF_YEAR));
            }
        });
        return root;
    }

    private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScheduleFragment.newInstance(position, current_year, current_year_leap);
        }

        @Override
        public int getCount() {
            return 368;
        }

    }

}