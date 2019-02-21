package com.komarnitskij.portaltohell;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecViewAdapter extends RecyclerView.Adapter<NewsRecViewAdapter.NewsViewHolder> {

    List<News> objects;

    NewsRecViewAdapter(List<News> objects) {
        this.objects = objects;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView title, date, description;
        ImageView img;

        NewsViewHolder(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.news_title_block);
            date = v.findViewById(R.id.news_date_block);
            description = v.findViewById(R.id.news_description_block);
            img = v.findViewById(R.id.news_image_block);
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_cardview_row_layout, viewGroup, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder v, int i) {
        v.title.setText(objects.get(i).name);
        v.description.setText(objects.get(i).description);
        v.date.setText(objects.get(i).date);
        Picasso.get().load(objects.get(i).imgUrl).into(v.img);
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
