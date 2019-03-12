package com.team3313.frcscoutingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.R;
import com.team3313.frcscoutingapp.components.MatchButtons;
import com.team3313.frcscoutingapp.components.NumberPicker;

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
    // TODO: Rename and change types of parameters

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
        try {
            //TODO check actual data name                                         V
            autoMoveBox.setChecked(data.getJSONObject("auto").getBoolean("passedLine"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        TableLayout teleopTable = new TableLayout(getContext());

        //label row
        TableRow rLabelRow = new TableRow(getContext());

        TableRow.LayoutParams params = new TableRow.LayoutParams();
        TextView cargoLabel = new TextView(getContext());
        cargoLabel.setText("Cargo");
        params = new TableRow.LayoutParams();
        params.span = 2; //amount of columns you will span
        params.gravity = Gravity.CENTER_HORIZONTAL;
        cargoLabel.setLayoutParams(params);
        rLabelRow.addView(cargoLabel);

        rLabelRow.addView(new Space(getContext()));
        TextView hatchLabel = new TextView(getContext());
        hatchLabel.setText("Hatch");
        params = new TableRow.LayoutParams();
        params.span = 2; //amount of columns you will span
        params.gravity = Gravity.CENTER_HORIZONTAL;
        hatchLabel.setLayoutParams(params);
        rLabelRow.addView(hatchLabel);

        //top row
        TableRow rTopRow = new TableRow(getContext());
        rocketTopCargo = new NumberPicker(getContext(), null);
        rTopRow.addView(rocketTopCargo);
        rTopRow.addView(new Space(getContext()));
        rTopRow.addView(new Space(getContext()));
        rocketTopHatch = new NumberPicker(getContext(), null);
        rTopRow.addView(new Space(getContext()));

        teleopTable.addView(rTopRow);

        //mid row
        TableRow rMidRow = new TableRow(getContext());
        rocketMidCargo = new NumberPicker(getContext(), null);
        rMidRow.addView(rocketMidCargo);
        rMidRow.addView(new Space(getContext()));
        rMidRow.addView(new Space(getContext()));
        rocketMidHatch = new NumberPicker(getContext(), null);
        rMidRow.addView(new Space(getContext()));

        teleopTable.addView(rMidRow);

        //bot row
        TableRow rBotRow = new TableRow(getContext());
        rocketBotCargo = new NumberPicker(getContext(), null);
        rBotRow.addView(rocketBotCargo);
        podCargo = new NumberPicker(getContext(), null);
        rBotRow.addView(podCargo);
        rBotRow.addView(new Space(getContext()));
        rocketBotHatch = new NumberPicker(getContext(), null);
        podHatch = new NumberPicker(getContext(), null);
        rBotRow.addView(podHatch);

        teleopTable.addView(rBotRow);


        linearLayout.addView(teleopTable);

        //endgame data
        LinearLayout endgameRow = new LinearLayout(getContext());
        endgameRow.setOrientation(LinearLayout.HORIZONTAL);

        TextView defenceLabel = new TextView(getContext());
        defenceLabel.setText("Played Defense");
        endgameRow.addView(defenceLabel);

        defenseBox = new CheckBox(getContext());

        endgameRow.addView(defenseBox);

//TODO set defence based on previous data


        TextView habEndLabel = new TextView(getContext());
        habEndLabel.setText("End Level");
        endgameRow.addView(habEndLabel);

        habEnd = new Spinner(getContext());
        ArrayAdapter<CharSequence> endAdapter = ArrayAdapter.createFromResource(getContext(), R.array.hab_levels, android.R.layout.simple_spinner_item);
        endAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habEnd.setAdapter(endAdapter);
        endgameRow.addView(habEnd);


        linearLayout.addView(endgameRow);

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
