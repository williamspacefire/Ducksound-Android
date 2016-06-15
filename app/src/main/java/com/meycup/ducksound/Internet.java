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
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Internet {

    private URL url;
    private InputStream is;

    public Internet(String url) throws MalformedURLException{
        this.url = new URL(url);
    }

    public Internet(){}

    protected void getInputStream() throws IOException{
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        is = conn.getInputStream();
    }

    public void connect() throws IOException{
        getInputStream();
    }

    public void connect(String url) throws IOException {

        this.url = new URL(url);

        getInputStream();
    }

    public String getContent() throws IOException{
        String content = "", line = "";

        BufferedReader data = new BufferedReader(new InputStreamReader(is));

        while((line = data.readLine()) != null){
            content += line;
        }

        return content;
    }

    public Bitmap getBitmap(){
        return BitmapFactory.decodeStream(is);
    }

}
