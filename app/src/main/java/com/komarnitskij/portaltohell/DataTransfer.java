package com.komarnitskij.portaltohell;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

class DataTransfer {
    private static final DataTransfer ourInstance = new DataTransfer();

    WebInterface web;
    ArrayList<News> news;
    ArrayList<Pair> schedule;
    ArrayList<String> dates;

    static DataTransfer getInstance() {
        return ourInstance;
    }

    private DataTransfer() {
    }
}
