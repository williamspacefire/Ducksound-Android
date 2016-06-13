package com.meycup.ducksound.media;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.meycup.ducksound.ImageBackground;
import com.meycup.ducksound.MainActivity;
import com.meycup.ducksound.R;

import java.io.IOException;

public class Player implements OnPreparedListener, OnCompletionListener{

    Toolbar player;
    String stream;
    private ImageButton action;
    private SeekBar seekBar;

    private MediaPlayer mediaPlayer;
    private boolean prepared = false, start;

    public Player(Toolbar player_toolbar, String music_url, String title, String cover, boolean start) throws IOException{
        player = player_toolbar;
        stream = music_url;
        this.start = start;

        action = (ImageButton) player.findViewById(R.id.action);
        action.setImageResource(R.drawable.ic_action_play);

        ImageView coverImg = (ImageView) player.findViewById(R.id.cover);
        ImageBackground imgb = new ImageBackground(coverImg, cover);
        imgb.execute();

        seekBar = (SeekBar) player.findViewById(R.id.progress);
        seekBar.setProgress(0);
        
        player.setTitle(title);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(stream+"?client_id="+MainActivity.CLIENT_ID);
        mediaPlayer.prepareAsync();

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prepared){
                    if(action.getTag().toString().equals("play")){
                        pause();
                    }else{
                        play();
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(prepared && fromUser){
                    try{
                        mediaPlayer.seekTo(progress);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

   }

    public static String getTimeByDuration(long duration){
        int aux = (int)(duration / 1000);
        int m = aux / 60;
        int s = aux % 60;

        return ((m < 10) ? "0"+m : ""+m)+":"+((s < 10) ? "0"+s : ""+s);
    }

    public void play(){

        action.setImageResource(R.drawable.ic_action_pause);
        action.setTag("play");

        try {
            mediaPlayer.start();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

        UpdateSeekBar updateSeekBar = new UpdateSeekBar(seekBar, mediaPlayer);
        updateSeekBar.execute();

    }

    public void pause(){

        action.setImageResource(R.drawable.ic_action_play);
        action.setTag("pause");

        try {
            mediaPlayer.pause();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    public void stop(){
        prepared = false;
        seekBar.setProgress(0);
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        prepared = true;

        seekBar.setMax(mp.getDuration());

        if(start){
            play();
        }else{
            pause();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        mp.seekTo(0);
        pause();
        seekBar.setProgress(0);
    }
}
