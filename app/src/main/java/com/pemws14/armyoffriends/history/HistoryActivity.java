package com.pemws14.armyoffriends.history;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pemws14.armyoffriends.R;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbHistory;
import com.pemws14.armyoffriends.drawer.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HistoryActivity extends BaseActivity {
    private View view;

    private DbHelper dbHelper;
    private DbHistory dbHistory;

    private List<DbHistory> historyList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter historyAdapter;

    private Integer ownStrength;
    private Boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //<-- IN EVERY ACTIVITY WITH DRAWER
        super.set();
        // catches Frame, where to insert actual ActivityView
        ViewGroup parent = (ViewGroup) findViewById(R.id.content_frame);
        //View of new activity
        view = LayoutInflater.from(this).inflate(R.layout.activity_history, parent, false);
        parent.addView(view);
        //--> IN EVERY ACTIVITY WITH DRAWER

        //get data from DB
        dbHelper = DbHelper.getInstance(getApplicationContext());
        historyList = dbHelper.getAllHistory();
        Collections.reverse(historyList);

        //Set up View
        recyclerView = (RecyclerView) findViewById(R.id.historyListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        historyAdapter = new HistoryListAdapter(historyList, getApplicationContext(), getFragmentManager());
        recyclerView.setAdapter(historyAdapter);

        // Show total wins and losses (would be more efficient when saved in specialDB
        // TODO: Maybe a new DB-Entry? Maybe in Profile
        int wins = 0;
        int losses = 0;
        for(DbHistory history:historyList){
            if(history.getResult()){
                wins++;
            }else{
                losses++;
            }
        }
        TextView won = (TextView) view.findViewById(R.id.history_info_total_wins);
        TextView lost = (TextView) view.findViewById(R.id.fight_info_total_losses);
        won.setText(new Integer(wins).toString());
        lost.setText(new Integer(losses).toString());
    }

    public static int getDateDifference (long historyTime, int day){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -day);
        String comparator = dateFormat.format(cal.getTime()).split("\\s+")[0];
        String date = dateFormat.format(new Date(historyTime*1000)).split("\\s+")[0];

        if (comparator.equals(date)){
            return day;
        }else{
            return getDateDifference(historyTime,++day);
        }
    }
}
