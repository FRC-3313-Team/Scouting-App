package com.team3313.frcscoutingapp.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team3313.frcscoutingapp.DataStore;
import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.components.TeamButtons;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oa10712 on 3/12/2018.
 */

public class TeamNotesFragment extends TeamFragment {
    private static final String ARG_TEAM_KEY = "frc3313";
    LinearLayout linearLayout;

    public TeamNotesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TeamPRFragment.
     */
    public static TeamNotesFragment newInstance(String param1) {
        TeamNotesFragment fragment = new TeamNotesFragment();
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
        MainActivity.instance.setTitle("Team " + teamKey + ", " + DataStore.getTeamField(teamKey, "name") + " - Notes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonRow = new TeamButtons(getContext(), this);
        linearLayout.addView(buttonRow);


        for (JSONObject item : DataStore.matchTable.values()) {
            try {
                if (item.getString("team").equalsIgnoreCase(teamKey)) {
                    if (item.getString("notes") != null && !item.getString("notes").equalsIgnoreCase("")) {
                        TextView label = new TextView(getContext());
                        label.setText(item.getString("match").split("_")[1]);
                        label.setTypeface(null, Typeface.BOLD);
                        label.setTextSize(20);
                        linearLayout.addView(label);

                        TextView notes = new TextView(getContext());
                        notes.setText(item.getString("notes"));
                        linearLayout.addView(notes);
                    }
                }
            } catch (JSONException e) {
            }
        }


        return linearLayout;
    }

}
