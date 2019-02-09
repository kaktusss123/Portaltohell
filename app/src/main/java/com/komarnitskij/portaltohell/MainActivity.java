package com.komarnitskij.portaltohell;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public WebInterface web;
    BottomNavigationView navigation;
    FragmentTransaction transaction;
    SharedPreferences mSettings;
    DataTransfer transfer;

    Context context;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_news:
                    selectedFragment = NewsFragment.newInstance();
                    break;
                case R.id.navigation_group:
                    selectedFragment = GroupFragment.newInstance();
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = ScheduleFragment.newInstance();
                    break;
                case R.id.navigation_search:
                    selectedFragment = MarksFragment.newInstance();
                    break;
                case R.id.navigation_like:
                    selectedFragment = RestFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            assert selectedFragment != null;
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, NewsFragment.newInstance());
        transaction.commit();

        mSettings = getSharedPreferences("LoginData", Context.MODE_PRIVATE);

        web = new WebInterface(this);
        transfer = DataTransfer.getInstance();
        web.auth(mSettings.getString("login", ""), mSettings.getString("pwd", ""));
        transfer.web = web;
    }
}