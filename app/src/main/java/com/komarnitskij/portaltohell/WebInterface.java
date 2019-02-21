package com.komarnitskij.portaltohell;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;

class WebInterface {

    private Context context;
    private RequestQueue queue;

    WebInterface(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
    }

    void auth(final AuthCallback callback, String login, String password) {
        String url = "https://portal.fa.ru/CoreAccount/LogOn?Login=" + login + "&Pwd=" + password;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.AuthRequest(response, context);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AuthError", error.toString());
            }
        });
        queue.add(stringRequest);
    }

    void get_schedule(final ScheduleCallback callback, String date) {
        String url = "https://portal.fa.ru/Job/SearchAjax?DateBegin=" + date + "&DateEnd=" + date + "&JobType=INDIVIDUAL&GroupId=21688";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.ScheduleRequest(response, context);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Не удалось получить расписание", Toast.LENGTH_LONG).show();
                System.out.println(error);
            }
        });
        queue.add(stringRequest);
    }

    void getNews(final NewsCallback callback) {
        String url = "http://www.fa.ru/_layouts/15/RNS.University/AJAX.ashx?action=mainnews&page=1&count=10";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.NewsRequest(response, context);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Не удалось получить новости", Toast.LENGTH_LONG).show();
                System.out.println(error);
            }
        });
        queue.add(stringRequest);
    }


}
