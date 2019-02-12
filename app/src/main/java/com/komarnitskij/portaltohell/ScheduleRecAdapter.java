package com.komarnitskij.portaltohell;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ScheduleRecAdapter extends RecyclerView.Adapter<ScheduleRecAdapter.PairViewHolder> {

    List<Pair> objects;

    ScheduleRecAdapter(List<Pair> objects) {
        this.objects = objects;
    }

    class PairViewHolder extends RecyclerView.ViewHolder {

        TextView startTime, endTime, name, type, prepod, classroom;
        CardView cardView;

        PairViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.scheduleCardView);
            startTime = v.findViewById(R.id.schedulePairStart);
            endTime = v.findViewById(R.id.schedulePairEnd);
            name = v.findViewById(R.id.schedulePairName);
            type = v.findViewById(R.id.schedulePairType);
            prepod = v.findViewById(R.id.schedulePrepodName);
            classroom = v.findViewById(R.id.schedulePairClass);
        }
    }

    @NonNull
    @Override
    public PairViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_cardview_layout, viewGroup, false);
        return new PairViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PairViewHolder v, int position) {
        v.startTime.setText(objects.get(position).startTime);
        v.endTime.setText(objects.get(position).endTime);
        v.name.setText(objects.get(position).name);
        v.type.setText(objects.get(position).type);
        v.prepod.setText(objects.get(position).prepodName);
        v.classroom.setText(objects.get(position).classroom);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView rv) {
        super.onAttachedToRecyclerView(rv);
    }
}
