package com.alex.dailynews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.alex.dailynews.R;
import com.alex.dailynews.data.NewsSQLite;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Splash extends AppCompatActivity {

    private NewsSQLite newsSQLite;

    public static Intent createLaunchActivityIntent(Context context)
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("key", 877); //Optional parameters
        context.startActivity(intent);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        newsSQLite = new NewsSQLite(this);
        final SharedPreferences getPrefs = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        final boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

        if (isFirstStart) {
            final Thread thread = new Thread() {
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        new NewsSQLite(Splash.this).dropAllTables();
                        //  Make a new preferences editor
                        SharedPreferences.Editor e = getPrefs.edit();

                        //  Make it false to avoid this to run again
                        e.putBoolean("firstStart", false);

                        //  Apply changes
                        e.apply();

                        newsSQLite.addSource("the-new-york-times");
                        newsSQLite.addSource("time");
                        newsSQLite.addSource("cbs-news");
                        newsSQLite.addSource("cnn");
                        newsSQLite.addSource("national-geographic");
                        newsSQLite.addSource("mtv-news");
                        newsSQLite.addSource("google-news");

                        final Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            thread.start();

        } else {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            thread.start();

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
