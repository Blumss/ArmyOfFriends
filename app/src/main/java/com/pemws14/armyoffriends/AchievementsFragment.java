package com.pemws14.armyoffriends;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pemws14.armyoffriends.database.DbAchievement;
import com.pemws14.armyoffriends.database.DbHelper;

import java.util.List;

/**
 * Created by Martin on 24.01.2015.
 */
public class AchievementsFragment extends Fragment {
    private View view;
    private AchievementListAdapter acAdapter;
    private DbHelper db;
    private List<DbAchievement> achievements;

    public static AchievementsFragment newInstance(String param1, String param2) {
        AchievementsFragment fragment = new AchievementsFragment();
        return fragment;
    }
    public AchievementsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.achievement_fragment, container, false);
        ListView listView = (ListView)view.findViewById(R.id.achievements_list);
        Context context = getActivity().getApplicationContext();
        setupDB();
        acAdapter = new AchievementListAdapter(context, achievements);

        listView.setAdapter(acAdapter);
        return view;
    }

    private void setupDB() {
        db = DbHelper.getInstance(this.getActivity());
        achievements = db.getAllAchievements();
    }
}
