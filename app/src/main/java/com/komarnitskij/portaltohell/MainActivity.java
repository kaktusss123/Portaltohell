package com.komarnitskij.portaltohell;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    WebInterface web;
    BottomNavigationView navigation;
    FragmentTransaction transaction;
    SharedPreferences mSettings;
    DataTransfer transfer;
    boolean successfulAuth;
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

        // Инициализация нав бара
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Выставляю фрагмент по умолчанию
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, NewsFragment.newInstance());
        transaction.commit();

        // Обработка сохраненных данных авторизации
        mSettings = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mSettings.edit();
//        editor.putString("login", "185464");
//        editor.putString("pwd", "e7afb0d5d15adb49f214bf8698b466a5");
//        editor.apply();

        // Инициализация веб-интерфейса и трансфера данных

        DataTransfer.getInstance().web = new WebInterface(this);
        DataTransfer.getInstance().schedule = new ArrayList<>();
        DataTransfer.getInstance().dates = new ArrayList<>();
//        DataTransfer.getInstance().web.auth(mSettings.getString("login", ""), mSettings.getString("pwd", ""));


        // TODO сделать экран логина и авторизацию
        startActivity(new Intent(MainActivity.this, LoginActivity.class));

    }
}