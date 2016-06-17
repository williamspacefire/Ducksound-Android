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
