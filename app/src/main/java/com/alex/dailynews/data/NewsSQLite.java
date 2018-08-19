package com.alex.dailynews.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.alex.dailynews.model.NewsArticle;
import com.alex.dailynews.model.SourceInfo;
import com.alex.dailynews.model.SourcesList;

public class NewsSQLite extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private final String TAG = getClass().getSimpleName();

    private final String CREATE_NEWS_TABLE =
            "CREATE TABLE " + Constants.TABLE_NEWS + " (" + Constants.AUTHOR + " VARCHAR , " +
                    Constants.TITLE + " VARCHAR PRIMARY KEY , " +
                    Constants.DESCRIPTION + " VARCHAR , " +
                    Constants.URL + " VARCHAR , " +
                    Constants.URL_TO_IMAGE + " VARCHAR , " +
                    Constants.SOURCES_NAME + " VARCHAR , " +
                    Constants.SOURCES_ID + " VARCHAR , " +
                    Constants.PUBLISHED_AT + " VARCHAR )";

    private final String CREATE_SOURCES_TABLE =
            "CREATE TABLE " + Constants.TABLE_SOURCES + " (" + Constants.SOURCE + " VARCHAR PRIMARY KEY , " +
                    Constants.IS_SELECTED + " BOOLEAN )";

    public NewsSQLite(Context context) {
        super(context, Constants.DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v("TAG", "create after drop");
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_SOURCES);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_FAVORITE);
        db.execSQL(CREATE_NEWS_TABLE);
        db.execSQL(CREATE_SOURCES_TABLE);
        String CREATE_FAVORITE_TABLE = "CREATE TABLE " + Constants.TABLE_FAVORITE + " (" + Constants.AUTHOR + " VARCHAR , " +
                Constants.TITLE + " VARCHAR PRIMARY KEY , " +
                Constants.DESCRIPTION + " VARCHAR , " +
                Constants.URL + " VARCHAR , " +
                Constants.URL_TO_IMAGE + " VARCHAR , " +
                Constants.SOURCES_NAME + " VARCHAR , " +
                Constants.SOURCES_ID + " VARCHAR , " +
                Constants.PUBLISHED_AT + " VARCHAR )";
        db.execSQL(CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_SOURCES);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_FAVORITE);
        onCreate(db);
    }

    public void addAllNews(List<NewsArticle> newsArticle) {
        Log.d(TAG, String.valueOf(newsArticle.size()));
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int flag = 0;
        for (int i = newsArticle.size() - 1; i >= 0; i--) {     //storing in reverse

            Cursor cursor = db.rawQuery("SELECT " + Constants.TITLE + " FROM " + Constants.TABLE_NEWS, null);
            for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
                if (String.valueOf(newsArticle.get(i).getTitle()).equals(cursor.getString(cursor.getColumnIndex(Constants.TITLE)))) {
                    flag = 1;
                }
            }
            cursor.close();
            if (flag == 0) {
                values.put(Constants.AUTHOR, newsArticle.get(i).getAuthor());
                values.put(Constants.TITLE, newsArticle.get(i).getTitle());
                values.put(Constants.DESCRIPTION, newsArticle.get(i).getDescription());
                values.put(Constants.URL, newsArticle.get(i).getUrl());
                values.put(Constants.URL_TO_IMAGE, newsArticle.get(i).getUrlToImage());
                values.put(Constants.PUBLISHED_AT, newsArticle.get(i).getPublishedAt());
                values.put(Constants.SOURCES_NAME, newsArticle.get(i).getSourceInfo().getName());
                values.put(Constants.SOURCES_ID, newsArticle.get(i).getSourceInfo().getId());

                db.insert(Constants.TABLE_NEWS, null, values);
            }

        }

        db.close();
    }


    public ArrayList<NewsArticle> getAllRows() {
        ArrayList<NewsArticle> newsList = new ArrayList<>();
        String selectAllRows = "SELECT * FROM " + Constants.TABLE_NEWS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAllRows, null);

        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            NewsArticle newsArticleObject = new NewsArticle();
            newsArticleObject.setAuthor(cursor.getString(cursor.getColumnIndex(Constants.AUTHOR)));
            newsArticleObject.setTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE)));
            newsArticleObject.setDescription(cursor.getString(cursor.getColumnIndex(Constants.DESCRIPTION)));
            newsArticleObject.setUrl(cursor.getString(cursor.getColumnIndex(Constants.URL)));
            newsArticleObject.setUrlToImage(cursor.getString(cursor.getColumnIndex(Constants.URL_TO_IMAGE)));
            newsArticleObject.setPublishedAt(cursor.getString(cursor.getColumnIndex(Constants.PUBLISHED_AT)));
            newsArticleObject.setSourceInfo(new SourceInfo(cursor.getString(cursor.getColumnIndex(Constants.SOURCES_NAME)), cursor.getString(cursor.getColumnIndex(Constants.SOURCES_ID))));
            newsList.add(newsArticleObject);
        }
        cursor.close();
        db.close();
        return newsList;
    }

    public ArrayList<NewsArticle> getTopNRows(int limit) {       //use limit to get tuples from the bottom
        ArrayList<NewsArticle> newsList = new ArrayList<>();
        String selectAllRows = "SELECT * FROM " + Constants.TABLE_NEWS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAllRows, null);

        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            limit--;
            NewsArticle newsArticleObject = new NewsArticle();
            newsArticleObject.setAuthor(cursor.getString(cursor.getColumnIndex(Constants.AUTHOR)));
            newsArticleObject.setTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE)));
            newsArticleObject.setDescription(cursor.getString(cursor.getColumnIndex(Constants.DESCRIPTION)));
            newsArticleObject.setUrl(cursor.getString(cursor.getColumnIndex(Constants.URL)));
            newsArticleObject.setUrlToImage(cursor.getString(cursor.getColumnIndex(Constants.URL_TO_IMAGE)));
            newsArticleObject.setPublishedAt(cursor.getString(cursor.getColumnIndex(Constants.PUBLISHED_AT)));
            newsArticleObject.setSourceInfo(new SourceInfo(cursor.getString(cursor.getColumnIndex(Constants.SOURCES_NAME)), cursor.getString(cursor.getColumnIndex(Constants.SOURCES_ID))));
            newsList.add(newsArticleObject);
            if (limit == 0) break;
        }
        cursor.close();
        db.close();
        return newsList;
    }

    public int getRowCount() {
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(readableDatabase, Constants.TABLE_NEWS);
    }

    public void dropNewsTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NEWS);
        db.execSQL(CREATE_NEWS_TABLE);
    }

    public void dropSourcesTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_SOURCES);
        db.execSQL(CREATE_SOURCES_TABLE);
    }

    public ArrayList<NewsArticle> getRowByPosition(int position) {
        ArrayList<NewsArticle> newsList = new ArrayList<>();
        String selectAllRows = "SELECT * FROM " + Constants.TABLE_NEWS;
        //Set into writable mode
        SQLiteDatabase db = this.getReadableDatabase();
        //Used to read write values
        Cursor cursor = db.rawQuery(selectAllRows, null);

        if (cursor.moveToPosition(position)) {

            NewsArticle newsArticleObject = new NewsArticle();
            newsArticleObject.setAuthor(cursor.getString(cursor.getColumnIndex(Constants.AUTHOR)));
            newsArticleObject.setTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE)));
            newsArticleObject.setDescription(cursor.getString(cursor.getColumnIndex(Constants.DESCRIPTION)));
            newsArticleObject.setUrl(cursor.getString(cursor.getColumnIndex(Constants.URL)));
            newsArticleObject.setUrlToImage(cursor.getString(cursor.getColumnIndex(Constants.URL_TO_IMAGE)));
            newsArticleObject.setPublishedAt(cursor.getString(cursor.getColumnIndex(Constants.PUBLISHED_AT)));
            newsArticleObject.setSourceInfo(new SourceInfo(cursor.getString(cursor.getColumnIndex(Constants.SOURCES_NAME)), cursor.getString(cursor.getColumnIndex(Constants.SOURCES_ID))));
            newsList.add(newsArticleObject);

        }

        cursor.close();
        db.close();
        Log.v("TAG", "Before list return");
        return newsList;
    }

    public void addSource(String source) {
        Log.v("addSource", source);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.SOURCE, source);
        values.put(Constants.IS_SELECTED, false);

        sqLiteDatabase.insert(Constants.TABLE_SOURCES, null, values);
    }

    public ArrayList<SourcesList> getAllSources() {
        ArrayList<SourcesList> sourceList = new ArrayList<>();
        int i = 0;
        String selectAllRows = "SELECT * FROM " + Constants.TABLE_SOURCES;
        //Set into writable mode
        SQLiteDatabase db = this.getReadableDatabase();
        //Used to read write values
        Cursor cursor = db.rawQuery(selectAllRows, null);

        if (cursor.moveToFirst()) {  // Returns false if cursor is empty

            do {
                SourcesList sourceObject = new SourcesList();
                sourceObject.setSource(cursor.getString(0));
                sourceObject.setSelected(Boolean.parseBoolean(cursor.getString(1)));
                sourceList.add(sourceObject);
                Log.v("All", String.valueOf(sourceList.get(i++).getSource()));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.v("TAG", "Before list return");
        return sourceList;
    }

    public int getNewsCount() {
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NEWS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void dropAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    /*
    Favorite methods
    */

    public void addNewFavoriteRow(ArrayList<NewsArticle> favoriteArticle) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Constants.AUTHOR, favoriteArticle.get(0).getAuthor());
        values.put(Constants.TITLE, favoriteArticle.get(0).getTitle());
        values.put(Constants.DESCRIPTION, favoriteArticle.get(0).getDescription());
        values.put(Constants.URL, favoriteArticle.get(0).getUrl());
        values.put(Constants.URL_TO_IMAGE, favoriteArticle.get(0).getUrlToImage());
        values.put(Constants.PUBLISHED_AT, favoriteArticle.get(0).getPublishedAt());
        values.put(Constants.SOURCES_NAME, favoriteArticle.get(0).getSourceInfo().getName());
        values.put(Constants.SOURCES_ID, favoriteArticle.get(0).getSourceInfo().getId());

        db.insert(Constants.TABLE_FAVORITE, null, values);
        db.close();
    }

    public ArrayList<NewsArticle> getFavoriteRows() {
        ArrayList<NewsArticle> newsList = new ArrayList<>();
        String selectAllRows = "SELECT * FROM " + Constants.TABLE_FAVORITE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAllRows, null);

        for (cursor.moveToLast(); !cursor.isBeforeFirst(); cursor.moveToPrevious()) {
            NewsArticle newsArticleObject = new NewsArticle();
            newsArticleObject.setAuthor(cursor.getString(cursor.getColumnIndex(Constants.AUTHOR)));
            newsArticleObject.setTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE)));
            newsArticleObject.setDescription(cursor.getString(cursor.getColumnIndex(Constants.DESCRIPTION)));
            newsArticleObject.setUrl(cursor.getString(cursor.getColumnIndex(Constants.URL)));
            newsArticleObject.setUrlToImage(cursor.getString(cursor.getColumnIndex(Constants.URL_TO_IMAGE)));
            newsArticleObject.setPublishedAt(cursor.getString(cursor.getColumnIndex(Constants.PUBLISHED_AT)));
            newsArticleObject.setSourceInfo(new SourceInfo(cursor.getString(cursor.getColumnIndex(Constants.SOURCES_NAME)), cursor.getString(cursor.getColumnIndex(Constants.SOURCES_ID))));
            newsList.add(newsArticleObject);
        }
        cursor.close();
        db.close();
        return newsList;
    }

    public void deleteFavoriteRow(String title) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            db.delete(Constants.TABLE_FAVORITE, "title = ?", new String[]{title});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}