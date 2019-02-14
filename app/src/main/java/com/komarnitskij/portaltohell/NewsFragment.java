package com.komarnitskij.portaltohell;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    WebInterface web;
    ArrayList<News> res;
    JSONArray arr;
    NewsRecViewAdapter adapter;
    RecyclerView newsRecView;
    LinearLayoutManager llm;


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
        newsRecView = root.findViewById(R.id.newsRV);

        llm = new LinearLayoutManager(root.getContext());
        newsRecView.setLayoutManager(llm);



        final DataTransfer transfer = DataTransfer.getInstance();
        web = transfer.web;
        if (transfer.news == null) {
            web.getNews(new NewsCallback() {
                @Override
                public void NewsRequest(String response, Context context) {
                    if (response != null) {
                        try {
                            res = new ArrayList<>();
                            arr = new JSONArray(response);
                            adapter = new NewsRecViewAdapter(res);
                            newsRecView.setAdapter(adapter);
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
            newsRecView.setAdapter(new NewsRecViewAdapter(transfer.news));
        }

        newsRecView.addOnItemTouchListener(new RecyclerItemClickListener(root.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(Intent.ACTION_VIEW, res.get(position).url));
            }
        }));

        return root;
    }
}
