package com.pemws14.armyoffriends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pemws14.armyoffriends.database.DbHelper;
import com.pemws14.armyoffriends.database.DbProfile;
import com.pemws14.armyoffriends.database.DbSoldier;
import com.pemws14.armyoffriends.database.ParseDb;
import com.pemws14.armyoffriends.drawer.BaseActivity;

import java.util.List;

public class ProfileActivity extends BaseActivity {
    private View view;
    private ImageView profileUserImage;
    private TextView profileUsername;
    private TextView profileCurrentLevel;
    private ProgressBar profileEpBar;
    private TextView profileNextLevel;
    private TextView profileActualArmyStrength;
    private TextView profileMaxArmyStrength;
    private TextView profileActiveSoldiers;
    private TextView profileTotalSoldiers;
    private TextView profileEpToNextLevel;

    private DbHelper db;
    private DbProfile profile;
    private ParseDb parseDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //<-- IN EVERY ACTIVITY WITH DRAWER
        super.set();
        // catches Frame, where to insert actual ActivityView
        ViewGroup parent = (ViewGroup) findViewById(R.id.content_frame);
        //View of new activity
        view = LayoutInflater.from(this).inflate(R.layout.activity_profile, parent, false);
        parent.addView(view);
        //--> IN EVERY ACTIVITY WITH DRAWER
        setupDB();


        // TODO connect userImg to DB
        profileUserImage = (ImageView) findViewById(R.id.profile_user_image);
        profileUserImage.setImageResource(R.drawable.profile_placeholder);

        profileUsername = (TextView) findViewById(R.id.profile_username);
        profileUsername.setText(profile.getUserName());

        profileCurrentLevel = (TextView) findViewById(R.id.profile_current_level);
        Integer level = new Integer(GameMechanics.getPlayerLevelForEp(profile.getEp()));
        profileCurrentLevel.setText(level.toString());
        profileNextLevel = (TextView) findViewById(R.id.profile_next_level);

        level = level+1;
        profileNextLevel.setText(level.toString());
        profileEpBar = (ProgressBar) findViewById(R.id.profile_ep_bar);
        int progress = (int)(100*GameMechanics.getPlayerLevelProgress(profile.getEp()));
        profileEpBar.setProgress(progress);

        profileEpToNextLevel = (TextView) findViewById(R.id.profile_ep_next_level);
        profileEpToNextLevel.setText(""+profile.getEp()+"/"+GameMechanics.getEpForPlayerLevelUp(profile.getPlayerLevel()));

        profileActualArmyStrength = (TextView) findViewById(R.id.profile_actual_army_strength);
        List<DbSoldier> limitedSoldiers = db.getLimitedSoldiers(GameMechanics.getMaxArmySize(level));
        Integer ownStrength = GameMechanics.getArmyStrength(limitedSoldiers);
        profileActualArmyStrength.setText(ownStrength.toString());

        profileMaxArmyStrength = (TextView) findViewById(R.id.profile_max_army_strength);
        Integer maxStrength = GameMechanics.getArmyStrength(db.getAllSoldiers());
        profileMaxArmyStrength.setText(maxStrength.toString());

        profileActiveSoldiers = (TextView) findViewById(R.id.profile_active_soldiers_number);
        profileActiveSoldiers.setText(((Integer)limitedSoldiers.size()).toString());

        profileTotalSoldiers = (TextView) findViewById(R.id.profile_total_soldiers_number);
        profileTotalSoldiers.setText(((Integer)db.getAllSoldiers().size()).toString());



    }

    private void setupDB() {
        parseDb = new ParseDb();
        db = new DbHelper(getApplicationContext());
        profile = db.getProfile(parseDb.getUserID());
    }
}
