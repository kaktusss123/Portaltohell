package com.komarnitskij.portaltohell;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleFragment extends Fragment {

    TextView selectedDate;
    Button prev_day, next_day;

    String[] months = {" января", " февраля", " марта", " апреля", " мая", " июня", " июля",
            " августа", " сентября", " октября", " ноября", " декабря"};

    Calendar now;
    WebInterface web;
    ArrayList<Pair> day;
    ScheduleAdapter adapter;
    ListView scheduleListView;

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
        scheduleListView = root.findViewById(R.id.scheduleListView);

        now = Calendar.getInstance();
        selectedDate.setText(String.format("%s%s", String.valueOf(now.get(Calendar.DAY_OF_MONTH)), months[now.get(Calendar.MONTH)]));

        prev_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now.add(Calendar.DAY_OF_YEAR, -1);
                selectedDate.setText(String.format("%s%s", String.valueOf(now.get(Calendar.DAY_OF_MONTH)), months[now.get(Calendar.MONTH)]));
                update_schedule();
            }
        });
        next_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                now.add(Calendar.DAY_OF_YEAR, 1);
                selectedDate.setText(String.format("%s%s", String.valueOf(now.get(Calendar.DAY_OF_MONTH)), months[now.get(Calendar.MONTH)]));
                update_schedule();
            }
        });

        // TODO if not in database
        DataTransfer transfer = DataTransfer.getInstance();
        web = transfer.web;
        update_schedule();

        return root;
    }

    void update_schedule() {
        web.get_schedule(new ScheduleCallback() {
            @Override
            public void ScheduleRequest(String response, Context context) {
                if (response != null) {
                    day = new ArrayList<>();
                    adapter = new ScheduleAdapter(context, R.layout.schedule_row_layout, day);
                    scheduleListView.setAdapter(adapter);
                    Document html = Jsoup.parse(response);
                    Elements disciplines = html.body().getElementsByClass("rowDisciplines");
                    // TODO Сделал получение таблиц, вывести таблицы на экран + бд
                    System.out.println(disciplines.toString());
                    for (int i = 0; i < disciplines.size(); i++) {
                        String name = disciplines.get(i).getElementsByAttributeValue("data-field", "discipline").text();
                        String startTime = disciplines.get(i).getElementsByAttributeValue("data-field", "datetime").get(0).getElementsByTag("div").get(0).text();
                        String endTime = disciplines.get(i).getElementsByAttributeValue("data-field", "datetime").get(0).getElementsByTag("div").get(1).text();
                        String type = disciplines.get(i).getElementsByAttributeValue("data-field", "datetime").get(0).getElementsByTag("div").get(2).text();
                        String classroom = "301";  //disciplines.get(i).getElementsByAttributeValue("data-field", "tutors").get(0).getElementsByTag("i").get(0).text();
                        String prepodName = "Буслаев";  //disciplines.get(i).getElementsByTag("button").get(0).text();
                        day.add(new Pair(name, startTime, endTime, type, classroom, prepodName));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }, getDate(now));
    }
}
