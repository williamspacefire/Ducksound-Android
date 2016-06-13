package com.meycup.ducksound.media;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.SeekBar;

public class UpdateSeekBar extends AsyncTask<Void, Void, Void> {

    private SeekBar bar;
    private MediaPlayer mediaPlayer;

    UpdateSeekBar(SeekBar bar, MediaPlayer mediaPlayer){
        this.bar = bar;
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    protected Void doInBackground(Void... params) {

        while(true){
            try {
                mediaPlayer.getCurrentPosition();
                publishProgress();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch (IllegalStateException e){
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        try{
            bar.setProgress(mediaPlayer.getCurrentPosition());
        }catch (IllegalStateException e){
            e.printStackTrace();
        }
    }
}
