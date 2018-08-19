package com.alex.dailynews.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import com.alex.dailynews.R;
import com.alex.dailynews.adapters.NewsAdapter;
import com.alex.dailynews.data.NewsSQLite;
import com.alex.dailynews.model.NewsArticle;

public class Favorite extends Fragment {

    private NewsSQLite newsSQLite;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    //TextView emptyState;
    private ArrayList<NewsArticle> favoriteArrayList;

    public Favorite() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        newsSQLite = new NewsSQLite(getActivity());
        recyclerView = rootView.findViewById(R.id.favorite_recycler_view);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefresh);
        TextView emptyState = rootView.findViewById(R.id.emptyText);
        favoriteArrayList = new ArrayList<>();

        new getNewsFromDb().execute();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new getNewsFromDb().execute();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh Fragment
            swipeRefreshLayout.setRefreshing(true);
            new getNewsFromDb().execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Call the data setup methods again, to reflect
        // the changes which took place then the Fragment was paused
                swipeRefreshLayout.setRefreshing(true);
                new getNewsFromDb().execute();
    }

    class getNewsFromDb extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            favoriteArrayList = newsSQLite.getFavoriteRows();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("MessageFragment", "onPostExecute messagesArrayList size=" + favoriteArrayList.size());
            if (favoriteArrayList.size() == 0) {

            } else {
                NewsAdapter unreadNewsAdapter = new NewsAdapter(getActivity(), 3, favoriteArrayList);
                recyclerView.setAdapter(new ScaleInAnimationAdapter(unreadNewsAdapter));
                unreadNewsAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
