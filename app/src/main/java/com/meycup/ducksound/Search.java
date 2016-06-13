package com.meycup.ducksound;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URLEncoder;

public class Search extends AppCompatActivity {

    Toolbar play_bar;
    ListView music_list;
    BackgroundSearch search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        play_bar = (Toolbar) findViewById(R.id.player_bar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            play_bar.setElevation(4);
        }

        music_list = (ListView) findViewById(R.id.listView);

        makeSearch(getIntent());
    }

    public void makeSearch(Intent intent){
        if(intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())){
            String q = intent.getStringExtra(SearchManager.QUERY);
            getSupportActionBar().setTitle(q);
            search = new BackgroundSearch(this, music_list, play_bar, (TextView) findViewById(R.id.notfound));
            search.execute(URLEncoder.encode(q));
        }
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
    protected void onNewIntent(Intent intent) {
        if(search.getPlayer() != null){
            search.getPlayer().stop();
        }

        setIntent(intent);
        makeSearch(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }else if(id == R.id.about){
            MainActivity.about(this);
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(search.getPlayer() != null){
            search.getPlayer().stop();
        }
    }
}
