package com.team3313.frcscoutingapp.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.team3313.frcscoutingapp.DataStore;
import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.components.TeamButtons;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamDataFragment extends TeamFragment {
    private static final String ARG_TEAM_KEY = "frc3313";
    private static int COLUMNS = 4;
    LinearLayout linearLayout;
    TableLayout tableLayout;


    public TeamDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TeamDataFragment.
     */
    public static TeamDataFragment newInstance(String param1) {
        TeamDataFragment fragment = new TeamDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEAM_KEY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            teamKey = getArguments().getString(ARG_TEAM_KEY);
        }
        MainActivity.instance.setTitle("Team " + teamKey.substring(3) + ", " + DataStore.getTeamField(teamKey, "name") + " - Data");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DataStore.updateTeamStats(teamKey);

        JSONObject teamStats = null;
        try {
            teamStats = DataStore.teamData.getJSONObject(teamKey).getJSONObject("stats");
        } catch (JSONException e) {
        }

        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonRow = new TeamButtons(getContext(), this);
        linearLayout.addView(buttonRow);

        GridLayout grid = new GridLayout(getContext());
        Log.i("AIRROR", teamStats.names().toString());
        Iterator<String> keys = teamStats.keys();
        int row = 0;
        while (keys.hasNext()) {
            for (int i = 0; i < COLUMNS; i++) {
                if (keys.hasNext()) {
                    String next = keys.next();

                    TextView label = new TextView(getContext());
                    label.setText(next);
                    label.setTypeface(null, Typeface.BOLD);
                    GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams(
                            GridLayout.spec(row, 1, GridLayout.CENTER, 1),
                            GridLayout.spec(i, 1, GridLayout.CENTER, 1));
                    grid.addView(label, gridParams);

                    TextView value = new TextView(getContext());
                    try {
                        value.setText(teamStats.getDouble(next) + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    gridParams = new GridLayout.LayoutParams(
                            GridLayout.spec(row + 1, 1, GridLayout.CENTER, 1),
                            GridLayout.spec(i, 1, GridLayout.CENTER, 1));
                    grid.addView(value, gridParams);

                } else {
                    i = COLUMNS;
                }
            }
            row += 2;
        }
        linearLayout.addView(grid);
        return linearLayout;
    }
}
