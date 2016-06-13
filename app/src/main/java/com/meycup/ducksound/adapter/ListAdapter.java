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
