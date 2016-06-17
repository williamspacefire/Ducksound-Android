/*
 * Copyright (C) 2016 Meycup
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

package com.meycup.ducksound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meycup.ducksound.ImageBackground;
import com.meycup.ducksound.R;
import com.meycup.ducksound.media.Player;

public class ListAdapter extends ArrayAdapter {

    private String[][] list;

    public ListAdapter(Context context, String[][] list, String[] x) {
        super(context, R.layout.list_music, x);

        this.list = list;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        LayoutInflater li = LayoutInflater.from(getContext());
        View custom = li.inflate(R.layout.list_music, null);

        ImageView cover = (ImageView) custom.findViewById(R.id.cover);
        TextView title = (TextView) custom.findViewById(R.id.title);
        TextView duration = (TextView) custom.findViewById(R.id.duration);

        try{

            ImageBackground image = new ImageBackground(cover,  (!list[i + 1][4].equals("null")) ? list[i + 1][4] : list[i + 1][3]);
            image.execute();

            duration.setText(Player.getTimeByDuration(Long.valueOf(list[i + 1][1])));

            title.setText(list[0][i]);

        } catch (Exception e){
            e.printStackTrace();
        }

        return custom;
    }
}
