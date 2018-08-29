package com.alex.dailynews.activities;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alex.dailynews.R;
import com.alex.dailynews.data.Contract;

import es.dmoral.toasty.Toasty;

//import android.content.Intent;

public class MyNews extends AppCompatActivity {

    private EditText mName;
    private EditText mUrl;
    private Button mBtnInsertData;
    private Button mBtnShowData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mynews);

        //setting obj
        mName = findViewById(R.id.editName);
        mUrl = findViewById(R.id.editUrl);
        mBtnInsertData = findViewById(R.id.btn_insert);
        //mBtnShowData = findViewById(R.id.btn_show);

        mBtnInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertData();
                Toasty.success(MyNews.this, getResources().getString(R.string.toast_data_inserted), Toast.LENGTH_LONG, true).show();
            }
        });
       /* mBtnShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),   MainActivity.class);
                intent.putExtra("key", 777); //Optional parameters
                startActivity(intent);
            }
        });*/
    }

    public void InsertData() {

        ContentValues mContentValues = new ContentValues();
        mContentValues.put(Contract.ContractValues.COLUMN_NAME, mName.getText().toString());
        mContentValues.put(Contract.ContractValues.COLUMN_URL, mUrl.getText().toString());
        Uri insert = getContentResolver().insert(Contract.URI_CONTENT, mContentValues);
        mName.setText("");
        mUrl.setText("");

    }

}
