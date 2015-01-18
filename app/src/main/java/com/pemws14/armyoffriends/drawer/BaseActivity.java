package com.pemws14.armyoffriends.drawer;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pemws14.armyoffriends.FightActivity;
import com.pemws14.armyoffriends.GameMechanics;
import com.pemws14.armyoffriends.ImpressumActivity;
import com.pemws14.armyoffriends.HistoryActivity;
import com.pemws14.armyoffriends.R;
import com.pemws14.armyoffriends.YourArmyActivity;
import com.pemws14.armyoffriends.YourProfileActivity;
import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbProfile;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.database.ParseDb;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends ActionBarActivity {
    private TextView profileUsername;
    private TextView profileLevel;
    private TextView profileArmyStrength;
    private ProgressBar profileEpBar;
    private TextView profileEpNumber;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerLeft;
    private ListView mDrawerList;
    private RelativeLayout mDrawerFooter;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    //nav drawer title
    private CharSequence mDrawerTitle;
    //used to store app title
    private CharSequence mTitle;

    private DbHelper db;
    private DbProfile profile;
    private ParseDb parseDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        parseDb = new ParseDb();
        db = new DbHelper(getApplicationContext());
        // TODO: getProfile & userId
//         profile = db.getProfile(parseDb.getUserID());
        System.out.println("Das Profil: "+profile);
    }

    //public void set(String[] navMenuTitles, TypedArray navMenuIcons) {
    public void set() {
        //--------------
        //set profile data
        //--------------
        profileUsername = (TextView) findViewById(R.id.left_drawer_user_profile_username);
        profileUsername.setText("Schnabeltier");
        //TODO an profil anbinden
      //  profileUsername.setText(profile.getUserName());

        profileLevel = (TextView) findViewById(R.id.left_drawer_user_profile_lvl_number);
        int ep = 1000;
        Integer level = new Integer(GameMechanics.getPlayerLevelForEp(ep));
        profileLevel.setText(level.toString());
        //TODO an profil anbinden
      //  profileLevel.setText(profile.getPlayerLevel());

        profileArmyStrength = (TextView) findViewById(R.id.left_drawer_user_profile_armystrength_number);
        List<DbSoldier> limitedSoldiers = db.getLimitedSoldiers(GameMechanics.getMaxArmySize(level));
        Integer ownStrength = GameMechanics.getArmyStrength(limitedSoldiers);
        profileArmyStrength.setText(ownStrength.toString());
        //TODO an profil anbinden
        //profileArmyStrength.setText(profile.getArmyStrength());

        profileEpBar = (ProgressBar) findViewById(R.id.left_drawer_user_profile_ep_bar);
        int progress = (int)(100*GameMechanics.getPlayerLevelProgress(ep));
        profileEpBar.setProgress(progress);
        //TODO get real ep from DB
        //profileEpBar.setProgress(profile.getEp());

//        profileEpNumber = (TextView) findViewById(R.id.left_drawer_user_profile_ep_number);
//        profileEpNumber.setText(" "+ ep);



        //-------------
        //set menu
        //-------------
        navMenuTitles = getResources().getStringArray(R.array.left_drawer_list_items);
        //load icons from strings.xml
        navMenuIcons = getResources().obtainTypedArray(R.array.left_drawer_list_icons);


        mTitle = mDrawerTitle = getTitle();
        //listActivityTitles = getResources().getStringArray(R.array.left_drawer_list_items);
        // R.id.drawer_layout should be in every activity with exactly the same id.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLeft = (RelativeLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_activities_list);
        mDrawerFooter = (RelativeLayout) findViewById(R.id.left_drawer_footer);
        navDrawerItems = new ArrayList<NavDrawerItem>();

        //adding nav drawer items
        if (navMenuIcons == null) {
            for (String navMenuTitle : navMenuTitles) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitle));
            }
        } else {
            for (int i = 0; i < navMenuTitles.length; i++) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
            }
        }

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = (String) getSupportActionBar().getTitle();
                if (!title.equals("Impressum")) {
                    Intent intent = new Intent(getApplicationContext(), ImpressumActivity.class);
                    mDrawerLayout.closeDrawer(mDrawerLeft);
                    startActivity(intent);
                    finish();
                }
                mDrawerLayout.closeDrawer(mDrawerLeft);
            }
        });

        //enabling action bar app icon and behaving it as toggle button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, 0, 0) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        //mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_item_layout, listActivityTitles));
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            if (mDrawerLayout.isDrawerOpen(mDrawerLeft)) {
                mDrawerLayout.closeDrawer(mDrawerLeft);
            } else {
                mDrawerLayout.openDrawer(mDrawerLeft);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
// if nav drawer is opened, hide the action items
// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
// menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
// update the main content by replacing fragments
        String title = (String) getSupportActionBar().getTitle();
        switch (position) {
            case 0:
                if (!title.equals("Your Army")) {
                    Intent intent = new Intent(this, YourArmyActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case 1:
                if (!title.equals("Fight")) {
                    Intent intent1 = new Intent(this, FightActivity.class);
                    startActivity(intent1);
                    finish();
                }
                break;
            case 2:
                if (!title.equals("History")) {
                    Intent intent2 = new Intent(this, HistoryActivity.class);
                    startActivity(intent2);
                    finish();
                }
                break;
            case 3:
                if (!title.equals("Your Profile")) {
                    Intent intent3 = new Intent(this, YourProfileActivity.class);
                    startActivity(intent3);
                    finish();
                }
                break;
            default:
                break;
        }

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        mDrawerLayout.closeDrawer(mDrawerLeft);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
