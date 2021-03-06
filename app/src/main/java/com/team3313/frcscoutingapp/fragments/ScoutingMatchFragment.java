package com.team3313.frcscoutingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.R;
import com.team3313.frcscoutingapp.components.MatchButtons;
import com.team3313.frcscoutingapp.components.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoutingMatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutingMatchFragment extends ScoutingFragment {

    public CheckBox autoMoveBox, defenseBox;
    public Spinner habStart, habEnd;
    public NumberPicker
            rocketTopCargo, rocketMidCargo, rocketBotCargo, podCargo,
            rocketTopHatch, rocketMidHatch, rocketBotHatch, podHatch;

    LinearLayout linearLayout;
    LinearLayout buttonRow;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param start Parameter 1.
     * @return A new instance of fragment ScoutingMatchFragment.
     */
    public static ScoutingMatchFragment newInstance(JSONObject start) {
        ScoutingMatchFragment fragment = new ScoutingMatchFragment();
        fragment.data = start;
        try {
            fragment.data.put("updated", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.instance.setTitle(getData("match", String.class).replace("qm", "Qualifier Match ") + " - Watching Team " + getData("team", String.class).substring(3));
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        buttonRow = new MatchButtons(getContext(), this);
        linearLayout.addView(buttonRow);

        //Auto data
        LinearLayout autoRow = new LinearLayout(getContext());
        autoRow.setOrientation(LinearLayout.HORIZONTAL);

        TextView autoMoveLabel = new TextView(getContext());
        autoMoveLabel.setText("Autonomous Movement");
        autoRow.addView(autoMoveLabel);

        autoMoveBox = new CheckBox(getContext());
        autoRow.addView(autoMoveBox);

        TextView habStartLabel = new TextView(getContext());
        habStartLabel.setText("Start Level");
        autoRow.addView(habStartLabel);

        habStart = new Spinner(getContext());
        ArrayAdapter<CharSequence> startAdapter = ArrayAdapter.createFromResource(getContext(), R.array.hab_levels, android.R.layout.simple_spinner_item);
        startAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habStart.setAdapter(startAdapter);
        autoRow.addView(habStart);
//TODO auto hatch/cargo
        linearLayout.addView(autoRow);

        //teleop data
        GridLayout grid = new GridLayout(getContext());

        //label row
        TextView cargoLabel = new TextView(getContext());

        cargoLabel.setText("Cargo");
        cargoLabel.setTextSize(25);
        GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams(
                GridLayout.spec(0, 1, GridLayout.CENTER),
                GridLayout.spec(0, 2, GridLayout.CENTER));
        grid.addView(cargoLabel, gridParams);

        TextView hatchLabel = new TextView(getContext());
        hatchLabel.setText("Hatch");
        hatchLabel.setTextSize(25);
        gridParams = new GridLayout.LayoutParams(
                GridLayout.spec(0, 1, GridLayout.CENTER),
                GridLayout.spec(3, 2, GridLayout.CENTER));
        grid.addView(hatchLabel, gridParams);

        //top row
        rocketTopCargo = new NumberPicker(getContext());
        rocketTopCargo.setOrientation(LinearLayout.HORIZONTAL);
        gridParams = new GridLayout.LayoutParams(
                GridLayout.spec(1, 1, GridLayout.CENTER),
                GridLayout.spec(0, 1));
        grid.addView(rocketTopCargo, gridParams);
        rocketTopHatch = new NumberPicker(getContext());
        gridParams = new GridLayout.LayoutParams(
                GridLayout.spec(1, 1, GridLayout.CENTER),
                GridLayout.spec(3, 1));
        grid.addView(rocketTopHatch, gridParams);

        //mid row
        rocketMidCargo = new NumberPicker(getContext());
        gridParams = new GridLayout.LayoutParams(
                GridLayout.spec(2, 1, GridLayout.CENTER),
                GridLayout.spec(0, 1));
        grid.addView(rocketMidCargo, gridParams);
        rocketMidHatch = new NumberPicker(getContext());
        gridParams = new GridLayout.LayoutParams(
                GridLayout.spec(2, 1, GridLayout.CENTER),
                GridLayout.spec(3, 1));
        grid.addView(rocketMidHatch, gridParams);

        //bot row
        rocketBotCargo = new NumberPicker(getContext());
        gridParams = new GridLayout.LayoutParams(
                GridLayout.spec(3, 1, GridLayout.CENTER),
                GridLayout.spec(0, 1));
        grid.addView(rocketBotCargo, gridParams);
        podCargo = new NumberPicker(getContext());
        gridParams = new GridLayout.LayoutParams(
                GridLayout.spec(3, 1, GridLayout.CENTER),
                GridLayout.spec(1, 1));
        grid.addView(podCargo, gridParams);
        rocketBotHatch = new NumberPicker(getContext());
        gridParams = new GridLayout.LayoutParams(
                GridLayout.spec(3, 1, GridLayout.CENTER),
                GridLayout.spec(3, 1));
        grid.addView(rocketBotHatch, gridParams);
        podHatch = new NumberPicker(getContext());
        gridParams = new GridLayout.LayoutParams(
                GridLayout.spec(3, 1, GridLayout.CENTER),
                GridLayout.spec(4, 1));
        grid.addView(podHatch, gridParams);


        linearLayout.addView(grid);

        //endgame data
        LinearLayout endgameRow = new LinearLayout(getContext());
        endgameRow.setOrientation(LinearLayout.HORIZONTAL);

        TextView defenseLabel = new TextView(getContext());
        defenseLabel.setText("Played Defense");
        endgameRow.addView(defenseLabel);

        defenseBox = new CheckBox(getContext());

        endgameRow.addView(defenseBox);

        TextView habEndLabel = new TextView(getContext());
        habEndLabel.setText("End Level");
        endgameRow.addView(habEndLabel);

        habEnd = new Spinner(getContext());
        ArrayAdapter<CharSequence> endAdapter = ArrayAdapter.createFromResource(getContext(), R.array.hab_levels, android.R.layout.simple_spinner_item);
        endAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habEnd.setAdapter(endAdapter);
        endgameRow.addView(habEnd);


        linearLayout.addView(endgameRow);

        //load previous data
        try {
            JSONObject matchData = this.data.getJSONObject("data");
            JSONObject auto = matchData.getJSONObject("auto");
            JSONObject hab = matchData.getJSONObject("habitat");
            JSONObject pod = matchData.getJSONObject("pod");
            JSONArray rocketHatch = matchData.getJSONObject("rocket").getJSONArray("hatch");
            JSONArray rocketCargo = matchData.getJSONObject("rocket").getJSONArray("cargo");
            autoMoveBox.setChecked(auto.getBoolean("movement"));
            habStart.setSelection(hab.getInt("start"));
            habEnd.setSelection(hab.getInt("end"));
            rocketTopCargo.setValue(rocketCargo.getInt(0));
            rocketMidCargo.setValue(rocketCargo.getInt(1));
            rocketBotCargo.setValue(rocketCargo.getInt(2));
            podCargo.setValue(pod.getInt("cargo"));
            rocketTopHatch.setValue(rocketHatch.getInt(0));
            rocketMidHatch.setValue(rocketHatch.getInt(1));
            rocketBotHatch.setValue(rocketHatch.getInt(2));
            podHatch.setValue(pod.getInt("hatch"));
            defenseBox.setChecked(matchData.getBoolean("defense"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return
        return linearLayout;
    }

    private <T> T getData(String key, Class<T> clazz) {
        try {
            return (T) data.get(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
