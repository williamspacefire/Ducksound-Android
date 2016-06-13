package com.meycup.ducksound;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /**
     * Your Soundcloud CLIENT_ID here 
     */
    final static public String CLIENT_ID = "YOUR CLIENT ID HERE";

    Toolbar play_bar;
    ListView music_list;
    TextView notfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_bar = (Toolbar) findViewById(R.id.player_bar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            play_bar.setElevation(4);
        }

        music_list = (ListView) findViewById(R.id.music_list);
        notfound = (TextView) findViewById(R.id.notfound);

        BackgroundSearch search = new BackgroundSearch(this, music_list, play_bar, notfound);
        search.execute("");
    }

    public static void about(Context context){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Sobre o Ducksound");
        alert.setMessage(
                "Ducksound é um buscador de músicas grátis, criado por um geek que ama " +
                        "músicas e detesta ir em sites não confiáveis para baixar." +
                        " O Ducksound foi feito para ser símples e fácil de usar, basta " +
                        "pesquisar sua música e dar um click longo nela para baixar." +
                        " O código fonte deste aplicativo está no Github e você pode contribuir " +
                        "com o código ou criar sua própria versão do Ducksound." +
                        " \n\nGithub: github.com/williamspacefire/Ducksound-Android"
        );

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView;

        MenuItem menuItem = menu.findItem(R.id.search);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            searchView = (SearchView) menuItem.getActionView();
        }else{
            searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        }

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Pesquise por Artista, Banda ou Música");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.about){

            about(this);
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.exit(0);
    }
}
