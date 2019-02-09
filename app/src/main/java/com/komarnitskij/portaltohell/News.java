package com.komarnitskij.portaltohell;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

class News {
    ImageView img;
    String imgUrl;
    String date;
    String name;
    String description;

    News(String imgUrl, String name, String description, String date) {
        this.date = date;
        this.description = description;
        this.imgUrl = "http://www.fa.ru" + imgUrl;
        this.name = name;
    }

    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        ImageView img;

        ImageDownloader(ImageView img) {
            this.img = img;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Ошибка передачи", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap img) {
            this.img.setImageBitmap(img);
        }
    }
}
