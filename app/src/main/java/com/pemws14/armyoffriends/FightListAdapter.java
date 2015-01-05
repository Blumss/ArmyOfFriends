package com.pemws14.armyoffriends;

import android.app.Activity;
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

import java.util.List;

/**
 * Created by Martin on 27.12.2014.
 */
public class FightListAdapter extends RecyclerView.Adapter<FightListAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<String[]> mDataset;
    public Context mContext;

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
    public FightListAdapter(List<String[]> myDataset, Context context) {
        mDataset = myDataset;
        mContext = context;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String fight_id = mDataset.get(position)[0];
        String fight_name =  mDataset.get(position)[1];
        String fight_strength =  mDataset.get(position)[2];
        String fight_best =  mDataset.get(position)[3];
        String fight_created =  mDataset.get(position)[4];

        holder.name.setText(fight_name);
        holder.strength.setText("Army strength: " + fight_strength);
        holder.bestFighter.setText("Best fighter: " + fight_best);

        holder.fightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"You want to fight " + fight_id, Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}