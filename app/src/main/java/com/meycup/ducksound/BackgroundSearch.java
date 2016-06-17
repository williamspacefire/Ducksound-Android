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

package com.meycup.ducksound;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.meycup.ducksound.adapter.ListAdapter;
import com.meycup.ducksound.media.Player;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

class BackgroundSearch extends AsyncTask<String, Void, String[][]> {

    private ListView listView;
    private Toolbar play_bar;
    private TextView tv;
    private Context context;
    private ProgressDialog progress;
    private Player player;
    private String search;

    BackgroundSearch(Context context, ListView listView, Toolbar play_bar, TextView tv){
        this.context = context;
        this.listView = listView;
        this.play_bar = play_bar;
        this.tv = tv;

        progress = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        progress.setMessage("Carregando músicas...");
        progress.show();
    }

    @Override
    protected String[][] doInBackground(String... params) {

        this.search = params[0];

        String[][] array;

        try {

            Internet conn = new Internet(
                    "https://api.soundcloud.com/tracks?client_id=" + MainActivity.CLIENT_ID +
                            "&q="+params[0] +
                            "&limit=100"
            );
            conn.connect();

            String json = conn.getContent();

            JSONArray Jarray = new JSONArray(json);

            if(Jarray.length() == 0){
                return new String[][]{
                        {
                                "error_on_search",
                                ""
                        }
                };
            }

            array = new String[Jarray.length() + 1][(Jarray.length() >= 7) ? Jarray.length() : 7];

            for(int i = 0;i < Jarray.length();i++){
                array[0][i] = Jarray.getJSONObject(i).getString("title");

                array[i + 1][0] = Jarray.getJSONObject(i).getString("downloadable");
                array[i + 1][1] = Jarray.getJSONObject(i).getString("duration");
                array[i + 1][2] = Jarray.getJSONObject(i).getString("stream_url");
                array[i + 1][3] = Jarray.getJSONObject(i).getJSONObject("user").getString("avatar_url");
                array[i + 1][4] = Jarray.getJSONObject(i).getString("artwork_url");

                try{

                    array[i + 1][5] = Jarray.getJSONObject(i).getString("download_url");

                }catch (JSONException e){

                    e.printStackTrace();

                    Jarray.getJSONObject(i).put("download_url", "null");
                    array[i + 1][5] = Jarray.getJSONObject(i).getString("download_url");

                }

                array[i + 1][6] = Jarray.getJSONObject(i).getString("streamable");

            }

            return array;

        } catch (MalformedURLException e) {
            e.printStackTrace();

            return new String[][]{
                    {
                            "error_on_search",
                            "MalformedURLException - Ocorreu um erro ao fazer sua pesquisa. Tente novamente"
                    }
            };

        } catch (IOException e) {
            e.printStackTrace();

            return new String[][]{
                    {
                            "error_on_search",
                            "IOException - Ocorreu um erro ao fazer sua pesquisa. Verifiqe se você tem internet " +
                                    "ou se o "+context.getResources().getString(R.string.app_name)+" tem permissão para acessar a internet e tente novamente"
                    }
            };

        } catch (JSONException e) {
            e.printStackTrace();

            return new String[][]{
                    {
                            "error_on_search",
                            ""
                    }
            };

        }
    }

    @Override
    protected void onPostExecute(final String[][] strings) {
        if(strings[0][0].equals("error_on_search")){

            if(strings[0][1].equals("")){

                tv.setVisibility(View.VISIBLE);
                listView.setAdapter(null);

            }else{

                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle("Ocorreu um erro!");
                alert.setMessage(strings[0][1]);

                alert.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BackgroundSearch research = new BackgroundSearch(context, listView, play_bar, tv);
                        research.execute(search);
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.exit(0);
                    }
                });

                alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();
            }

        }else{

            if(tv != null){
                tv.setVisibility(View.INVISIBLE);
            }

            int count = 0;
            for(int i = 0;i < strings[0].length;i++){
                if(strings[0][i] != null){
                    count++;
                }
            }

            ListAdapter adp = new ListAdapter(context, strings, new String[count]);
            listView.setAdapter(adp);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(strings[position + 1][6].equals("true")){
                        try {
                            if (player != null) {
                                player.stop();
                                player = new Player(
                                        play_bar,
                                        strings[position + 1][2],
                                        strings[0][position],
                                        (!strings[position + 1][4].equals("null")) ? strings[position + 1][4] : strings[position + 1][3],
                                        true
                                );
                            } else {
                                player = new Player(
                                        play_bar,
                                        strings[position + 1][2], strings[0][position],
                                        (!strings[position + 1][4].equals("null")) ? strings[position + 1][4] : strings[position + 1][3],
                                        true
                                );
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("Desculpe, não é possível reproduzir essa música, escolha outra.");
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        
                        alert.show();
                    }

                }
            });
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(strings[position + 1][6].equals("true")) {
                    File dir = new File(Environment.getExternalStorageDirectory() + "/Ducksound");

                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    DownloadManager DM = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);

                    String download_url;

                    if (strings[position + 1][0].equals("true")) {
                        download_url = strings[position + 1][5] + "?client_id=" + MainActivity.CLIENT_ID;
                    } else {
                        download_url =
                                "http://188.138.17.231/~krafta/dow.php?url=" + strings[position + 1][2] +
                                        "?client_id=" + MainActivity.CLIENT_ID +
                                        "&name=" + URLEncoder.encode(strings[0][position] + "(Ducksound)");
                    }

                    Uri uri = Uri.parse(download_url.replace("https://","http://"));

                    DownloadManager.Request request = new DownloadManager.Request(uri);

                    request.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE
                    )
                            .setAllowedOverRoaming(false)
                            .setTitle(strings[0][position])
                            .setDestinationInExternalPublicDir("/Ducksound",
                                    strings[0][position]
                                            .replace(" ", "_")
                                            .replace(",", "_")
                                            .replace("ç", "c")
                                            .replace("'", "_") + ".mp3"
                            );

                    DM.enqueue(request);

                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("Desculpe, não é possível baixar essa música, escolha outra.");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                }

                return true;
            }
        });

        progress.hide();
    }

    public Player getPlayer(){
        return player;
    }
}
