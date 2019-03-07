package com.team3313.frcscoutingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.team3313.frcscoutingapp.DataStore;
import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.components.TeamButtons;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamDataFragment extends TeamFragment {
    private static final String ARG_TEAM_KEY = "frc3313";
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

        JSONObject teamInfo = null;
        try {
            teamInfo = DataStore.teamData.getJSONObject(teamKey);
        } catch (JSONException e) {
        }

        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonRow = new TeamButtons(getContext(), this);
        linearLayout.addView(buttonRow);

        tableLayout = new TableLayout(getContext());
        tableLayout.setStretchAllColumns(true);

        TableRow labels1 = new TableRow(getContext());

        TextView scaleLabel = new TextView(getContext());
        scaleLabel.setText("Scale Cubes/Game");
        labels1.addView(scaleLabel);

        TextView switchLabel = new TextView(getContext());
        switchLabel.setText("Switch Cubes/Game");
        labels1.addView(switchLabel);

        TextView exchangeLabel = new TextView(getContext());
        exchangeLabel.setText("Exchange Cubes/Game");
        labels1.addView(exchangeLabel);

        TextView climbLabel = new TextView(getContext());
        climbLabel.setText("Succesful Climb %");
        labels1.addView(climbLabel);

        tableLayout.addView(labels1);


        TableRow values1 = new TableRow(getContext());

        TextView scaleValue = new TextView(getContext());
        try {
            scaleValue.setText(teamInfo.getDouble("scale") + "");
        } catch (JSONException e) {
            scaleValue.setText("0");
        }
        values1.addView(scaleValue);

        TextView switchValue = new TextView(getContext());
        try {
            switchValue.setText(teamInfo.getDouble("switch") + "");
        } catch (JSONException e) {
            switchValue.setText("0");
        }
        values1.addView(switchValue);

        TextView exchangeValue = new TextView(getContext());
        try {
            exchangeValue.setText(teamInfo.getDouble("exchange") + "");
        } catch (JSONException e) {
            exchangeValue.setText("0");
        }
        values1.addView(exchangeValue);

        TextView climbValue = new TextView(getContext());
        try {
            climbValue.setText(teamInfo.getDouble("climb") * 100 + "%");
        } catch (JSONException e) {
            climbValue.setText("0%");
        }
        values1.addView(climbValue);

        tableLayout.addView(values1);


        Space space = new Space(getContext());
        space.setMinimumHeight(50);
        tableLayout.addView(space);


        TableRow labels2 = new TableRow(getContext());

        TextView gamesLabel = new TextView(getContext());
        gamesLabel.setText("Matches Played");
        labels2.addView(gamesLabel);

        TextView autoCrossLabel = new TextView(getContext());
        autoCrossLabel.setText("Auto Line Cross %");
        labels2.addView(autoCrossLabel);

        TextView autoSwitchLabel = new TextView(getContext());
        autoSwitchLabel.setText("Auto Switch %");
        labels2.addView(autoSwitchLabel);

        TextView autoScaleLabel = new TextView(getContext());
        autoScaleLabel.setText("Auto Scale %");
        labels2.addView(autoScaleLabel);

        tableLayout.addView(labels2);


        TableRow values2 = new TableRow(getContext());

        TextView playValue = new TextView(getContext());
        try {
            playValue.setText(teamInfo.getInt("played") + "");
        } catch (JSONException e) {
            playValue.setText("0");
        }
        values2.addView(playValue);

        TextView crossValue = new TextView(getContext());
        try {
            crossValue.setText(teamInfo.getDouble("cross") * 100 + "%");
        } catch (JSONException e) {
            crossValue.setText("0%");
        }
        values2.addView(crossValue);

        TextView autoSwitchValue = new TextView(getContext());
        try {
            autoSwitchValue.setText(teamInfo.getDouble("autoSwitch") + "%");
        } catch (JSONException e) {
            autoSwitchValue.setText("0%");
        }
        values2.addView(autoSwitchValue);

        TextView autoScaleValue = new TextView(getContext());
        try {
            autoScaleValue.setText(teamInfo.getDouble("autoScale") + "%");
        } catch (JSONException e) {
            autoScaleValue.setText("0%");
        }
        values2.addView(autoScaleValue);


        tableLayout.addView(values2);


        linearLayout.addView(tableLayout);
        return linearLayout;
    }
}
