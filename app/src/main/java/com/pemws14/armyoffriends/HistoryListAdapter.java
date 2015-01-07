package com.pemws14.armyoffriends;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.pemws14.armyoffriends.database.DbHistory;

import java.util.List;
import java.util.Locale;

/**
 * Created by Martin on 07.01.2015.
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {
    private List<DbHistory> mDataset;
    public Context mContext;
    public FragmentManager mFragmentManager;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePic;
        public TextView result;
        public TextView detail;
        public TextView date;

        public ViewHolder(View v) {
            super(v);

            profilePic = (ImageView) v.findViewById(R.id.history_list_picture);
            result = (TextView) v.findViewById(R.id.history_list_result);
            detail = (TextView) v.findViewById(R.id.history_list_detail);
            date = (TextView) v.findViewById(R.id.history_list_date);
        }
    }

    public HistoryListAdapter(List<DbHistory> myDataset, Context context, FragmentManager fragmentManager) {
        mDataset = myDataset;
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    @Override
    public HistoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final DbHistory history = mDataset.get(position);

        //set result, text and date
        holder.result.setText("Relations: " + history.getOwnStrength() + " vs. " + history.getEnemyStrength());
        if (history.getResult()){
            holder.detail.setText("You won against " + history.getEnemyName() + "!");
            holder.detail.setTextColor(0xff006400);
        }else {
            holder.detail.setText("You lost against " + history.getEnemyName() + "!");
            holder.detail.setTextColor(0xff990000);
        }

        getDate(holder,history,0);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void getDate(ViewHolder holder, DbHistory history, int i){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -i);

        String historySplit[] = history.getCreated_at().split("\\s+");
        String split[] = dateFormat.format(cal.getTime()).split("\\s+");

        if (split[0].equals(historySplit[0])) {
            if(i==0){
                holder.date.setText("today, " + historySplit[1].substring(0,5));
            }else if(i==1){
                holder.date.setText("yesterday, " + historySplit[1].substring(0,5));
            }else if(2<=i && i<14){
                holder.date.setText(i + " days ago");
            }else if(14<=i && i<=200){
                holder.date.setText((i+1)/7 + " weeks ago");
            }else if(i>200){
                holder.date.setText("a long time ago");
            }
        }else{
            getDate(holder, history, ++i);
        }
    }
}
