package com.komarnitskij.portaltohell;

import android.net.Uri;

import java.net.URI;

class News {
    String imgUrl;
    String date;
    String name;
    String description;
    Uri url;

    News(String imgUrl, String name, String description, String date, String url) {
        this.date = date;
        this.description = description;
        this.imgUrl = "http://www.fa.ru" + imgUrl;
        this.name = name;
        this.url = Uri.parse("http://www.fa.ru" + url);
    }

}
