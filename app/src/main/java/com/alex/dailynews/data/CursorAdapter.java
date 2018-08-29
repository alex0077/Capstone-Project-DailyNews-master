package com.alex.dailynews.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alex.dailynews.R;


public class CursorAdapter extends android.widget.CursorAdapter {


    public CursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View v = LayoutInflater.from(context).inflate(R.layout.listview_layout, viewGroup, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView mTextName = view.findViewById(R.id.name_layout);
        TextView mTextUrl = view.findViewById(R.id.url_layout);

        int id = cursor.getColumnIndexOrThrow(com.alex.dailynews.data.Contract.ContractValues._ID);
        int name = cursor.getColumnIndexOrThrow(com.alex.dailynews.data.Contract.ContractValues.COLUMN_NAME);
        int url = cursor.getColumnIndexOrThrow(Contract.ContractValues.COLUMN_URL);

        mTextName.setText(cursor.getString(name));
        mTextUrl.setText(cursor.getString(url));

    }
}
