package com.pemws14.armyoffriends;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pemws14.armyoffriends.database.DbFight;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbHistory;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.drawer.BaseActivity;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class FightActivity extends BaseActivity implements FightResultDialogFragment.NoticeDialogListener{
    private View view;

    private DbHelper dbHelper;
    private DbHistory dbHistory;

    private List<DbFight> fights;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter fightAdapter;

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

        //get data from Fight-DB
        dbHelper = new DbHelper(getApplicationContext());
        fights = dbHelper.getAllFights();
        //fights = buildDummyData();
        fightAdapter = new FightListAdapter(fights, getApplicationContext(), getFragmentManager());
        recyclerView.setAdapter(fightAdapter);

        //get and set own level and army strength
        TextView armyStrength = (TextView) view.findViewById(R.id.fight_info_strength);
        TextView ownLevel = (TextView) view.findViewById(R.id.fight_info_level);
        List<DbSoldier> getSoldiers = dbHelper.getAllSoldiers();
        ownStrength = GameMechanics.getArmyStrength(getSoldiers);
        armyStrength.setText(ownStrength.toString());
        // TODO: show own level (see commentary)
        // Integer level = GameMechanics.getLevel();
        // ownLevel.setText(level.toString());
    }

    private List<DbFight> buildDummyData() {
        List<DbFight> fights;

        DbFight fight1 = new DbFight("abc",1,3);
        DbFight fight2 = new DbFight("def",2,4);
        DbFight fight3 = new DbFight("ghi",3,5);
        DbFight fight4 = new DbFight("jkl",3,2);
        DbFight fight5 = new DbFight("mno",4,9);
        DbFight fight6 = new DbFight("pqr",5,7);
        DbFight fight7 = new DbFight("stu",6,1);
        DbFight fight8 = new DbFight("vwx",7,6);
        DbFight fight9 = new DbFight("yz0",8,0);

        dbHelper.createFight(fight1);
        dbHelper.createFight(fight2);
        dbHelper.createFight(fight3);
        dbHelper.createFight(fight4);
        dbHelper.createFight(fight5);
        dbHelper.createFight(fight6);
        dbHelper.createFight(fight7);
        dbHelper.createFight(fight8);
        dbHelper.createFight(fight9);

        fights = dbHelper.getAllFights();
        return fights;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, int fightId, int position) throws InterruptedException {
        dialog.dismiss();
        DbFight dbFight = dbHelper.getFight(fightId);

        // FIGHT INCL DELAY -
        // TODO: PROGRESS BAR OR DIE!
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = GameMechanics.getFightResult(ownStrength, dbFight.getStrength());

        // SAVE RESULT IN HISTORY-DB
        dbHistory = new DbHistory(result, dbFight.getName(), ownStrength, dbHelper.getMaxLevel(), dbFight.getStrength(), dbFight.getMaxLevel());
        dbHelper.createHistory(dbHistory);

        // REMOVE FIGTH FROM FIGHT-DB UPDATE ACTIVITY
        System.out.println(position);
        fights.remove(position);
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
