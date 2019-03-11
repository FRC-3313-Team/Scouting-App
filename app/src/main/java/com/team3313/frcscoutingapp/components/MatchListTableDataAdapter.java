package com.team3313.frcscoutingapp.components;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team3313.frcscoutingapp.DataStore;
import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.R;
import com.team3313.frcscoutingapp.fragments.ScoutingMatchFragment;

import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.TableDataAdapter;

/**
 * Created by oa10712 on 3/20/2018.
 */

class MatchListTableDataAdapter extends TableDataAdapter<JSONObject> {
    public MatchListTableDataAdapter(Context context, JSONObject[] matchList) {
        super(context, matchList);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final JSONObject match = getRowData(rowIndex);
        TextView renderedView = new TextView(getContext());

        switch (columnIndex) {
            case 0:
                try {
                    renderedView.setText(match.getString("key").split("_")[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    String team = DataStore.matchTable.get(match.getString("key"),"r1").getString("team");
                    renderedView.setText(team.substring(3));
                    if (DataStore.matchTable.get(match.getString("key"),"r1").getBoolean("scouted")) {
                        renderedView.setTypeface(null, Typeface.BOLD_ITALIC);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    String team = DataStore.matchTable.get(match.getString("key"),"r2").getString("team");
                    renderedView.setText(team.substring(3));
                    if (DataStore.matchTable.get(match.getString("key"),"r2").getBoolean("scouted")) {
                        renderedView.setTypeface(null, Typeface.BOLD_ITALIC);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    String team = DataStore.matchTable.get(match.getString("key"),"r3").getString("team");
                    renderedView.setText(team.substring(3));
                    if (DataStore.matchTable.get(match.getString("key"),"r3").getBoolean("scouted")) {
                        renderedView.setTypeface(null, Typeface.BOLD_ITALIC);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    String team = DataStore.matchTable.get(match.getString("key"),"b1").getString("team");
                    renderedView.setText(team.substring(3));
                    if (DataStore.matchTable.get(match.getString("key"),"b1").getBoolean("scouted")) {
                        renderedView.setTypeface(null, Typeface.BOLD_ITALIC);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    String team = DataStore.matchTable.get(match.getString("key"),"b2").getString("team");
                    renderedView.setText(team.substring(3));
                    if (DataStore.matchTable.get(match.getString("key"),"b2").getBoolean("scouted")) {
                        renderedView.setTypeface(null, Typeface.BOLD_ITALIC);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    String team = DataStore.matchTable.get(match.getString("key"),"b2").getString("team");
                    renderedView.setText(team.substring(3));
                    if (DataStore.matchTable.get(match.getString("key"),"b2").getBoolean("scouted")) {
                        renderedView.setTypeface(null, Typeface.BOLD_ITALIC);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

        renderedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject start = null;
                try {
                    String pos = "";
                    switch (DataStore.config.getInt("station")) {
                        case 0:
                            pos ="r1";
                            break;
                        case 1:
                            pos ="r2";
                            break;
                        case 2:
                            pos ="r3";
                            break;
                        case 3:
                            pos ="b1";
                            break;
                        case 4:
                            pos ="b2";
                            break;
                        case 5:
                            pos ="b3";
                            break;
                    }
                    start = DataStore.matchTable.get(match.getString("key"), pos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = MainActivity.instance.getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, ScoutingMatchFragment.newInstance(start)).commit();
            }
        });

        renderedView.setMinHeight(75);
        renderedView.setGravity(Gravity.CENTER_VERTICAL);
        return renderedView;
    }
}
