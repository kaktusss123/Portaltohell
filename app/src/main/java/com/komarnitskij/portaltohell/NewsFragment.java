package com.komarnitskij.portaltohell;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class NewsFragment extends Fragment {

    ListView newsListView;
    WebInterface web;
    ArrayList<News> res;
    JSONArray arr;
    NewsAdapter adapter;


    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.news_fragment_layout, container, false);
        newsListView = root.findViewById(R.id.newsListView);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News clicked = (News) parent.getItemAtPosition(position);
                Intent openNewsInternetIntent = new Intent(Intent.ACTION_VIEW, clicked.url);
                startActivity(openNewsInternetIntent);
            }
        });

        final DataTransfer transfer = DataTransfer.getInstance();
        web = transfer.web;
        if (transfer.news == null) {
            web.getNews(new WebCallbacks() {
                @Override
                public void NewsRequest(String response, Context context) {
                    if (response != null) {
                        try {
                            res = new ArrayList<>();
                            arr = new JSONArray(response);
                            adapter = new NewsAdapter(context, R.layout.news_row_layout, res);
                            newsListView.setAdapter(adapter);
                            for (int i = 0; i < 10; i++) {
                                JSONObject news = arr.getJSONObject(i);
                                res.add(new News(news.getString("ImageUrl"),
                                        news.getString("Title"), news.getString("Description"),
                                        news.getString("Date"), news.getString("Url")));
                                adapter.notifyDataSetChanged();
                            }
                            transfer.news = res;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
        } else {
            newsListView.setAdapter(new NewsAdapter(this.getContext(), R.layout.news_row_layout, transfer.news));
        }
        return root;
    }
}
