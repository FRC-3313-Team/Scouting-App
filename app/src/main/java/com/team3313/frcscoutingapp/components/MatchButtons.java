package com.team3313.frcscoutingapp.components;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.team3313.frcscoutingapp.DataStore;
import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.R;
import com.team3313.frcscoutingapp.fragments.ScoutingFragment;
import com.team3313.frcscoutingapp.fragments.ScoutingMatchFragment;
import com.team3313.frcscoutingapp.fragments.ScoutingNotesFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 3313 on 3/13/2018.
 */

public class MatchButtons extends LinearLayout {

    public MatchButtons(Context context, final ScoutingFragment fragment) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        Button matchButton = new Button(fragment.getActivity());
        matchButton.setText("Match");
        if (fragment instanceof ScoutingNotesFragment) {
            final ScoutingNotesFragment sm = (ScoutingNotesFragment) fragment;
            matchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        sm.data.getJSONObject("data").put("notes", sm.editText.getText());
                    } catch (JSONException e) {
                    }
                    FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, ScoutingMatchFragment.newInstance(sm.data)).commit();
                }
            });
        } else {
            matchButton.setEnabled(false);
        }
        addView(matchButton);
        Button notesButton = new Button(fragment.getActivity());
        notesButton.setText("Notes");
        if (fragment instanceof ScoutingMatchFragment) {
            final ScoutingMatchFragment sm = (ScoutingMatchFragment) fragment;
            notesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveMatchData(sm);
                    FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, ScoutingNotesFragment.newInstance(fragment.data)).commit();
                }
            });
        } else {
            notesButton.setEnabled(false);
        }
        addView(notesButton);

        Button saveButton = new Button(fragment.getContext());
        saveButton.setText(R.string.saveMatchButton);
        saveButton.setHighlightColor(getResources().getColor(R.color.colorAccent));
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment instanceof ScoutingMatchFragment) {
                    saveMatchData((ScoutingMatchFragment) fragment);

                } else if (fragment instanceof ScoutingNotesFragment) {
                    ScoutingNotesFragment sm = (ScoutingNotesFragment) fragment;
                    try {
                        sm.data.getJSONObject("data").put("notes", sm.editText.getText());
                    } catch (JSONException e) {
                    }
                }
                try {
                    fragment.data.put("updated", true);
                    fragment.data.put("scouted", true);
                    DataStore.matchTable.put(fragment.data.getString("match"), fragment.data.getString("position"), fragment.data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    DataStore.updateTeamStats(fragment.data.getString("team"));
                } catch (JSONException e) {
                }
            }
        });
        addView(saveButton);
    }

    private void saveMatchData(ScoutingMatchFragment sm) {

        try {

            JSONObject dat = new JSONObject();
            JSONObject auto = new JSONObject();
            JSONObject hab = new JSONObject();
            JSONObject rocket = new JSONObject();
            JSONObject pod = new JSONObject();

            auto.put("hatch", sm.autoHatch.isChecked());
            auto.put("cargo", sm.autoCargo.isChecked());
            auto.put("movement", sm.autoMoveBox.isChecked());

            hab.put("start", sm.habStart.getSelectedItemPosition());
            hab.put("end", sm.habEnd.getSelectedItemPosition());

            pod.put("hatch", sm.podHatch.getValue());
            pod.put("cargo", sm.podCargo.getValue());

            JSONArray rocketCargo = new JSONArray();
            rocketCargo.put(sm.rocketTopCargo.getValue());
            rocketCargo.put(sm.rocketMidCargo.getValue());
            rocketCargo.put(sm.rocketBotCargo.getValue());
            rocket.put("cargo", rocketCargo);

            JSONArray rocketHatch = new JSONArray();
            rocketHatch.put(sm.rocketTopHatch.getValue());
            rocketHatch.put(sm.rocketMidHatch.getValue());
            rocketHatch.put(sm.rocketBotHatch.getValue());
            rocket.put("hatch", rocketHatch);

            dat.put("auto", auto);
            dat.put("habitat", hab);
            dat.put("rocket", rocket);
            dat.put("pod", pod);
            dat.put("defense", sm.defenseBox.isChecked());
            sm.data.put("data", dat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
