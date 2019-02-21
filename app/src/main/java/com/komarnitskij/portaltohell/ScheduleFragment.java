package com.komarnitskij.portaltohell;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleFragment extends Fragment {

    String[] months = {" января", " февраля", " марта", " апреля", " мая", " июня", " июля",
            " августа", " сентября", " октября", " ноября", " декабря"};

    Calendar now;
    WebInterface web;
    ArrayList<Pair> day;
    ScheduleRecAdapter adapter;
    RecyclerView scheduleRecView;
    LinearLayoutManager llm;
    GestureDetector gl;
    MaterialCalendarView calendarView;
    CalendarMode current;


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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.schedule_fragment_layout, container, false);
        now = Calendar.getInstance();

        gl = new GestureDetector(new GestureListener());
        View.OnTouchListener swiper = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gl.onTouchEvent(event);
                return true;
            }
        };
        root.setOnTouchListener(swiper);

        scheduleRecView = root.findViewById(R.id.scheduleRecView);
        calendarView = root.findViewById(R.id.calendarView);

        calendarView.setTopbarVisible(false);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                update_schedule(getDate(date.getCalendar()));
            }
        });

        llm = new LinearLayoutManager(root.getContext());
        scheduleRecView.setLayoutManager(llm);


        DataTransfer transfer = DataTransfer.getInstance();
        web = transfer.web;
        update_schedule(getDate(now));

        return root;
    }

    void update_schedule(String date) {
        day = new ArrayList<>();
        adapter = new ScheduleRecAdapter(day);
        scheduleRecView.setAdapter(adapter);
//        if (DataTransfer.getInstance().dates.contains(date)) {
//            for (Pair p : DataTransfer.getInstance().schedule)
//                if (p.date.equals(date))
//                    day.add(p);
//        } else
            web.get_schedule(new ScheduleCallback() {
                @Override
                public void ScheduleRequest(String response, Context context) {
                    if (response != null) {
                        Document html = Jsoup.parse(response);
                        Elements disciplines = html.body().getElementsByClass("rowDisciplines");
                        System.out.println(disciplines.toString());
                        for (int i = 0; i < disciplines.size(); i++) {
                            // Очень сложный парсинг, просто оставить как оно есть
                            String name = disciplines.get(i).getElementsByAttributeValue("data-field", "discipline").text();
                            String startTime = disciplines.get(i).getElementsByAttributeValue("data-field", "datetime").get(0).getElementsByTag("div").get(0).text();
                            String endTime = disciplines.get(i).getElementsByAttributeValue("data-field", "datetime").get(0).getElementsByTag("div").get(1).text();
                            String type = disciplines.get(i).getElementsByAttributeValue("data-field", "datetime").get(0).getElementsByTag("div").get(2).text();

                            // Собираю список из аудиторий
                            ArrayList<String> classrooms = new ArrayList<>();
                            try {
                                Elements classroomsElems = disciplines.get(i).getElementsByAttributeValue("data-field", "tutors").get(0).getElementsByTag("i");
                                for (Element el : classroomsElems) {
                                    // Обычно в первой части находится номер кабинета, хз что делать с фразами типа "Актовый зал"
                                    classrooms.add(el.text().split(" ")[0]);
                                }
                                // Если че, пропускаем
                            } catch (IndexOutOfBoundsException ignored) {
                            }

                            // Та же хрень, но для преподов
                            ArrayList<String> prepodName = new ArrayList<String>();
                            try {
                                Elements prepods = disciplines.get(i).getElementsByTag("button");
                                for (Element el : prepods) {
                                    prepodName.add(el.text());
                                }
                            } catch (IndexOutOfBoundsException ignored) {
                            }

                            // Ну и добавляю в адаптер
                            Pair today = new Pair(name, startTime, endTime, type, classrooms, prepodName, getDate(now));
                            day.add(today);
                            DataTransfer.getInstance().schedule.add(today);
                            DataTransfer.getInstance().dates.add(getDate(now));
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }, getDate(now));
        adapter.notifyDataSetChanged();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // справа налево
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // слева направо
            }

            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // снизу вверх
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // сверху вниз
            }
            return false;
        }
    }
}
