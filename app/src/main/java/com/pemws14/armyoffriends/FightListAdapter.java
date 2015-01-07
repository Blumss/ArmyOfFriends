package com.pemws14.armyoffriends;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pemws14.armyoffriends.database.DbFight;

import java.util.List;

/**
 * Created by Martin on 27.12.2014.
 */
public class FightListAdapter extends RecyclerView.Adapter<FightListAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<DbFight> mDataset;
    public Context mContext;
    public FragmentManager mFragmentManager;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView profilePic;
        public TextView name;
        public TextView strength;
        public TextView bestFighter;
        public Button fightButton;

        public ViewHolder(View v) {
            super(v);

            profilePic = (ImageView) v.findViewById(R.id.fight_list_picture);
            name = (TextView) v.findViewById(R.id.fight_list_name);
            strength = (TextView) v.findViewById(R.id.fight_list_strength);
            bestFighter = (TextView) v.findViewById(R.id.fight_list_bestFighter);
            fightButton = (Button) v.findViewById(R.id.fight_list_button);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public FightListAdapter(List<DbFight> myDataset, Context context, FragmentManager fragmentManager) {
        mDataset = myDataset;
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FightListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fight_list_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        String[] ranks = mContext.getResources().getStringArray(R.array.army_ranks);
        DbFight fight = mDataset.get(position);
        final int fightId = fight.getId();
        final String enemyName = fight.getName();
        int enemyStrength = fight.getStrength();
        String enemyBest = ranks[fight.getMaxLevel()];

        holder.name.setText(enemyName);
        holder.strength.setText("Army strength: " + enemyStrength);
        holder.bestFighter.setText("Best fighter: " + enemyBest);

        holder.fightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Show dailog to confirm or cancel fight-action
                FightResultDialogFragment dialog = FightResultDialogFragment.newInstance(enemyName, fightId, position, "");
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                dialog.show(ft, enemyName);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
