package com.pemws14.armyoffriends;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Martin on 06.01.2015.
 */
public class FightResultDialogFragment extends DialogFragment{
    String enemy;
    int enemyStrength;
    int fightId;
    int position;
    String toggle;
    NoticeDialogListener mListener;

    static FightResultDialogFragment newInstance(String name, int id, int pos, String toggle) {
        FightResultDialogFragment f = new FightResultDialogFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putInt("id", id);
        args.putInt("pos", pos);
        args.putString("toggle", toggle);
        f.setArguments(args);

        return f;
    }

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, int id, int position) throws InterruptedException;
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        enemy = getArguments().getString("name");
        enemyStrength = getArguments().getInt("strength");
        fightId = getArguments().getInt("id");
        position = getArguments().getInt("pos");
        toggle = getArguments().getString("toggle");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.OurAlertDialog);

        if (!toggle.isEmpty()) {
            if(toggle.equals("true"))
                builder.setMessage("Congrats, you won against " + enemy + "!")
                        .setTitle("Victory!")
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mListener.onDialogNegativeClick(FightResultDialogFragment.this);
                            }
                        });
            else if(toggle.equals("false"))
                builder.setMessage("You lost against " + enemy + "...")
                        .setTitle("Loss!")
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mListener.onDialogNegativeClick(FightResultDialogFragment.this);
                            }
                        });
        } else {
            builder.setMessage("Do you really want to fight " + enemy + " ?")
                    .setPositiveButton("Fight!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                mListener.onDialogPositiveClick(FightResultDialogFragment.this, fightId, position);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mListener.onDialogNegativeClick(FightResultDialogFragment.this);
                        }
                    });
        }
        return builder.create();
    }
}
