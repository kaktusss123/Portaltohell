package com.komarnitskij.portaltohell;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText login;
    EditText password;
    CheckBox remember;
    Button submit;
    boolean success;
    Context context;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acrivity_login);

        context = this;

        login = findViewById(R.id.editText_login);
        password = findViewById(R.id.editText_password);
        remember = findViewById(R.id.checkbox_remember);
        submit = findViewById(R.id.button_login);
        submit.setOnClickListener(this);
        mSettings = getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        if (mSettings.contains("login") && mSettings.contains("pwd")) {
            login.setText(mSettings.getString("login", ""));
            password.setText(mSettings.getString("pwd", ""));
            remember.setChecked(true);
            DataTransfer.getInstance().web.auth(new AuthCallback() {
                @Override
                public void AuthRequest(String response, Context context) {
                    if (response == null) {
                        Toast.makeText(context, "Ошибка соединения", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Document html = Jsoup.parse(response);
                    Element loginForm = html.body().getElementById("loginForm");
                    if (loginForm == null) {
                        success = true;
                        Toast.makeText(context, "Успешный вход", Toast.LENGTH_SHORT).show();
                        if (remember.isChecked()) {
                            mSettings.edit().putString("login", login.getText().toString()).putString("pwd", password.getText().toString()).apply();
                        }
                        finish();
                    }
                }
            }, mSettings.getString("login", ""), mSettings.getString("pwd", ""));
        }
    }

    @Override
    public void onClick(View view) {
        for (int i = 1; i < 4; i++) {
            if (success) break;
            DataTransfer.getInstance().web.auth(new AuthCallback() {
                @Override
                public void AuthRequest(String response, Context context) {
                    if (response == null) {
                        Toast.makeText(context, "Ошибка соединения", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Document html = Jsoup.parse(response);
                    Element loginForm = html.body().getElementById("loginForm");
                    if (loginForm == null) {
                        success = true;
                        Toast.makeText(context, "Успешный вход", Toast.LENGTH_SHORT).show();
                        if (remember.isChecked()) {
                            mSettings.edit().putString("login", login.getText().toString()).putString("pwd", password.getText().toString()).apply();
                        }
                        finish();
                    }
                }
            }, login.getText().toString(), password.getText().toString());
        }
        if (!success) {
            Toast.makeText(context, "Неверный логин/пароль", Toast.LENGTH_SHORT).show();
        }
    }
}

