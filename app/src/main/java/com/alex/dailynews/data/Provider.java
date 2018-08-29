package com.alex.dailynews.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Provider extends ContentProvider {

    private static final int ROWS1 = 100;
    private static final int ROW_ID1 = 101;
    private static final int ROWS2 = 200;
    private static final int ROW_ID2 = 201;
    private final static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_ARTICLES + "/#", ROW_ID1);
        mUriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_ARTICLES, ROWS1);

        mUriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_MYDATA + "/#", ROW_ID2);
        mUriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_MYDATA, ROWS2);
    }

    NewsSQLiteHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new NewsSQLiteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase mSqLite = mHelper.getReadableDatabase();
        Cursor mCursor;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case ROWS1:
                mCursor = mSqLite.query(Contract.ContractValues.TABLE_NEWS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ROWS2:
                mCursor = mSqLite.query(Contract.ContractValues.TABLE_MYDATA, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ROW_ID1:
                selection = com.alex.dailynews.data.Contract.ContractValues._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                mCursor = mSqLite.query(com.alex.dailynews.data.Contract.ContractValues.TABLE_NEWS, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ROW_ID2:
                selection = com.alex.dailynews.data.Contract.ContractValues._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                mCursor = mSqLite.query(com.alex.dailynews.data.Contract.ContractValues.TABLE_MYDATA, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Query Error " + uri);
        }
        mCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return mCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase mSqLite = mHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        long ID;
        Uri uriReturn;
        switch (match) {
            case ROWS1:
                ID = mSqLite.insertOrThrow(com.alex.dailynews.data.Contract.ContractValues.TABLE_NEWS, null, contentValues);
                if (ID > 0) {
                    uriReturn = ContentUris.withAppendedId(uri, ID);
                } else {
                    throw new IllegalArgumentException("Error " + uri);
                }
                break;
            case ROWS2:
                ID = mSqLite.insertOrThrow(com.alex.dailynews.data.Contract.ContractValues.TABLE_MYDATA, null, contentValues);
                if (ID > 0) {
                    uriReturn = ContentUris.withAppendedId(uri, ID);
                } else {
                    throw new IllegalArgumentException("Error " + uri);
                }
                break;
            default:
                throw new IllegalArgumentException("Insert Error " + uri);
        }
        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        SQLiteDatabase mSqLite = mHelper.getWritableDatabase();
        int match = mUriMatcher.match(uri);
        int ID;
        switch (match) {
            case ROWS1:
                ID = mSqLite.delete(com.alex.dailynews.data.Contract.ContractValues.TABLE_NEWS, s, strings);
                break;
            case ROWS2:
                ID = mSqLite.delete(com.alex.dailynews.data.Contract.ContractValues.TABLE_MYDATA, s, strings);
                break;
            case ROW_ID1:
                s = com.alex.dailynews.data.Contract.ContractValues._ID + " =?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                ID = mSqLite.delete(Contract.ContractValues.TABLE_NEWS, s, strings);
                break;
            case ROW_ID2:
                s = com.alex.dailynews.data.Contract.ContractValues._ID + " =?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                ID = mSqLite.delete(Contract.ContractValues.TABLE_MYDATA, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Error delete_menu " + uri);
        }
        if (ID != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return ID;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
