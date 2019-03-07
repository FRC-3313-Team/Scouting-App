package com.team3313.frcscoutingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.R;
import com.team3313.frcscoutingapp.components.MatchButtons;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoutingNotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutingNotesFragment extends ScoutingFragment {

    LinearLayout linearLayout;
    public EditText editText;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScoutingNotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoutingNotesFragment newInstance(JSONObject start) {
        ScoutingNotesFragment fragment = new ScoutingNotesFragment();
        fragment.data = start;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.instance.setTitle("Notes for " + getData("match_key", String.class).split("_")[1].replace("qm", "Qualifier Match ") + " - Watching Team " + getData("team_key", String.class).substring(3));
        linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonRow = new MatchButtons(getContext(), this);
        linearLayout.addView(buttonRow);

        editText = new EditText(getContext());

        try {
            editText.setText(data.getString("notes"));
        } catch (JSONException e) {
        }

        linearLayout.addView(editText);
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
