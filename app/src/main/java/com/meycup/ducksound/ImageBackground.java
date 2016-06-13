package com.meycup.ducksound;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;

public class ImageBackground extends AsyncTask<Void, Void, Bitmap> {

    private ImageView imageView;
    private String url;

    public ImageBackground(ImageView image, String imageUrl){
        imageView = image;
        url = imageUrl;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {

        try {

            Internet conn = new Internet(url);
            conn.connect();

            return conn.getBitmap();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }else{
            Log.i("Ducksound", "Failed on load image: "+url);
        }
    }
}
