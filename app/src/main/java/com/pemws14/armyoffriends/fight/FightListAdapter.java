package com.pemws14.armyoffriends.fight;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pemws14.armyoffriends.GameMechanics;
import com.pemws14.armyoffriends.R;
import com.pemws14.armyoffriends.database.DbFight;

import java.util.List;


public class FightListAdapter extends RecyclerView.Adapter<FightListAdapter.ViewHolder> {
    private List<DbFight> mDataset;
    public Context mContext;
    public FragmentManager mFragmentManager;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profilePic;
        public TextView name;
        public TextView level;
        public TextView strength;
        public TextView bestFighter;
        public Button fightButton;

        public ViewHolder(View v) {
            super(v);

            profilePic = (ImageView) v.findViewById(R.id.fight_list_picture);
            name = (TextView) v.findViewById(R.id.fight_list_name);
            level = (TextView) v.findViewById(R.id.fight_list_level);
            strength = (TextView) v.findViewById(R.id.fight_list_strength);
            bestFighter = (TextView) v.findViewById(R.id.fight_list_bestFighter);
            fightButton = (Button) v.findViewById(R.id.fight_list_button);
        }
    }

    public FightListAdapter(List<DbFight> myDataset, Context context, FragmentManager fragmentManager) {
        mDataset = myDataset;
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    @Override
    public FightListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fight_list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String[] ranks = mContext.getResources().getStringArray(R.array.army_ranks);
        final DbFight fight = mDataset.get(position);

        holder.profilePic.setImageBitmap(fight.getImg());
        holder.name.setText(fight.getName());
        holder.strength.setText("Army strength: " + fight.getStrength());
        holder.level.setText("Level: " + fight.getPlayerLevel());
        //Log.d("onBindViewHolder", "ranks.length: "+ ranks.length + " fight.getMaxLevel: "+ fight.getMaxLevel());
        holder.bestFighter.setText("Best fighter: " + ranks[GameMechanics.getRankByLevel(fight.getMaxLevel())]);
        holder.fightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Show dailog to confirm or cancel fight-action
                FightResultDialogFragment dialog = FightResultDialogFragment.newInstance(fight.getName(), fight.getId(), position, 0, "", 0.0, 0.0);
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                dialog.show(ft, fight.getName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
