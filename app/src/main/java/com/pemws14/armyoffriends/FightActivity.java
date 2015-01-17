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
import java.util.Collections;
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

    private Integer level;
    private Integer ownStrength;
    private Integer possibleOwnStrength;
    private Integer armySize;
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
        checkFights(currentTime);
        //createDummies();

        fightList = dbHelper.getAllFights();
        fightList.remove(0);
        fightAdapter = new FightListAdapter(fightList, getApplicationContext(), getFragmentManager());
        recyclerView.setAdapter(fightAdapter);

        //get view for ownLevel and ownArmyStrength
        TextView armyStrength = (TextView) view.findViewById(R.id.fight_info_strength);
        TextView ownLevel = (TextView) view.findViewById(R.id.fight_info_level);

        //TODO: get real userID (only one profile is saved on each device? so 0 might be ok?)
        /*
        int userId = 0;
        level = GameMechanics.getPlayerLevelForEp(dbProfile.getPlayerLevel());
        armySize = GameMechanics.getMaxArmySize(level);
        List<DbSoldier> getSoldiers = dbHelper.getAllSoldiers();
        possibleOwnStrength = GameMechanics.getArmyStrength(getSoldiers);
        ownStrength = GameMechanics.getArmyStrength(dbHelper.getLimitedSoldiers(GameMechanics.getMaxArmySize(level)));
        armyStrength.setText(ownStrength.toString() + "/" + actualOwnStrength.toString());
        ownLevel.setText(level.toString());
        */


        //TODO: remove the part below when proper userID is received
        level = GameMechanics.getPlayerLevelForEp(1000);
        armySize = GameMechanics.getMaxArmySize(level);
        List<DbSoldier> getSoldiers = dbHelper.getAllSoldiers();
        possibleOwnStrength = GameMechanics.getArmyStrength(getSoldiers);
        ownStrength = GameMechanics.getArmyStrength(dbHelper.getLimitedSoldiers(GameMechanics.getMaxArmySize(level)));
        armyStrength.setText(ownStrength.toString() + "/" + possibleOwnStrength.toString());
        ownLevel.setText(level.toString());
        //TODO: remove the part above
    }


    /*
    Checks, if Fight was created more than a day ago and if so deletes it from DbFight
     */
    public void checkFights(long currentTime) {
        List<DbFight> fights = dbHelper.getAllFights();
        for (DbFight fight : fights) {
            //if not daily challenge
            if (fight.getMaxLevel() != 10){
                if(fight.getCreated_at_Unix() < currentTime-86400) {
                    dbHelper.deleteFight(fight.getId());
                }
            }else{
                //if already fought or current daily should be calculated
                if(fight.getPlayerLevel()==0){
                    Log.i("FightActivity.checkFights","dailyChallenge has already been fought");
                    displayDailyChallenge(fight, false);
                    if(fight.getCreated_at_Unix() < currentTime-86400){
                        Log.i("FightActivity.checkFights","old dailyChallenge getting replaced");
                        generateDailyChallenge(/*DbProfile profile*/);
                    }
                //if not or never fought (initial call)
                }else{
                    if(fight.getStrength() == 0){
                        Log.i("FightActivity.checkFights","dailyChallenge gets initially calculated");
                        generateDailyChallenge(/*DbProfile profile*/);
                    }else{
                        Log.i("FightActivity.checkFights","dailyChallenge has not been fought today");
                        displayDailyChallenge(fight, true);
                    }
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

        //TODO enter ownArmyStrength (4 was for testing)
        fightResult = GameMechanics.getFightResult(4/*ownArmyStrength*/, dbFight.getStrength());
        double random = fightResult[0];
        double chance = fightResult[1];

        Boolean result = random >= chance;
        FightResultDialogFragment resultFrag = FightResultDialogFragment.newInstance(dbFight.getName(), fightId, 0,dbFight.getStrength(), result.toString(), random, chance);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        resultFrag.show(ft, dbFight.getName());

        // Save result in DbHistory & update Activity
        dbHistory = new DbHistory(level, ownStrength, dbHelper.getMaxLevel(), dbFight.getName(), dbFight.getPlayerLevel(), dbFight.getStrength(), dbFight.getMaxLevel(), result);
        dbHelper.createHistory(dbHistory);
        updateView(fightId, position);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    /*
    remove Fight-entry if fought from DbFight and Activity, update Activity but show info for dailyChallenge
    */
    public void updateView(int fightId, int position){
        DbFight dbFight = dbHelper.getFight(fightId);
        if (fightId != 1){
            Log.i("FightActivity.updateView","deleting fought Fight from DB and List");
            fightList.remove(position);
            fightAdapter.notifyItemRemoved(position);
            fightAdapter.notifyDataSetChanged();
            dbHelper.deleteFight(fightId);
        }else{
            Log.i("FightActivity.updateView","hiding DailyChallenge-Stuff");
            displayDailyChallenge(dbFight, false);
            dbFight.setPlayerLevel(0);
            dbHelper.updateFight(dbFight);
        }
    }

    /*
    generates a new Daily Challenge
     */
    public void generateDailyChallenge(/*DbProfile profile*/){
        final DbFight daily = dbHelper.getAllFights().get(0);
        //TODO: integrate DbProfile
        //int ownLevel = profile.getPlayerLevel();

        //generate new & update DbEntry
        int[] challenge = GameMechanics.randomEncounter(/*ownLevel, ownStrength*/ 2, 42);
        //TODO Get some badass frightening name
        daily.setName("PEM Presentation");
        daily.setPlayerLevel(challenge[0]);
        daily.setStrength(challenge[1]);
        daily.setMaxLevel(10);
        daily.setId(1);
        daily.setCreated_at(dbHelper.getDateTime());
        daily.setCreated_at_Unix(dbHelper.getUnix());
        dbHelper.updateFight(daily);

        displayDailyChallenge(daily, true);
    }

    /*
    cares about displaying the daily challenge
     */
    public void displayDailyChallenge(final DbFight daily, boolean display){
        TextView chName = (TextView) view.findViewById(R.id.challenge_name);
        TextView chLevel = (TextView) view.findViewById(R.id.challenge_level);
        TextView chStrength = (TextView) view.findViewById(R.id.challenge_strength);
        Button chFight = (Button) view.findViewById(R.id.challenge_button);

        chName.setVisibility(View.VISIBLE);
        chStrength.setVisibility(View.VISIBLE);
        chFight.setVisibility(View.VISIBLE);

        if(display){
            chName.setText(daily.getName());
            chLevel.setText(daily.getName() + "'s Level: " + Integer.toString(daily.getPlayerLevel()));
            chStrength.setText(daily.getName() + "'s Strength: " + Integer.toString(daily.getStrength()));
            chFight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FightResultDialogFragment dialog = FightResultDialogFragment.newInstance(daily.getName(), daily.getId(), 0, 0, "",0.0, 0.0);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, daily.getName());
                }
            });
        }else{
            chName.setVisibility(View.GONE);
            chLevel.setText("You already fought the Daily Challenge!");
            chStrength.setVisibility(View.GONE);
            chFight.setVisibility(View.GONE);
        }
    }
}
