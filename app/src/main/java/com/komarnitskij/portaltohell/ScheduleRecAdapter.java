package com.komarnitskij.portaltohell;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ScheduleRecAdapter extends RecyclerView.Adapter<ScheduleRecAdapter.PairViewHolder> {

    private List<Pair> objects;

    ScheduleRecAdapter(List<Pair> objects) {
        this.objects = objects;
    }

    class PairViewHolder extends RecyclerView.ViewHolder {

        TextView startTime, endTime, name, type, prepod, classroom;
        LinearLayout ll;

        PairViewHolder(View v) {
            super(v);
            startTime = v.findViewById(R.id.schedulePairStart);
            endTime = v.findViewById(R.id.schedulePairEnd);
            name = v.findViewById(R.id.schedulePairName);
            type = v.findViewById(R.id.schedulePairType);
            prepod = v.findViewById(R.id.schedulePrepodName);
            classroom = v.findViewById(R.id.schedulePairClass);
            ll = v.findViewById(R.id.scheduleMainLinearLayout);
        }
    }

    private int pxFromDp(float dp, Context context) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    @NonNull
    @Override
    public PairViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_cardview_layout, viewGroup, false);
        return new PairViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PairViewHolder v, int position) {
        // Инициализация текстовых объектов
        v.startTime.setText(objects.get(position).startTime);
        v.endTime.setText(objects.get(position).endTime);
        v.name.setText(objects.get(position).name);
        v.type.setText(objects.get(position).type);
        // Самое интересное - перебор аудиторий и преподов
        for (int i = 0; i < Math.max(objects.get(position).classrooms.size(), objects.get(position).prepodName.size()); i++) {
            LinearLayout row = new LinearLayout(v.itemView.getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            Context c = v.itemView.getContext();

            // Выставляю margin
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(pxFromDp(0, c), pxFromDp(0, c), pxFromDp(8, c), pxFromDp(4, c));

            TextView classroom = new TextView(v.itemView.getContext());
            try {
                classroom.setText(objects.get(position).classrooms.get(i));
                classroom.setBackgroundResource(R.drawable.schedule_class_background);
                classroom.setTextColor(Color.WHITE);
            } catch (IndexOutOfBoundsException ex) {
                classroom.setVisibility(View.INVISIBLE);
            }

            TextView prepod = new TextView(v.itemView.getContext());
            try {
                prepod.setText(objects.get(position).prepodName.get(i));
                prepod.setBackgroundResource(R.drawable.schedule_prepod_background);
            } catch (IndexOutOfBoundsException ex) {
                prepod.setVisibility(View.INVISIBLE);
            }

            // Сначала добавляю LinearLayout горизонтальный, а в него помещаю свои TextView с margin
            row.addView(classroom, params);
            row.addView(prepod, params);
            v.ll.addView(row);

        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView rv) {
        super.onAttachedToRecyclerView(rv);
    }
}
