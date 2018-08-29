package com.alex.dailynews.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alex.dailynews.R;
import com.alex.dailynews.adapters.NewsAdapter;
import com.alex.dailynews.model.NewsArticle;
import com.alex.dailynews.model.NewsReply;
import com.alex.dailynews.network.APIClient;
import com.alex.dailynews.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchNews extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();

    private ArrayList<NewsArticle> searchList;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private ActionBar actionBar;
    private LinearLayout noMessagesLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.search_recycler_view);
        noMessagesLayout = findViewById(R.id.no_messages_error_layout);
        progressBar = findViewById(R.id.search_progress_bar);
        searchList = new ArrayList<>();
        newsAdapter = new NewsAdapter(SearchNews.this, 1, searchList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ScaleInAnimationAdapter(newsAdapter));
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String query = "";
        String WidgetQuery = getIntent().getStringExtra(MainActivity.widgetSearchString);
        if (!Objects.equals(WidgetQuery, "")) {
            query = WidgetQuery;
            actionBar.setTitle(R.string.search_everything);
        }
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            WidgetQuery = "";
            query = intent.getStringExtra(SearchManager.QUERY);
            actionBar.setTitle(query);
        }
        if (Intent.ACTION_SEARCH.equals(intent.getAction()) || (!Objects.equals(WidgetQuery, ""))) {
            progressBar.setVisibility(View.VISIBLE);
            APIClient apiClient = RetrofitInstance.getRetrofitInstance().create(APIClient.class);
            Call<NewsReply> call = apiClient.searchAnything(query);

            call.enqueue(new Callback<NewsReply>() {
                @Override
                public void onResponse(Call<NewsReply> call, Response<NewsReply> response) {
                    searchList = new ArrayList<>();
                    NewsReply newsReply = response.body();
                    searchList.addAll(Objects.requireNonNull(newsReply).getNewsArticleList());
                    Log.d(TAG, searchList.toString());
                    if (searchList == null || searchList.size() == 0) {
                        progressBar.setVisibility(View.GONE);
                        noMessagesLayout.setVisibility(View.VISIBLE);
                    } else {
                        noMessagesLayout.setVisibility(View.GONE);
                        newsAdapter = new NewsAdapter(SearchNews.this, 1, searchList);
                        recyclerView.setAdapter(new ScaleInAnimationAdapter(newsAdapter));
                        newsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<NewsReply> call, Throwable t) {
                    Log.d("failure", t.toString() + "");
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                if (Intent.ACTION_SEARCH.trim().equals("")) {
                    Toasty.info(this, "Please type your keyword to search.", Toast.LENGTH_LONG).show();
                }
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
