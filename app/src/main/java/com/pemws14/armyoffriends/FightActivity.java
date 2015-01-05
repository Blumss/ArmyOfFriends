package com.pemws14.armyoffriends;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.pemws14.armyoffriends.database.DbFight;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.drawer.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class FightActivity extends BaseActivity {
    private View view;
    private DbHelper db;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter fightAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //<-- IN EVERY ACTIVITY WITH DRAWER
        super.set();
        // catches Frame, where to insert actual ActivityView
        ViewGroup parent = (ViewGroup) findViewById(R.id.content_frame);
        //View of new activity
        view = LayoutInflater.from(this).inflate(R.layout.activity_fight, parent, false);
        parent.addView(view);
        //--> IN EVERY ACTIVITY WITH DRAWER

        recyclerView = (RecyclerView)findViewById(R.id.fightListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<String[]> list = buildDummyData();

        fightAdapter = new FightListAdapter(list, getApplicationContext());
        recyclerView.setAdapter(fightAdapter);

        /*ListView listview = (ListView) findViewById(R.id.fightListView);
        Resources res = this.getResources();
        Drawable divider = res.getDrawable(R.drawable.list_divider);
        listview.setDivider(divider);
        listview.setDividerHeight(1);
        List<String[]> list = buildDummyData();
        final FightListAdapter fightAdapter = new FightListAdapter(this, list);
        listview.setAdapter(fightAdapter);*/
    }

    private List<String[]> buildDummyData() {
        String[] ranks = getResources().getStringArray(R.array.army_ranks);
        List<DbFight> fights;
        List<String[]> enemies = new ArrayList<String[]>();
        db = new DbHelper(getApplicationContext());
        /*
        DbFight fight1 = new DbFight("abc",1,3);
        DbFight fight2 = new DbFight("def",2,4);
        DbFight fight3 = new DbFight("ghi",3,5);

        db.createFight(fight1);
        db.createFight(fight2);
        db.createFight(fight3);
        */
        fights = db.getAllFights();
        for(DbFight fight:fights){
            String[] enemy = new String[5];
            enemy[0] = String.valueOf(fight.getId());
            enemy[1] = fight.getName();
            enemy[2] = String.valueOf(fight.getStrength());
            enemy[3] = ranks[fight.getMaxLevel()];
            enemy[4] = fight.getCreated_at();
            enemies.add(enemy);
        }
//        Log.i("2enemies: " + enemies, "");

        return enemies;
    }
}
