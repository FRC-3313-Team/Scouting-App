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
import android.widget.TextView;
import android.widget.Spinner;

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

    public CheckBox autoMoveBox;
    public CheckBox defenseBox;
    public Spinner habStart;
    public Spinner habEnd;
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
        MainActivity.instance.setTitle(getData("match_key", String.class).split("_")[1].replace("qm", "Qualifier Match ") + " - Watching Team " + getData("team_key", String.class).substring(3));
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





        //endgame data
        LinearLayout endgameRow = new LinearLayout(getContext());
        endgameRow.setOrientation(LinearLayout.HORIZONTAL);

        TextView defenceLabel = new TextView(getContext());
        defenceLabel.setText("Played Defense");
        endgameRow.addView(defenceLabel);

        defenseBox = new CheckBox(getContext());

//TODO set defence based on previous data


        TextView habEndLabel = new TextView(getContext());
        habEndLabel.setText("End Level");
        endgameRow.addView(habEndLabel);

        habEnd = new Spinner(getContext());
        ArrayAdapter<CharSequence> endAdapter = ArrayAdapter.createFromResource(getContext(), R.array.hab_levels, android.R.layout.simple_spinner_item);
        endAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        habEnd.setAdapter(endAdapter);
        autoRow.addView(habEnd);


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
