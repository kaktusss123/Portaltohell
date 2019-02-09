package com.komarnitskij.portaltohell;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

class WebInterface {

    private Context context;
    private RequestQueue queue;
    private String groupId;

    WebInterface(Context context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
        CookieManager manager = new CookieManager();
        CookieHandler.setDefault(manager);
    }

    void auth(String login, String password) {
        String url = "https://portal.fa.ru/CoreAccount/LogOn?Login=" + login + "&Pwd=" + password;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Document html = Jsoup.parse(response);
                    Element loginForm = html.body().getElementById("loginForm");
                    if (loginForm != null) {
                        // TODO Ошибка неверный лог/пар
                        Toast.makeText(context, "Неверный логин/пароль", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Успешный вход", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
        url = "https://portal.fa.ru/GosvpoGroup/Education?MenuId=GosvpoGroupEducation";
        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    int pos = response.lastIndexOf("21688");
                    System.out.println("**************************************" + String.valueOf(pos) + "**************************************");
//                    Document html = Jsoup.parse(response);
//                    groupId = html.body().getElementsByAttribute("selected").get(0).attr("value");
//                    Toast.makeText(context, groupId, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Что-то не так с группой", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    void get_schedule(String date) {
        String url = "https://portal.fa.ru/Job/SearchAjax?DateBegin=" + date + "&DateEnd=" + date + "&JobType=INDIVIDUAL&GroupId=21688";
        final ArrayList<ArrayList<String>> scheduleOnDay = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
//                    System.out.println(response);
                    Document html = Jsoup.parse(response);
                    Elements disciplines = html.body().getElementsByClass("rowDisciplines");
                    System.out.println();
                    // TODO Сделал получение таблиц, вывести таблицы на экран + бд
                    System.out.println(disciplines.toString());
                    for (int i = 0; i < disciplines.size(); i++) {
                        scheduleOnDay.add(new ArrayList<String>());
                        String name = disciplines.get(i).getElementsByAttributeValue("data-field", "discipline").text();
                        System.out.println(name);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Опачки", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    void getNews(final WebCallbacks callback) {
        String url = "http://www.fa.ru/_layouts/15/RNS.University/AJAX.ashx?action=archivednews&page=1&count=30";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.NewsRequest(response, context);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Не удалось получить новости", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }


}
