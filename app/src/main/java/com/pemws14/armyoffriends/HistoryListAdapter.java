package com.pemws14.armyoffriends;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pemws14.armyoffriends.database.DbHistory;

import java.util.List;

/**
 * Created by Martin on 07.01.2015.
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<DbHistory> mDataset;
    public Context mContext;
    public FragmentManager mFragmentManager;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView profilePic;
        public TextView result;
        public TextView detail;

        public ViewHolder(View v) {
            super(v);

            profilePic = (ImageView) v.findViewById(R.id.history_list_picture);
            result = (TextView) v.findViewById(R.id.history_list_result);
            detail = (TextView) v.findViewById(R.id.history_list_detail);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HistoryListAdapter(List<DbHistory> myDataset, Context context, FragmentManager fragmentManager) {
        mDataset = myDataset;
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HistoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final DbHistory history = mDataset.get(position);

        holder.result.setText("Relations: " + history.getOwnStrength() + " vs. " + history.getEnemyStrength());
        if (history.getResult()){
            holder.detail.setText("You won against " + history.getEnemyName() + "!");
            holder.detail.setTextColor(0xff006400);
        }else {
            holder.detail.setText("You lost against " + history.getEnemyName() + "!");
            holder.detail.setTextColor(0xff990000);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
