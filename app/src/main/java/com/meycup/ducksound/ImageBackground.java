/*
 * Copyright (C) 2016 Spacefire
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
