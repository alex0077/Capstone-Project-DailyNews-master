package com.alex.dailynews.network;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alex.dailynews.data.NewsSQLiteHelper;
import com.alex.dailynews.model.NewsArticle;
import com.alex.dailynews.model.NewsReply;
import com.alex.dailynews.model.SourcesList;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class BackgroundSyncJobService extends JobService {
    private static final String PRIMARY_CHANNEL = "default";
    private static final String SOURCES = "sources";
    private static final String COUNTS = "counts";
    private final StringBuilder sourcesString = new StringBuilder();
    private NewsSQLiteHelper newsSQLiteHelper;
    private ArrayList<SourcesList> sourceArrayList;
    private ArrayList<NewsArticle> newsArticleArrayList = new ArrayList<>();
    private AsyncTask mGetSourcesTask;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Bundle bundle;

    @Override
    public boolean onStartJob(JobParameters job) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        newsSQLiteHelper = new NewsSQLiteHelper(this);
        mGetSourcesTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] objects) {
                sourceArrayList = new ArrayList<>();
                sourceArrayList = newsSQLiteHelper.getAllSources();
                for (int i = 0; i < sourceArrayList.size(); i++) {
                    sourcesString.append(sourceArrayList.get(i).getSource());
                    sourcesString.append(",");
                }
                bundle = new Bundle();
                bundle.putString("sources_string", sourcesString.toString());
                mFirebaseAnalytics.logEvent(SOURCES, bundle);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                networkCall();
            }
        };
        mGetSourcesTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mGetSourcesTask != null) {
            mGetSourcesTask.cancel(true);
        }
        return true;
    }

    private void networkCall() {
        APIClient apiClient = RetrofitInstance.getRetrofitInstance().create(APIClient.class);

        Call<NewsReply> call = apiClient.getTopHeadlines(sourcesString.toString());

        call.enqueue(new Callback<NewsReply>() {
            @Override
            public void onResponse(@NonNull Call<NewsReply> call, @NonNull retrofit2.Response<NewsReply> response) {

                NewsReply newsReply = response.body();
                if (newsReply != null) {
                    int previousCount = newsSQLiteHelper.getNewsCount();
                    newsSQLiteHelper.addAllNews(newsReply.getNewsArticleList());
                    getUnread(previousCount);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NewsReply> call, @NonNull Throwable t) {
                Log.d("failure", t.toString() + "");
            }
        });

    }

    private void getUnread(final int previousCount) {
        int currentCount = newsSQLiteHelper.getNewsCount();
        final int difference = currentCount - previousCount;
        bundle = new Bundle();
        bundle.putString("currentCount", String.valueOf(currentCount));
        bundle.putString("previousCount", String.valueOf(previousCount));
        bundle.putString("difference", String.valueOf(difference));
        mFirebaseAnalytics.logEvent(COUNTS, bundle);
        if (difference > 0) {
            AsyncTask mGetTopNewsTask = new AsyncTask() {

                @Override
                protected Object doInBackground(Object[] objects) {
                    newsArticleArrayList = new ArrayList<>();
                    newsArticleArrayList = newsSQLiteHelper.getTopNRows(difference);
                    return null;
                }

              /*  @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);
                    sendNotification(difference);
                }*/
            };
            mGetTopNewsTask.execute();
        }
    }
}
