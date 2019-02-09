package com.komarnitskij.portaltohell;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends ArrayAdapter {

    Context context;
    int resource;
    List<News> objects;

    public NewsAdapter( Context context, int resource, List<News> objects) {
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
        v = inflater.inflate(R.layout.news_row_layout, null);
        TextView title = v.findViewById(R.id.news_title_block);
        TextView description = v.findViewById(R.id.news_description_block);
        TextView date = v.findViewById(R.id.news_date_block);
        ImageView img = v.findViewById(R.id.news_image_block);
        title.setText(objects.get(position).name);
        description.setText(objects.get(position).description);
        date.setText(objects.get(position).date);
        Picasso.get().load(objects.get(position).imgUrl).into(img);
        return v;
    }
}
