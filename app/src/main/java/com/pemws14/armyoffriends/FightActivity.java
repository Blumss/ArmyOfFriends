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
    private long currentTime;

    private List<DbFight> fightList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter fightAdapter;

    private Boolean dailyBool;
    private Integer level;
    private Integer ownStrength;
    private double[] fightResult;

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
        currentTime = dbHelper.getUnix();
        // TODO dbProfile = dbHelper.getProfile(0);
        dailyBool = false;                      // Bool to toggle dailyChallenge (different onDialogPositiveClick-Handling)
        checkFights(currentTime);
        //createDummies();
        //checkFights();

        fightList = dbHelper.getAllFights();
        fightList.remove(0);
        fightAdapter = new FightListAdapter(fightList, getApplicationContext(), getFragmentManager());
        recyclerView.setAdapter(fightAdapter);

        //get view for ownLevel and ownArmyStrength
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


    /*
    Checks, if Fight was created more than a day ago and if so deletes it from DbFight
     */
    public void checkFights(long currentTime) {
        DbHelper db = new DbHelper(getApplicationContext());
        List<DbFight> fights = db.getAllFights();
        for (DbFight fight : fights) {
            System.out.println("FightActivity.checkFights: Checking " + fight.getName());
            //if not daily challenge
            if (fight.getMaxLevel() != 10){
                if(fight.getCreated_at_Unix() < currentTime-86400) {
                    db.deleteFight(fight.getId());
                }
            }else if (fight.getMaxLevel() == 10){
                System.out.println("FightActivity.checkFights: Daily Challenge! " + fight.getName() + " - not deleting!");
                if(fight.getCreated_at_Unix() < currentTime-86400 ||  fight.getStrength() == 0){
                    System.out.println("FightActivity.checkFights: Old Daily Challenge! " + fight.getName() + " - generating new! Daily was from " + fight.getCreated_at_Unix() + " current is " + currentTime);
                    dailyChallenge(/*DbProfile profile*/);
                }
            }
        }
    }

    /*
    creates Dummy-Fights
     */
    private void createDummies(){
        /**********DUMMIES******************/
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
        /**********DUMMIES******************/
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int fightId, int position) throws InterruptedException {
        dialog.dismiss();
        DbFight dbFight = dbHelper.getFight(fightId);

        fightResult = GameMechanics.getFightResult(4/*ownArmyStrength*/, dbFight.getStrength());
        double random = fightResult[0];
        double chance = fightResult[1];

        //if (!daily){
            // CALCULATE & SHOW FIGHT
            Boolean result = random >= chance;
            FightResultDialogFragment resultFrag = FightResultDialogFragment.newInstance(dbFight.getName(), fightId, 0,dbFight.getStrength(), result.toString(), chance);
            System.out.println("Random/Chance: " + random + "/" + chance + " - Result: " + result.toString());
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
            dbHelper.deleteFight(fightId);
            */
        /*}else {
            FightResultDialogFragment dailyResultFrag = FightResultDialogFragment.newInstance(dailyName, 0, 0, chLevel, dailyResult.toString(), dailyChance);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dailyResultFrag.show(ft, dailyName);
            daily = false;*/

            //TODO: hide - already fought that day
            /*chName.setVisibility(View.GONE);
            chLevel.setVisibility(View.GONE);
            chStrength.setVisibility(View.GONE);
            chFight.setVisibility(View.GONE);
            */
        //}
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    /*
    generates a new Daily Challenge
     */
    public void dailyChallenge(/*DbProfile profile*/){
        System.out.println("FightActivity.dailyChallenge: Generating new Daily Challenge");
        System.out.println("FightActivity.dailyChallenge: dbHelper.getAllFightsList: " + dbHelper.getAllFights());
        final DbFight daily = dbHelper.getAllFights().get(0);
        //TODO: integrate DbProfile
        /*int ownArmyStrength = profile.getArmyStrength();
        int ownLevel = profile.getPlayerLevel();*/

        //generate new & update DbEntry
        int[] challenge = GameMechanics.randomEncounter(/*ownLevel, ownArmyStrength*/ 2, 42);
        //TODO Get some badass frightening name
        daily.setName("PEM Presentation");
        daily.setPlayerLevel(challenge[0]);
        daily.setStrength(challenge[1]);
        daily.setMaxLevel(0);
        daily.setId(0);
        daily.setCreated_at(dbHelper.getDateTime());
        daily.setCreated_at_Unix(dbHelper.getUnix());
        dbHelper.updateFight(daily);

        TextView TextChName = (TextView) view.findViewById(R.id.challenge_name);
        TextView TextChLevel = (TextView) view.findViewById(R.id.challenge_level);
        TextView TextChStrength = (TextView) view.findViewById(R.id.challenge_strength);
        Button ButtonChFight = (Button) view.findViewById(R.id.challenge_button);

        TextChName.setText(daily.getName());
        TextChLevel.setText(daily.getName() + "'s Level: " + Integer.toString(daily.getPlayerLevel()));
        TextChStrength.setText(daily.getName() + "'s Strength: " + Integer.toString(daily.getStrength()));
        ButtonChFight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FightResultDialogFragment dialog = FightResultDialogFragment.newInstance(daily.getName(), 0, 0, 0, "", 0.0);
                dailyBool = true;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, daily.getName());
            }
        });
    }
}
