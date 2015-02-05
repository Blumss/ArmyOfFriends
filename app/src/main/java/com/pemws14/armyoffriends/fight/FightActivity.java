package com.pemws14.armyoffriends.fight;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pemws14.armyoffriends.GameMechanics;
import com.pemws14.armyoffriends.R;
import com.pemws14.armyoffriends.database.DbAchievement;
import com.pemws14.armyoffriends.database.DbFight;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbHistory;
import com.pemws14.armyoffriends.database.DbProfile;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.database.ParseDb;
import com.pemws14.armyoffriends.drawer.BaseActivity;
import com.pemws14.armyoffriends.history.HistoryActivity;

import java.util.List;


public class FightActivity extends BaseActivity implements FightResultDialogFragment.NoticeDialogListener{
    private View view;
    private ViewGroup parent;

    private ParseDb parseDb;
    private DbHelper dbHelper;
    private DbHistory dbHistory;
    private DbProfile dbProfile;
    private int profileId;
    private long currentTime;

    private List<DbFight> fightList;
    private List<DbAchievement> achievementList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter fightAdapter;

    private TextView ownLevelText;
    private TextView armyStrengthText;

    private Integer level;
    private Integer limitedStrength;
    private Integer maxOwnStrength;
    private Integer possibleArmySize;
    private double[] fightResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //<-- IN EVERY ACTIVITY WITH DRAWER
        super.set();
        // catches Frame, where to insert actual ActivityView
        parent = (ViewGroup) findViewById(R.id.content_frame);
        //View of new activity
        view = LayoutInflater.from(this).inflate(R.layout.activity_fight, parent, false);
        parent.addView(view);
        //--> IN EVERY ACTIVITY WITH DRAWER

        //Set up View
        recyclerView = (RecyclerView)findViewById(R.id.fightListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //get fights
        dbHelper = DbHelper.getInstance(getApplicationContext());
        currentTime = dbHelper.getUnix();
        parseDb = new ParseDb();
        dbProfile = dbHelper.getProfile(parseDb.getUserID());

        //createDummies();

        fightList = dbHelper.getAllFights();
        fightList.remove(0);
        fightAdapter = new FightListAdapter(fightList, getApplicationContext(), getFragmentManager());
        recyclerView.setAdapter(fightAdapter);

        //get view for ownLevel and ownArmyStrength
        ownLevelText = (TextView) view.findViewById(R.id.fight_info_level);
        armyStrengthText = (TextView) view.findViewById(R.id.fight_info_strength);

        //update Profile
        List<DbSoldier> getSoldiers = dbHelper.getAllSoldiers();
        dbProfile.setArmyStrength(GameMechanics.getArmyStrength(getSoldiers));
        dbProfile.setPlayerLevel(GameMechanics.getPlayerLevelForEp(dbProfile.getEp()));
        dbHelper.updateProfile(dbProfile);

        generateTopBar();
        checkFights(currentTime);

        achievementList = dbHelper.getAllAchievements();
    }

    /*
    generates the top bar (Level and ArmyStrength display) in onCreate and after a won fight
     */
    public void generateTopBar(){
        level = dbProfile.getPlayerLevel();
        maxOwnStrength = dbProfile.getArmyStrength();

        possibleArmySize = GameMechanics.getMaxArmySize(level);
        limitedStrength = GameMechanics.getArmyStrength(dbHelper.getLimitedSoldiers(possibleArmySize));

        ownLevelText.setText(level.toString());
        armyStrengthText.setText(" " + limitedStrength.toString() + "/" + maxOwnStrength.toString());
    }

    /*
    Checks, if Fight was created more than a day ago and if so deletes it from DbFight
     */
    public void checkFights(long currentTime) {
        List<DbFight> fights = dbHelper.getAllFights();
        for (DbFight fight : fights) {
            //if not daily challenge
            if (fight.getId() != 1){
                if(fight.getCreated_at_Unix() < currentTime-86400) {
                    dbHelper.deleteFight(fight.getId());
                }
            }else{
                //if already fought or current daily should be calculated
                if(fight.getPlayerLevel()==0){
                    Log.i("FightActivity.checkFights","dailyChallenge has already been fought");
                    displayDailyChallenge(fight, false);

                    if (HistoryActivity.getDateDifference(fight.getCreated_at_Unix(),0) >= 1) {
                        Log.i("FightActivity.checkFights","old dailyChallenge getting replaced");
                        generateDailyChallenge(dbProfile);
                    }else{
                        Log.i("FightActivity.checkFights","dailyChallenge is ok");
                    }

                //if not or never fought (initial call)
                }else{
                    if(fight.getName().equals("Daily Challenge")){
                        Log.i("FightActivity.checkFights","dailyChallenge gets initially calculated");
                        generateDailyChallenge(dbProfile);
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
        DbFight fight1 = new DbFight("abc",BitmapFactory.decodeResource(getResources(), R.drawable.userpic_placeholder),1,1,3);
        DbFight fight2 = new DbFight("def",BitmapFactory.decodeResource(getResources(), R.drawable.userpic_placeholder),2,2,4);
        DbFight fight3 = new DbFight("ghi",BitmapFactory.decodeResource(getResources(), R.drawable.userpic_placeholder),3,3,5);
        DbFight fight4 = new DbFight("jkl",BitmapFactory.decodeResource(getResources(), R.drawable.userpic_placeholder),3,3,2);
        DbFight fight5 = new DbFight("mno",BitmapFactory.decodeResource(getResources(), R.drawable.userpic_placeholder),4,4,9);
        DbFight fight6 = new DbFight("pqr",BitmapFactory.decodeResource(getResources(), R.drawable.userpic_placeholder),5,5,7);
        DbFight fight7 = new DbFight("stu",BitmapFactory.decodeResource(getResources(), R.drawable.userpic_placeholder),6,6,1);
        DbFight fight8 = new DbFight("vwx",BitmapFactory.decodeResource(getResources(), R.drawable.userpic_placeholder),7,7,6);
        DbFight fight9 = new DbFight("yz0",BitmapFactory.decodeResource(getResources(), R.drawable.userpic_placeholder),8,8,0);

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
        fightResult = GameMechanics.getFightResult(limitedStrength, dbFight.getStrength());
        double random = fightResult[0];
        double chance = fightResult[1];

        Boolean result = random >= chance;
        FightResultDialogFragment resultFrag = FightResultDialogFragment.newInstance(dbFight.getName(), fightId, 0,dbFight.getStrength(), result.toString(), random, chance);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        resultFrag.show(ft, dbFight.getName());

        // Save result in DBs & update Activity
        updateDBs(dbFight, result, random, chance);
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
    update Databases after Fight (History and Profile)
     */
    public void updateDBs(DbFight dbFight, Boolean result, double random, double chance){
        //update History
        dbHistory = new DbHistory(level, limitedStrength, dbHelper.getMaxLevel(), dbFight.getName(), dbFight.getImg(), dbFight.getPlayerLevel(), dbFight.getStrength(), dbFight.getMaxLevel(), result);
        dbHelper.createHistory(dbHistory);

        //update Achievements & ProfileEPs
        //*set AchievementFirstFight
        achievementList.get(0).setAchieved(achievementList.get(0).getAchieved() + 1);
        achievementList.get(0).setFulfilled(dbHelper.checkAchievementState(achievementList.get(0), getApplicationContext(), parent));
        dbHelper.updateAchievement(achievementList.get(0));
        //*others
        //**if won
        if(result){
            //***Overall win Achievements update
            for (int i = 1; i<8; i++) {
                DbAchievement achievement = achievementList.get(i);
                achievement.setAchieved(achievement.getAchieved() + 1);
                achievement.setFulfilled(dbHelper.checkAchievementState(achievement, getApplicationContext(), parent));
                dbHelper.updateAchievement(achievement);
            }
            //***Close win Achievement
            if(chance-random<=0.1){
                DbAchievement achievement = achievementList.get(9);
                achievement.setAchieved(achievement.getAchieved()+1);
                achievement.setFulfilled(dbHelper.checkAchievementState(achievement, getApplicationContext(), parent));
                dbHelper.updateAchievement(achievement);
            }
            //***Daily Challenge Achievements update
            if(dbFight.getId()==1){
                DbAchievement achievement1 = achievementList.get(17);
                DbAchievement achievement2 = achievementList.get(18);
                achievement1.setAchieved(achievement1.getAchieved()+1);
                achievement2.setAchieved(achievement2.getAchieved()+1);
                achievement1.setFulfilled(dbHelper.checkAchievementState(achievement1, getApplicationContext(), parent));
                achievement2.setFulfilled(dbHelper.checkAchievementState(achievement2, getApplicationContext(), parent));
                dbHelper.updateAchievement(achievement1);
                dbHelper.updateAchievement(achievement2);
            }
            //***update EPs, Level aaaaand EP-Achievement aaaaaaaaaaaaand generate the TopBar new (maybe upgrade in ArmyStrength)
            dbProfile.setEp(dbProfile.getEp() + (int)(GameMechanics.getEpBaseReward(dbFight.getPlayerLevel())*chance));
            dbProfile.setEp(dbProfile.getEp() + (int)Math.max((GameMechanics.getEpBaseReward(dbFight.getPlayerLevel())*chance),1));
            dbProfile.setPlayerLevel(GameMechanics.getPlayerLevelForEp(dbProfile.getEp()));
            dbHelper.updateProfile(dbProfile);

            DbAchievement achievement = achievementList.get(23);
            achievement.setAchieved(dbProfile.getPlayerLevel());
            achievement.setFulfilled(dbHelper.checkAchievementState(achievement, getApplicationContext(), parent));
            dbHelper.updateAchievement(achievement);

            generateTopBar();
        //**if lost
        }else{
            //***Loose achievement update
            DbAchievement achievement1 = achievementList.get(8);
            achievement1.setAchieved(achievement1.getAchieved()+1);
            achievement1.setFulfilled(dbHelper.checkAchievementState(achievement1, getApplicationContext(), parent));
            dbHelper.updateAchievement(achievement1);
            //***lost closely
            if(chance-random<=0.1){
                DbAchievement achievement2 = achievementList.get(10);
                achievement2.setAchieved(achievement2.getAchieved()+1);
                achievement2.setFulfilled(dbHelper.checkAchievementState(achievement2, getApplicationContext(), parent));
                dbHelper.updateAchievement(achievement2);
            }
        }
    }

    /*
    generates a new Daily Challenge
     */
    public void generateDailyChallenge(DbProfile profile){
        final DbFight daily = dbHelper.getAllFights().get(0);
        Resources res = getResources();

        //generate new & update DbEntry
        String [] enemies = res.getStringArray(R.array.daily_challenge_enemies);
        int enemy = (int) (Math.random()*enemies.length);
        int[] challenge = GameMechanics.randomEncounter(profile.getPlayerLevel()+1, profile.getArmyStrength()+1);
        daily.setName(enemies[enemy]);
        daily.setPlayerLevel(challenge[0]);
        daily.setStrength(challenge[1]);
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
