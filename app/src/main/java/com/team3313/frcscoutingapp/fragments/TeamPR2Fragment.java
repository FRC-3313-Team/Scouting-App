package com.team3313.frcscoutingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.team3313.frcscoutingapp.DataStore;
import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.R;
import com.team3313.frcscoutingapp.components.TeamButtons;
import com.team3313.frcscoutingapp.components.TextViewExtra;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by oa10712 on 3/12/2018.
 */

public class TeamPR2Fragment extends TeamFragment {
    private static final String ARG_TEAM_KEY = "frc3313";
    public EditText gameStrategy;
    public EditText climbing;
    public EditText auton;
    public EditText cubeScore;
    public EditText cubeIntake;
    LinearLayout layout;

    public TeamPR2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment TeamPRFragment.
     */
    public static TeamPR2Fragment newInstance(String param1) {
        TeamPR2Fragment fragment = new TeamPR2Fragment();
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
        MainActivity.instance.setTitle("Team " + teamKey + ", " + DataStore.getTeamField(teamKey, "name") + " - PR");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout buttonRow = new TeamButtons(getContext(), this);
        layout.addView(buttonRow);
        JSONObject teamData = new JSONObject();
        try {
            teamData = DataStore.teamData.getJSONObject(teamKey).getJSONObject("pit");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TableLayout table = new TableLayout(getContext());
        table.setStretchAllColumns(true);

        TableRow labels1 = new TableRow(getContext());
        TextView climbLabel = new TextViewExtra(getContext(), "Climbing/How", null);
        labels1.addView(climbLabel);
        TextView autonLabel = new TextViewExtra(getContext(), "Auton Preferred Starting Position", null);
        labels1.addView(autonLabel);
        table.addView(labels1);

        TableRow data1 = new TableRow(getContext());
        climbing = new EditText(getContext());
        try {
            climbing.setText(teamData.getJSONObject("2018game").getString("climb"));
        } catch (JSONException e) {
        }
        data1.addView(climbing);
        auton = new EditText(getContext());
        try {
            auton.setText(teamData.getJSONObject("2018game").getString("auton"));
        } catch (JSONException e) {
        }
        data1.addView(auton);
        table.addView(data1);

        TableRow label2 = new TableRow(getContext());
        TextView scoreLabel = new TextViewExtra(getContext(), "Method of Cube Scoring", null);
        label2.addView(scoreLabel);
        TextView intakeLabel = new TextViewExtra(getContext(), "Method of Cube Intake", null);
        label2.addView(intakeLabel);
        table.addView(label2);

        TableRow data2 = new TableRow(getContext());
        cubeScore = new EditText(getContext());
        try {
            cubeScore.setText(teamData.getJSONObject("2018game").getString("score"));
        } catch (JSONException e) {
        }
        data2.addView(cubeScore);
        cubeIntake = new EditText(getContext());
        try {
            cubeIntake.setText(teamData.getJSONObject("2018game").getString("intake"));
        } catch (JSONException e) {
        }
        data2.addView(cubeIntake);
        table.addView(data2);

        TableRow stratLabel = new TableRow(getContext());
        TextView strat = new TextViewExtra(getContext(), "Game Strategy", null);
        stratLabel.addView(strat);
        table.addView(stratLabel);

        TableRow stratData = new TableRow(getContext());
        gameStrategy = new EditText(getContext());
        try {
            gameStrategy.setText(teamData.getJSONObject("2018game").getString("strategy"));
        } catch (JSONException e) {
        }
        stratData.addView(gameStrategy);
        table.addView(stratData);

        layout.addView(table);
        return layout;
    }

}
