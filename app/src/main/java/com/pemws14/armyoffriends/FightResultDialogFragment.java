package com.pemws14.armyoffriends;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pemws14.armyoffriends.database.DbHelper;

/**
 * Created by Martin on 06.01.2015.
 */
public class FightResultDialogFragment extends DialogFragment{
    String enemy;
    int enemyStrength;
    int fightId;
    int position;
    int enemyLevel;
    String toggle;
    double result;
    NoticeDialogListener mListener;

    static FightResultDialogFragment newInstance(String name, int id, int pos, int enemyLevel, String toggle, Double result) {
        FightResultDialogFragment f = new FightResultDialogFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putInt("id", id);
        args.putInt("pos", pos);
        args.putInt("enemyLevel", enemyLevel);
        args.putString("toggle", toggle);
        args.putDouble("result", result);
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
        enemyLevel = getArguments().getInt("enemyLevel");
        toggle = getArguments().getString("toggle");
        result = getArguments().getDouble("result");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.OurAlertDialog);
        final DbHelper dbHelper= new DbHelper(getActivity());

        if (!toggle.isEmpty()) {

            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.fighting_dialog, (ViewGroup)getActivity().findViewById(R.id.fighting_dialog_root));

            SeekBar seekBar = (SeekBar)layout.findViewById(R.id.fighting_dialog_seekbar);
            final TextView resultText = (TextView)layout.findViewById(R.id.fighting_dialog_result);
            final TextView additionalText1 = (TextView)layout.findViewById(R.id.fighting_dialog_additional1);
            final TextView additionalText2 = (TextView)layout.findViewById(R.id.fighting_dialog_additional2);
            final Button dismiss = (Button)layout.findViewById(R.id.fighting_dialog_button);

            seekBar.setEnabled(false);
            resultText.setVisibility(View.GONE);
            additionalText1.setVisibility(View.GONE);
            additionalText2.setVisibility(View.GONE);
            dismiss.setEnabled(false);

            dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onDialogNegativeClick(FightResultDialogFragment.this);
                }
            });

            builder.setView(layout)
                    .setTitle("Fight");
            ObjectAnimator anim;
            if(toggle.equals("true")) {
                anim = ObjectAnimator.ofInt(seekBar, "progress", 0, seekBar.getMax());
            }else{
                anim = ObjectAnimator.ofInt(seekBar, "progress", 0, 100-(int)result);
            }
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator anim) {
                    if(toggle.equals("true")) {
                        //TODO save EP's in Profile
                        //int wonEPs = GameMechanics.getEpBaseReward(enemyLevel);
                        //dbHelper.getProfile(0).setEp(wonEPs);
                        resultText.setVisibility(View.VISIBLE);
                        additionalText1.setVisibility(View.VISIBLE);
                        dismiss.setEnabled(true);
                        resultText.setText("Victory!");
                        additionalText1.setText("Congrats, you won against " + enemy + "!");
                    }else if(toggle.equals("false")){
                        resultText.setVisibility(View.VISIBLE);
                        additionalText1.setVisibility(View.VISIBLE);
                        additionalText2.setVisibility(View.VISIBLE);
                        dismiss.setEnabled(true);
                        resultText.setText("Loss!");
                        additionalText1.setText("You lost against " + enemy + "...");
                        if (result<=10){additionalText2.setText("Pretty close! You were just unlucky...");}
                        else{additionalText2.setText("You got destroyed! Improve your army to have a chance!");
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.setDuration(5000).start();


        } else if (toggle.isEmpty()) {
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
