package com.alex.dailynews.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alex.dailynews.R;
import com.alex.dailynews.model.SourcesList;

import java.util.ArrayList;
import java.util.Locale;

public class SourcesAdapter extends RecyclerView.Adapter<SourcesAdapter.ViewHolder> {

    private final ArrayList<SourcesList> sourceList;
    private final ArrayList<SourcesList> sourceListCopy;
    private final ArrayList<SourcesList> selectedList = new ArrayList<SourcesList>();

    public SourcesAdapter(ArrayList<SourcesList> sourceList, Context mContext) {
        this.sourceList = sourceList;
        sourceListCopy = new ArrayList<>();
        sourceListCopy.addAll(sourceList);
        Log.v("list", String.valueOf(sourceList));
        Context mContext1 = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.source_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;
        holder.sourceText.setText(sourceList.get(pos).getSource());
        holder.checkBox.setChecked(sourceList.get(pos).isSelected());
        holder.checkBox.setTag(sourceList.get(pos));
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cB = (CheckBox) v;
                SourcesList item = (SourcesList) cB.getTag();
                item.setSelected(cB.isChecked());
                sourceList.get(pos).setSelected(cB.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (sourceList == null) {
            return 0;
        } else {
            return sourceList.size();
        }
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ArrayList<SourcesList> getSelectedList() {
        for (int i = 0; i < sourceList.size(); i++) {
            if (sourceList.get(i).isSelected()) {
                selectedList.add(sourceList.get(i));
            }
        }
        return selectedList;
    }

    public int getSelectedSize() {
        return selectedList.size();
    }

    public void filter(String searchText) {
        searchText = searchText.toLowerCase(Locale.getDefault());

        sourceList.clear();
        if (searchText.length() == 0) {
            sourceList.addAll(sourceListCopy);
        } else {

            for (int i = 0; i < sourceListCopy.size(); i++) {

                if (sourceListCopy.get(i).getSource().toLowerCase(Locale.getDefault()).contains(searchText)) {
                    sourceList.add(sourceListCopy.get(i));
                }
            }
        }

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView sourceText;
        final CheckBox checkBox;
        final LinearLayout checkboxLayout;

        ViewHolder(View itemView) {
            super(itemView);
            sourceText = itemView.findViewById(R.id.source);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkboxLayout = itemView.findViewById(R.id.checkboxLayout);
        }

    }

}
