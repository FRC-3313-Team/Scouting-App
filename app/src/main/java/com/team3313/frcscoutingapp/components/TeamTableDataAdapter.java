package com.team3313.frcscoutingapp.components;

import android.content.Context;
import android.support.v4.app.FragmentManager;
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
        return 0
                + 200 * team.getDouble("autoScale")
                + 75 * team.getDouble("climb")
                + 23 * team.getDouble("autoSwitch")
                + 10.87 * team.getDouble("scale")
                + 9.82 * team.getDouble("exchange")
                + 5.6 * team.getDouble("switch")
                + 5.4 * team.getDouble("cross");
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
                    number = team.getString("number");
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
            double score = (int) (getOverallScore(team) * 100);

            view.setText(score / 100 + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private TextView renderTeamNumber(JSONObject team) {
        TextView view = new TextView(getContext());
        try {
            view.setText(team.getString("number").substring(3));
        } catch (JSONException e) {
        }
        return view;
    }
}
