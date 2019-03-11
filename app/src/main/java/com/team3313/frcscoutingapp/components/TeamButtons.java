package com.team3313.frcscoutingapp.components;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;

import com.team3313.frcscoutingapp.DataStore;
import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.R;
import com.team3313.frcscoutingapp.fragments.TeamDataFragment;
import com.team3313.frcscoutingapp.fragments.TeamFragment;
import com.team3313.frcscoutingapp.fragments.TeamMatchesFragment;
import com.team3313.frcscoutingapp.fragments.TeamNotesFragment;
import com.team3313.frcscoutingapp.fragments.TeamPR2Fragment;
import com.team3313.frcscoutingapp.fragments.TeamPRFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oa10712 on 3/13/2018.
 */

public class TeamButtons extends LinearLayout {
    public TeamButtons(Context context, final TeamFragment fragment) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
        Button dataButton = new Button(getContext());
        dataButton.setText("Data");
        if (fragment instanceof TeamDataFragment) {
            dataButton.setEnabled(false);
        } else {
            dataButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, TeamDataFragment.newInstance(fragment.teamKey)).commit();
                }
            });
        }
        addView(dataButton);
        Button matchesButton = new Button(fragment.getActivity());
        matchesButton.setText("Matches");
        if (fragment instanceof TeamMatchesFragment) {
            matchesButton.setEnabled(false);
        } else {
            matchesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                    TeamMatchesFragment schedule = TeamMatchesFragment.newInstance(fragment.teamKey);
                    fragmentManager.beginTransaction().replace(R.id.content_frame, schedule).commit();
                }
            });
        }
        addView(matchesButton);
        Button notesButton = new Button(fragment.getActivity());
        notesButton.setText("Notes");
        if (fragment instanceof TeamNotesFragment) {
            notesButton.setEnabled(false);
        } else {
            notesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, TeamNotesFragment.newInstance(fragment.teamKey)).commit();
                }
            });
        }
        addView(notesButton);
        Button prButton = new Button(getContext());
        prButton.setText("PR");
        if (fragment instanceof TeamPRFragment) {
            prButton.setEnabled(false);
            Button saveButton = new Button(getContext());
            saveButton.setText("Save PR Data");
            saveButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeamPRFragment frag = (TeamPRFragment) fragment;
                    try {
                        JSONObject pit = new JSONObject();

                        try {
                            pit = DataStore.teamData.getJSONObject(frag.teamKey).getJSONObject("pit");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject awards = new JSONObject();
                        awards.put("chairmans", frag.chairmansBox.isChecked());
                        awards.put("flowers", frag.flowersBox.isChecked());
                        awards.put("deans", frag.deansBox.isChecked());
                        awards.put("safety", frag.safetyBox.isChecked());
                        awards.put("entrepreneurship", frag.entrepreneurshipBox.isChecked());
                        pit.put("awards", awards);
                        JSONArray social = new JSONArray();
                        for (TableRow r : frag.socialRows) {
                            Spinner site = (Spinner) r.getChildAt(0);
                            EditText handle = (EditText) r.getChildAt(1);
                            JSONObject row = new JSONObject();
                            if (!site.getSelectedItem().toString().equalsIgnoreCase("")) {
                                row.put("site", site.getSelectedItemPosition());
                                row.put("handle", handle.getText());
                                social.put(row);
                            }
                        }
                        pit.put("social", social);
                        pit.put("outreach", frag.outreach.getText());
                        pit.put("updated", true);
                        DataStore.teamData.getJSONObject(frag.teamKey).put("pit", pit);
                        System.out.println("Saving award data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            addView(saveButton);
        } else {
            prButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, TeamPRFragment.newInstance(fragment.teamKey)).commit();
                }
            });
        }
        addView(prButton);


        Button pr2Button = new Button(getContext());
        pr2Button.setText("Robot Strategy");
        if (fragment instanceof TeamPR2Fragment) {
            pr2Button.setEnabled(false);
            Button saveButton = new Button(getContext());
            saveButton.setText("Save PR2 Data");
            saveButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TeamPR2Fragment frag = (TeamPR2Fragment) fragment;
                    try {
                        JSONObject pit = new JSONObject();
                        try {
                            pit = DataStore.teamData.getJSONObject(frag.teamKey).getJSONObject("pit");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONObject strategy = new JSONObject();

                        strategy.put("auton", frag.auton.getText());
                        strategy.put("climb", frag.climbing.getText());
                        strategy.put("intake", frag.cubeIntake.getText());
                        strategy.put("score", frag.cubeScore.getText());
                        strategy.put("strategy", frag.gameStrategy.getText());

                        pit.put("2018game", strategy);
                        pit.put("updated", true);
                        DataStore.teamData.getJSONObject(frag.teamKey).put("pit", pit);
                        System.out.println("Saving pit data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            addView(saveButton);
        } else {
            pr2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, TeamPR2Fragment.newInstance(fragment.teamKey)).commit();
                }
            });
        }
        addView(pr2Button);
    }
}
