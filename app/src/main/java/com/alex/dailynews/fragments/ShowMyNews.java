package com.alex.dailynews.fragments;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.dailynews.R;
import com.alex.dailynews.data.Contract;
import com.alex.dailynews.data.CursorAdapter;

import es.dmoral.toasty.Toasty;

public class ShowMyNews extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    CursorAdapter mAdapter;
    private View rootView;
    private ListView mList;
    private Button mBtnDeleteData;

    public ShowMyNews() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CursorAdapter(this.getActivity(), null);
        getActivity().getLoaderManager().initLoader(0, null, this);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_showmynews, container, false);
        mList = rootView.findViewById(R.id.showData);
        getActivity().getLoaderManager().initLoader(0, null, this);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mList.setAdapter(mAdapter);
        mBtnDeleteData = getActivity().findViewById(R.id.btn_delete);
        getActivity().getLoaderManager().initLoader(0, null, this);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView mTextUrl = view.findViewById(R.id.url_layout);

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://" + mTextUrl.getText().toString()));
                startActivity(intent);

               /* Toast.makeText(getActivity().getApplicationContext(),
                        "Click ListItem Number " + mTextUrl.getText() + " " + position, Toast.LENGTH_LONG)
                        .show();*/
            }
        });

        mBtnDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteData();
                Toasty.success(getActivity().getApplicationContext(), getResources().getString(R.string.toast_data_deleted), Toast.LENGTH_LONG, true).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = new CursorAdapter(this.getActivity(), null);
        mList = rootView.findViewById(R.id.showData);
        mList.setAdapter(mAdapter);
        getActivity().getLoaderManager().initLoader(0, null, this);
    }

    public void DeleteData() {
        getActivity().getContentResolver().delete(Contract.URI_CONTENT, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projector = {
                Contract.ContractValues._ID,
                Contract.ContractValues.COLUMN_NAME,
                Contract.ContractValues.COLUMN_URL
        };
        return new CursorLoader(getActivity().getApplicationContext(), Contract.URI_CONTENT, projector, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
