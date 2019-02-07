package com.komarnitskij.portaltohell;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

public class ScheduleFragment extends Fragment {

    TextView selectedDate;
    Button prev_day, next_day;

    String[] months = {" января", " февраля", " марта", " апреля", " мая", " июня", " июля",
            " августа", " сентября", " октября", " ноября", " декабря"};

    Calendar now;
    WebInterface web;

    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private String getDate(Calendar now) {
        int month = now.get(Calendar.MONTH) + 1;
        String sMonth;
        if (month < 10) sMonth = "0" + month;
        else sMonth = String.valueOf(month);
        int day = now.get(Calendar.DAY_OF_MONTH);
        String sDay;
        if (day < 10) sDay = "0" + day;
        else sDay = String.valueOf(day);
        return sMonth + "%2F" + sDay + "%2F" + now.get(Calendar.YEAR);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.schedule_fragment_layout, container, false);

        selectedDate = root.findViewById(R.id.text_selected_date);
        prev_day = root.findViewById(R.id.prev_day);
        next_day = root.findViewById(R.id.next_day);

        now = Calendar.getInstance();
        selectedDate.setText(String.format("%s%s", String.valueOf(now.get(Calendar.DAY_OF_MONTH)), months[now.get(Calendar.MONTH)]));

        prev_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now.add(Calendar.DAY_OF_YEAR, -1);
                selectedDate.setText(String.format("%s%s", String.valueOf(now.get(Calendar.DAY_OF_MONTH)), months[now.get(Calendar.MONTH)]));
                web.schedule(getDate(now));
            }
        });
        next_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now.add(Calendar.DAY_OF_YEAR, 1);
                selectedDate.setText(String.format("%s%s", String.valueOf(now.get(Calendar.DAY_OF_MONTH)), months[now.get(Calendar.MONTH)]));
                web.schedule(getDate(now));
            }
        });

        // TODO if not in database
        DataTransfer transfer = DataTransfer.getInstance();
        web = transfer.web;
        web.schedule(getDate(now));

        return root;
    }
}
