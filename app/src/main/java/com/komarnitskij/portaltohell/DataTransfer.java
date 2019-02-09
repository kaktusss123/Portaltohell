package com.komarnitskij.portaltohell;

import android.app.ActionBar;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

class DataTransfer {
    private static final DataTransfer ourInstance = new DataTransfer();

    WebInterface web;
    ArrayList<News> news;
    ActionBar actionBar;

    static DataTransfer getInstance() {
        return ourInstance;
    }

    private DataTransfer() {
    }
}
