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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.Game;
import com.pemws14.armyoffriends.database.DbFight;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbHistory;
import com.pemws14.armyoffriends.database.DbProfile;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.database.ParseDb;
import com.pemws14.armyoffriends.drawer.BaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FightActivity extends BaseActivity implements FightResultDialogFragment.NoticeDialogListener{
    private View view;

    private DbHelper dbHelper;
    private DbHistory dbHistory;
    private DbProfile dbProfile;
    private int profileId;

    private List<DbFight> fightList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter fightAdapter;

    private Boolean daily;
    private Integer level;
    private Integer ownStrength;
    private Integer chLevel;
    private Integer chStrength;
    private String dailyName;
    private double dailyChance;
    private Boolean dailyResult;
    private double chance;
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

        //Set up View
        recyclerView = (RecyclerView)findViewById(R.id.fightListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //get fights
        dbHelper = new DbHelper(getApplicationContext());
        // TODO dbProfile = dbHelper.getProfile(0);

        //TODO create DailyChallenge
        daily = false;                      // Bool to toggle dailyChallenge (different onDialogPositiveClick-Handling)
        dailyChallenge(/*DbProfile profile*/);
        //createFights();

        fightList = dbHelper.getAllFights();
        fightAdapter = new FightListAdapter(fightList, getApplicationContext(), getFragmentManager());
        recyclerView.setAdapter(fightAdapter);

        //get and set own level and army strength
        TextView armyStrength = (TextView) view.findViewById(R.id.fight_info_strength);
        TextView ownLevel = (TextView) view.findViewById(R.id.fight_info_level);

        //TODO: get real userID (only one profile is saved on each device? so 0 might be ok?)
        //TODO: replace Ep-value with real value
        /*
        int userId = 0;
        ownStrength = new Integer(dbProfile.getArmyStrength()).toString();
        armyStrength.setText(ownStrength);
        level = new Integer(dbProfile.getPlayerLevel()).toString();
        ownLevel.setText(level);
        */


        //TODO: remove the part below when proper userID is received
        List<DbSoldier> getSoldiers = dbHelper.getAllSoldiers();
        ownStrength = GameMechanics.getArmyStrength(getSoldiers);
        armyStrength.setText(ownStrength.toString());
        level = GameMechanics.getPlayerLevelForEp(1000);
        ownLevel.setText(level.toString());
        //TODO: remove the part above
    }

    private void createFights() {
        /**********DUMMIES*******************/
        DbFight fight1 = new DbFight("abc",1,1,3);
        DbFight fight2 = new DbFight("def",2,2,4);
        DbFight fight3 = new DbFight("ghi",3,3,5);
        DbFight fight4 = new DbFight("jkl",3,3,2);
        DbFight fight5 = new DbFight("mno",4,4,9);
        DbFight fight6 = new DbFight("pqr",5,5,7);
        DbFight fight7 = new DbFight("stu",6,6,1);
        DbFight fight8 = new DbFight("vwx",7,7,6);
        DbFight fight9 = new DbFight("yz0",8,8,0);

        dbHelper.createFight(fight1);
        dbHelper.createFight(fight2);
        dbHelper.createFight(fight3);
        dbHelper.createFight(fight4);
        dbHelper.createFight(fight5);
        dbHelper.createFight(fight6);
        dbHelper.createFight(fight7);
        dbHelper.createFight(fight8);
        dbHelper.createFight(fight9);
        /***********DUMMIES******************/
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int fightId, int position) throws InterruptedException {
        dialog.dismiss();
        if (!daily){
            DbFight dbFight = dbHelper.getFight(fightId);
            // CALCULATE & SHOW FIGHT
            chance = GameMechanics.getFightResult(4/*ownArmyStrength*/, dbFight.getStrength());
            result = GameMechanics.getFightResult(4, dbFight.getStrength()) > 0 ? true : false;
            FightResultDialogFragment resultFrag = FightResultDialogFragment.newInstance(dbFight.getName(), fightId, 0,dbFight.getStrength(), result.toString(), chance);
            System.out.println("Chance: " + chance + " - Result: " + result.toString());
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            resultFrag.show(ft, dbFight.getName());

        /*
            // SAVE RESULT IN HISTORY-DB
            dbHistory = new DbHistory(level, ownStrength, dbHelper.getMaxLevel(), dbFight.getName(), dbFight.getPlayerLevel(), dbFight.getStrength(), dbFight.getMaxLevel(), result);
            dbHelper.createHistory(dbHistory);

            // REMOVE FIGTH FROM FIGHT-DB UPDATE ACTIVITY
            fightList.remove(position);
            fightAdapter.notifyItemRemoved(position);
            fightAdapter.notifyDataSetChanged();
            dbHelper.deleteFight(fightId);*/
        }else {
            FightResultDialogFragment dailyResultFrag = FightResultDialogFragment.newInstance(dailyName, 0, 0, chLevel, dailyResult.toString(), dailyChance);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dailyResultFrag.show(ft, dailyName);
            daily = false;

            //TODO: hide - already fought that day
            /*chName.setVisibility(View.GONE);
            chLevel.setVisibility(View.GONE);
            chStrength.setVisibility(View.GONE);
            chFight.setVisibility(View.GONE);
            */
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    //TODO Fix: getting recalculated every time FightActivity opens
    public void dailyChallenge(/*DbProfile profile*/){
        //TODO: integrate DbProfile
        /*int ownArmyStrength = profile.getArmyStrength();
        int ownLevel = profile.getPlayerLevel();*/
        //TODO: Get some badass frightening name
        dailyName = "PEM Presentation";
        int[] challenge = GameMechanics.randomEncounter(/*ownLevel, ownArmyStrength*/ 2, 42);
        chLevel = /*challenge[0]*/ 2;
        chStrength = /*challenge[1]*/ 42;

        TextView TextChName = (TextView) view.findViewById(R.id.challenge_name);
        TextView TextChLevel = (TextView) view.findViewById(R.id.challenge_level);
        TextView TextChStrength = (TextView) view.findViewById(R.id.challenge_strength);
        Button ButtonChFight = (Button) view.findViewById(R.id.challenge_button);

        TextChName.setText(dailyName);
        TextChLevel.setText(dailyName + "'s Level: " + chLevel.toString());
        TextChStrength.setText(dailyName + "'s Strength: " + chStrength.toString());
        ButtonChFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FightResultDialogFragment dialog = FightResultDialogFragment.newInstance(dailyName, 0, 0, 0, "", 0.0);
                daily = true;
                dailyChance = GameMechanics.getFightResult(4/*ownArmyStrength*/, chStrength);
                dailyResult = GameMechanics.getFightResult(4, chStrength) > 0 ? true : false;
                System.out.println("Chance Daily: " + dailyChance + "; Result: " + dailyResult);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, dailyName);
            }
        });
    }
}
