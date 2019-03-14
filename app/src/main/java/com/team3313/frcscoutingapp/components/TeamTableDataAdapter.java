package com.team3313.frcscoutingapp.components;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.R;
import com.team3313.frcscoutingapp.fragments.TeamDataFragment;

import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.TableDataAdapter;

/**
 * Created by oa10712 on 3/15/2018.
 */

public class TeamTableDataAdapter extends TableDataAdapter<JSONObject> {
    public TeamTableDataAdapter(Context context, JSONObject[] data) {
        super(context, data);

    }

    public static double getOverallScore(JSONObject team) throws JSONException {
        Log.e("AIRROR", team.toString());
        return 3076.9 * (getValueOr0(team, "Average Hab End") - 1)
                + 3333.3 * getValueOr0(team, "Average Rct. Hatch Top")
                + 500 * getValueOr0(team, "Average Rct. Hatch Mid")
                + 333.33 * getValueOr0(team, "Average Rct. Hatch Bot")
                + 500 * getValueOr0(team, "Average Pod Hatches")
                + 8000 * getValueOr0(team, "Average Rct. Cargo Top")
                + 400 * getValueOr0(team, "Average Rct. Cargo Mid")
                + 266.67 * getValueOr0(team, "Average Rct. Cargo Bot")
                + 200 * getValueOr0(team, "Average Pod Cargo")
                + 400 * getValueOr0(team, "Defensive Games %")
                + 100 * (getValueOr0(team, "Average Hab Start") - 1)
                + 21.053 * getValueOr0(team, "Auto Movement %")
                + 266.67 * getValueOr0(team, "Auto Cargo %")
                + 200 * getValueOr0(team, "Auto Hatch %");
    }

    private static double getValueOr0(JSONObject team, String name) {
        try {
            return team.getDouble(name);
        } catch (JSONException e) {
            return 0;
        }
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final JSONObject team = getRowData(rowIndex);
        TextView renderedView = null;
        switch (columnIndex) {
            case 0:
                renderedView = renderTeamNumber(team);
                break;
            case 1:
                renderedView = renderOverall(team);
                break;
        }
        renderedView.setGravity(Gravity.CENTER_VERTICAL);
        renderedView.setMinimumHeight(65);
        renderedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = null;
                try {
                    number = team.getString("key");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, TeamDataFragment.newInstance(number)).commit();
            }
        });
        return renderedView;
    }

    private TextView renderOverall(JSONObject team) {
        TextView view = new TextView(getContext());
        try {
            double score = (int) (getOverallScore(team.getJSONObject("stats")) * 100);

            view.setText(score / 100 + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private TextView renderTeamNumber(JSONObject team) {
        TextView view = new TextView(getContext());
        try {
            view.setText(team.getString("key").substring(3));
        } catch (JSONException e) {
        }
        return view;
    }
}
