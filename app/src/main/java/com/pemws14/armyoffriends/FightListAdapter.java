package com.pemws14.armyoffriends;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Martin on 27.12.2014.
 */
public class FightListAdapter extends ArrayAdapter {
    private final Context context;
    private LayoutInflater layoutInflater;
    private final List<String[]> list;

    public FightListAdapter(Context context, List<String[]> list){
        super(context,0);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.fight_list_layout, null);

        ImageView profilePic = (ImageView) view.findViewById(R.id.fight_list_picture);
        TextView name = (TextView) view.findViewById(R.id.fight_list_name);
        TextView strength = (TextView) view.findViewById(R.id.fight_list_strength);
        TextView bestFighter = (TextView) view.findViewById(R.id.fight_list_bestFighter);
        Button fight = (Button) view.findViewById(R.id.fight_list_button);

        final String fight_id = list.get(position)[0];
        String fight_name = list.get(position)[1];
        String fight_strength = list.get(position)[2];
        String fight_best = list.get(position)[3];
        String fight_created = list.get(position)[4];

        name.setText(fight_name);
        strength.setText("Army strength: " + fight_strength);
        bestFighter.setText("Best fighter: " + fight_best);

        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You want to fight " + fight_id,Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
