package com.alex.dailynews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.alex.dailynews.R;
import com.alex.dailynews.data.NewsSQLiteHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Splash extends AppCompatActivity {

    private NewsSQLiteHelper newsSQLiteHelper;

    public static Intent createLaunchActivityIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("key", 877); //Optional parameters
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        newsSQLiteHelper = new NewsSQLiteHelper(this);
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
                        new NewsSQLiteHelper(Splash.this).dropAllTables();
                        //  Make a new preferences editor
                        SharedPreferences.Editor e = getPrefs.edit();

                        //  Make it false to avoid this to run again
                        e.putBoolean("firstStart", false);

                        //  Apply changes
                        e.apply();

                        newsSQLiteHelper.addSource("the-new-york-times");
                        newsSQLiteHelper.addSource("time");
                        newsSQLiteHelper.addSource("cbs-news");
                        newsSQLiteHelper.addSource("cnn");
                        newsSQLiteHelper.addSource("national-geographic");
                        newsSQLiteHelper.addSource("mtv-news");
                        newsSQLiteHelper.addSource("google-news");

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
