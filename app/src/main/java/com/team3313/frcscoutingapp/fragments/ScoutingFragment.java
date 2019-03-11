package com.team3313.frcscoutingapp.fragments;

import android.support.v4.app.Fragment;

import com.team3313.frcscoutingapp.DataStore;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 3313 on 3/13/2018.
 */

public class ScoutingFragment extends Fragment {

    public JSONObject data;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScoutingNotesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoutingFragment newInstance(JSONObject start) {
        ScoutingFragment fragment = new ScoutingFragment();
        fragment.data = start;
        return fragment;
    }

    public void saveData() {
        try {
            data.put("updated", true);
            DataStore.matchTable.put(data.getString("match"), data.getString("position"), data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
