package com.team3313.frcscoutingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.team3313.frcscoutingapp.DataStore;
import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.components.MatchList;
import com.team3313.frcscoutingapp.components.TeamButtons;

/**
 * Created by oa10712 on 3/12/2018.
 */

public class TeamMatchesFragment extends TeamFragment {
    private static final String ARG_TEAM_KEY = "frc3313";
    LinearLayout linearLayout;

    public TeamMatchesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment PRFragment.
     */
    public static TeamMatchesFragment newInstance(String param1) {
        TeamMatchesFragment fragment = new TeamMatchesFragment();
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
        MainActivity.instance.setTitle("Team " + teamKey + ", " + DataStore.getTeamField(teamKey, "name") + " - Matches");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonRow = new TeamButtons(getContext(), this);
        linearLayout.addView(buttonRow);


        linearLayout.addView(new MatchList(getContext(), DataStore.searchTeamMatches(teamKey, MainActivity.instance.getActiveRegional())));
        return linearLayout;
    }

}
