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

import com.pemws14.armyoffriends.database.DbFight;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbHistory;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.drawer.BaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FightActivity extends BaseActivity implements FightResultDialogFragment.NoticeDialogListener{
    private View view;

    private DbHelper dbHelper;
    private DbHistory dbHistory;

    private List<DbFight> fightList;
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

        //Set up View
        recyclerView = (RecyclerView)findViewById(R.id.fightListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //get fights
        dbHelper = new DbHelper(getApplicationContext());
        //TODO create DailyChallenge & normal Fights
        dailyChallenge(/*DbProfile profile*/);
        createFights();
        fightList = dbHelper.getAllFights();
        fightAdapter = new FightListAdapter(fightList, getApplicationContext(), getFragmentManager());
        recyclerView.setAdapter(fightAdapter);

        //get and set own level and army strength
        TextView armyStrength = (TextView) view.findViewById(R.id.fight_info_strength);
        TextView ownLevel = (TextView) view.findViewById(R.id.fight_info_level);

        //TODO: get real userID (only one profile is saved on each device? so 0 might be ok?)
        /*
        int userId = 0;
        ownStrength = new Integer(dbHelper.getProfile(userId).getArmyStrength()).toString();
        armyStrength.setText(ownStrength);
        level = new Integer(dbHelper.getProfile(userId).getPlayerLevel()).toString();
        ownLevel.setText(level);
        */

        //TODO: replace Ep-value with real value
        //TODO: remove the part below when proper userID is received
        List<DbSoldier> getSoldiers = dbHelper.getAllSoldiers();
        ownStrength = GameMechanics.getArmyStrength(getSoldiers);
        armyStrength.setText(ownStrength.toString());
        level = GameMechanics.getPlayerLevelForEp(1000);
        ownLevel.setText(level.toString());
        //TODO: remove the part above
    }

    private void createFights() {
        //TODO add Soldier's maxLevel and ArmyStrength
        /*List<DbSoldier> soldiers = dbHelper.getAllSoldiers();
        long currentUnix = System.currentTimeMillis()/1000L;
        for (DbSoldier soldier: soldiers){
            if(soldier.getCreated_at_Unix()-84400<=currentUnix){
                DbFight fight = new DbFight(soldier.getName(), soldier.getLevel(),)
            }
        }*/

        /**********DUMMIES*******************
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
        ***********DUMMIES******************/
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
        fightList.remove(position);
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

    public void dailyChallenge(/*DbProfile profile*/){
        //TODO: integrate DbProfile
        /*int armyStrength = profile.getArmyStrength();
        int maxLevel = profile.getMaxSoldierLevel();
        int level = profile.getPlayerLevel();*/
        //TODO: Calculate Challenge's strength, level and some badass frightening name
        String challengeName = "PEM Presentation";
        Integer challengeLevel = 2;
        Integer challengeStrength = 42;

        TextView chName = (TextView) view.findViewById(R.id.challenge_name);
        TextView chLevel = (TextView) view.findViewById(R.id.challenge_level);
        TextView chStrength = (TextView) view.findViewById(R.id.challenge_strength);
        Button chFight = (Button) view.findViewById(R.id.challenge_button);

        chName.setText(challengeName);
        chLevel.setText(challengeName + "'s Level: " + challengeLevel.toString());
        chStrength.setText(challengeName + "'s Strength: " + challengeStrength.toString());

        //TODO: hide when already fought that day
        /*chName.setVisibility(View.GONE);
        chFight.setVisibility(View.GONE);
        */
    }
}
