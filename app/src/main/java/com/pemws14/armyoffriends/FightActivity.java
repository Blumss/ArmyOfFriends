package com.pemws14.armyoffriends;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;

import com.pemws14.armyoffriends.database.DbFight;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbHistory;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.drawer.BaseActivity;

import java.util.ArrayList;
import java.util.List;


public class FightActivity extends BaseActivity implements FightResultDialogFragment.NoticeDialogListener{
    private View view;

    private DbHelper dbHelper;
    private DbHistory dbHistory;

    private List<String[]> list;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter fightAdapter;

    private Integer level;
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
        view = LayoutInflater.from(this).inflate(R.layout.activity_fight, parent, false);
        parent.addView(view);
        //--> IN EVERY ACTIVITY WITH DRAWER

        dbHelper = new DbHelper(getApplicationContext());

        //Set up View
        recyclerView = (RecyclerView)findViewById(R.id.fightListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // TODO: replace with real data
        list = buildDummyData();
        fightAdapter = new FightListAdapter(list, getApplicationContext(), getFragmentManager());
        recyclerView.setAdapter(fightAdapter);

        //get and set own level and army strength
        TextView armyStrength = (TextView) view.findViewById(R.id.fight_info_strength);
        TextView ownLevel = (TextView) view.findViewById(R.id.fight_info_level);
        List<DbSoldier> getSoldiers = dbHelper.getLimitedSoldiers();
        ownStrength = GameMechanics.getArmyStrength(getSoldiers);
        armyStrength.setText(ownStrength.toString());
        // TODO: show own level (see commentary)
        level = GameMechanics.getPlayerLevelForEp(1000); //dummy value EP = 1000
        ownLevel.setText(level.toString());
    }

    private List<String[]> buildDummyData() {
        String[] ranks = getResources().getStringArray(R.array.army_ranks);
        List<DbFight> fights;
        List<String[]> enemies = new ArrayList<String[]>();
        /*
        DbFight fight1 = new DbFight("abc",10,1,3);
        DbFight fight2 = new DbFight("def",20,2,4);
        DbFight fight3 = new DbFight("ghi",30,3,5);
        DbFight fight4 = new DbFight("jkl",40,3,2);
        DbFight fight5 = new DbFight("mno",50,4,9);
        DbFight fight6 = new DbFight("pqr",60,5,7);
        DbFight fight7 = new DbFight("stu",70,6,1);
        DbFight fight8 = new DbFight("vwx",80,7,6);
        DbFight fight9 = new DbFight("yz0",90,8,0);

        dbHelper.createFight(fight1);
        dbHelper.createFight(fight2);
        dbHelper.createFight(fight3);
        dbHelper.createFight(fight4);
        dbHelper.createFight(fight5);
        dbHelper.createFight(fight6);
        dbHelper.createFight(fight7);
        dbHelper.createFight(fight8);
        dbHelper.createFight(fight9);
        */
        fights = dbHelper.getAllFights();
        for(DbFight fight:fights){
            String[] enemy = new String[6];
            enemy[0] = String.valueOf(fight.getId());
            enemy[1] = fight.getName();
            enemy[2] = String.valueOf(fight.getPlayerLevel());
            enemy[3] = String.valueOf(fight.getStrength());
            enemy[4] = ranks[fight.getMaxLevel()];
            enemy[5] = fight.getCreated_at();
            enemies.add(enemy);
        }
        return enemies;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int fightId, int position) throws InterruptedException {
        dialog.dismiss();
        DbFight dbFight = dbHelper.getFight(fightId);
        // FIGHT INCL DELAY -
        // TODO: PROGRESS BAR OR DIE!
        Thread.sleep(1000);
        result = GameMechanics.getFightResult(ownStrength, dbFight.getStrength()) > 0 ? true : false;

        // SAVE RESULT IN HISTORY-DB
        dbHistory = new DbHistory(level, ownStrength, dbHelper.getMaxLevel(), dbFight.getName(), dbFight.getPlayerLevel(), dbFight.getStrength(), dbFight.getMaxLevel(), result);
        dbHelper.createHistory(dbHistory);

        // REMOVE FIGTH FROM FIGHT-DB UPDATE ACTIVITY
        list.remove(position);
        fightAdapter.notifyItemRemoved(position);
        fightAdapter.notifyDataSetChanged();
        dbHelper.deleteFight(fightId);

        // SHOW RESULT IN DIALOG
        FightResultDialogFragment resultFrag = FightResultDialogFragment.newInstance(dbFight.getName(), fightId, 0, result.toString());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        resultFrag.show(ft, dbFight.getName());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}
