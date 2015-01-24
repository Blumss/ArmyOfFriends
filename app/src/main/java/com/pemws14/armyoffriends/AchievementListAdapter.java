package com.pemws14.armyoffriends;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pemws14.armyoffriends.database.DbAchievement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Martin on 24.01.2015.
 */
public class AchievementListAdapter extends ArrayAdapter{
    List<DbAchievement> achievements;
    private LayoutInflater layoutInflater;
    private Context context;

    public AchievementListAdapter(Context context, List<DbAchievement> achievements){
        super(context,0);
        this.achievements = achievements;
        this.context = context;

        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup){
        view = layoutInflater.inflate(R.layout.achievement, null);
        ImageView image = (ImageView)view.findViewById(R.id.achivement_pic);
        TextView title = (TextView)view.findViewById(R.id.achievement_title);
        TextView description = (TextView)view.findViewById(R.id.achievement_description);

        title.setText(achievements.get(pos).getTitle());
        description.setText(achievements.get(pos).getDescription());

        if(achievements.get(pos).getFulfilled()==1){
            title.setTextColor(Color.parseColor("#000000"));
            description.setTextColor(Color.parseColor("#000000"));
            image.setImageResource(R.drawable.achievement_check);
        }
        return view;
    }

    @Override
    public int getCount(){
        return achievements.size();
    }
}
