package com.komarnitskij.portaltohell;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ScheduleAdapter extends ArrayAdapter<Pair> {
    public ScheduleAdapter(Context context, int resource) {
        super(context, resource);
    }

    Context context;
    int resource;
    List<Pair> objects;

    public ScheduleAdapter( Context context, int resource, List<Pair> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.schedule_row_layout, null);
        TextView startTime = v.findViewById(R.id.schedulePairStart);
        TextView endTime = v.findViewById(R.id.schedulePairEnd);
        TextView name = v.findViewById(R.id.schedulePairName);
        TextView type = v.findViewById(R.id.schedulePairType);
        TextView prepod = v.findViewById(R.id.schedulePrepodName);
        TextView classroom = v.findViewById(R.id.schedulePairClass);
        startTime.setText(objects.get(position).startTime);
        endTime.setText(objects.get(position).endTime);
        name.setText(objects.get(position).name);
        type.setText(objects.get(position).type);
        prepod.setText(objects.get(position).prepodName);
        classroom.setText(objects.get(position).classroom);
        return v;
    }
}

class Pair {
    String name, startTime, endTime, type, classroom, prepodName;

    Pair(String name, String startTime, String endTime, String type, String classroom, String prepodName) {
        this.classroom = classroom;
        this.endTime = endTime;
        this.name = name;
        this.prepodName = prepodName;
        this.startTime = startTime;
        this.type = type;
    }

    Pair(String name) {
        this.name = name;
        this.classroom = "";
        this.endTime = "";
        this.prepodName = "";
        this.startTime = "";
        this.type = "";
    }
}
