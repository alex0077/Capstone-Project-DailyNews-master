package com.alex.dailynews.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.alex.dailynews.R;
import com.alex.dailynews.data.Contract;

public class NewsWidgetServices extends RemoteViewsService {

    String[] PROJECTION = {
            // Contract.ContractValues._ID,
            Contract.ContractValues.AUTHOR,
            Contract.ContractValues.TITLE,
            Contract.ContractValues.DESCRIPTION,
            Contract.ContractValues.URL,
            Contract.ContractValues.URL_TO_IMAGE,
            Contract.ContractValues.PUBLISHED_AT,
            Contract.ContractValues.SOURCES_NAME,
            Contract.ContractValues.SOURCES_ID
    };
    private Context context;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                String order = Contract.ContractValues.PUBLISHED_AT + " DESC";
                data = getContentResolver().query(Contract.ContractValues.CONTENT_URI1,
                        PROJECTION,
                        null,
                        null,
                        order);
                Log.d("Widget data", data + "");
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                String newsTitle = data.getString(data.getColumnIndex(Contract.ContractValues.TITLE));
                String newsDes = data.getString(data.getColumnIndex(Contract.ContractValues.DESCRIPTION));
                String newsSources = data.getString(data.getColumnIndex(Contract.ContractValues.SOURCES_NAME));
                String newsUrl = data.getString(data.getColumnIndex(Contract.ContractValues.URL));

                Log.d("Widget title", newsTitle + "");

                long newsID = data.getLong(data.getColumnIndex(Contract.ContractValues.TITLE));
                final RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_list_item);


                views.setTextViewText(R.id.news_title, newsTitle);
                views.setTextViewText(R.id.widget_description_view, newsDes);
                views.setTextViewText(R.id.widget_source_view, newsSources);

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra("rowId", newsID);
                views.setOnClickFillInIntent(R.id.news_list, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}
