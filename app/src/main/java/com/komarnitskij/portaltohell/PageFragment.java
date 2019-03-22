package com.komarnitskij.portaltohell;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    int pageNumber;
    int current_year;
    boolean current_year_leap;

    Calendar now;
    WebInterface web;
    ArrayList<Pair> day;
    ScheduleRecAdapter adapter;
    RecyclerView scheduleRecView;
    LinearLayoutManager llm;

    static PageFragment newInstance(int page, int current_year, boolean current_year_leap) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        arguments.putInt("year", current_year);
        arguments.putBoolean("year_l", current_year_leap);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        current_year = getArguments().getInt("year");
        current_year_leap = getArguments().getBoolean("year_l");
        System.out.println("Create " + pageNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pager_fragment, null);
        now = Calendar.getInstance();
        scheduleRecView = root.findViewById(R.id.scheduleRecView);
        llm = new LinearLayoutManager(root.getContext());
        scheduleRecView.setLayoutManager(llm);
        DataTransfer transfer = DataTransfer.getInstance();
        web = transfer.web;

        Calendar pageDate = getDateFromDayOfTheYear(pageNumber);
        TextView tvPage = root.findViewById(R.id.tvPage);
        tvPage.setText("Page " + pageNumber + " Date " + pageDate.get(Calendar.DAY_OF_MONTH) + current_year);

        update_schedule(getDate(pageDate));
        return root;
    }

    private Calendar getDateFromDayOfTheYear(int day) {
        int[] months = {31, current_year_leap ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (day > 365) {
            day = 365;
        }
        int month = 0;
        while (day > months[month]) {
            day -= months[month];
            month += 1;
        }
        Calendar date = new GregorianCalendar(current_year, month, day);
        return date;
    }

    public static boolean isLeapYear(int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
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
                    //System.out.println(disciplines.toString());
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
        }, date);
        adapter.notifyDataSetChanged();
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
}