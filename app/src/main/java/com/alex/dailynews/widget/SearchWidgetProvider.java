package com.alex.dailynews.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.alex.dailynews.R;
import com.alex.dailynews.activities.Splash;


public class SearchWidgetProvider extends AppWidgetProvider {
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int appWidgetId : appWidgetIds) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId,
                    Splash.createLaunchActivityIntent(context), 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_search);
            views.setOnClickPendingIntent(R.id.widget_search, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }
}
