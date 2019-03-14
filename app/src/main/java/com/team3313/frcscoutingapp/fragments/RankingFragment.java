package com.team3313.frcscoutingapp.fragments;

/**
 * Created by oa10712 on 3/7/2018.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team3313.frcscoutingapp.DataStore;
import com.team3313.frcscoutingapp.R;
import com.team3313.frcscoutingapp.components.TeamTableDataAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.SortingOrder;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;


public class RankingFragment extends Fragment {
    private static final String[] TABLE_HEADERS = {"Team Number", "Ranking Score"};
    SortableTableView<JSONObject> table;

    public RankingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        JSONArray names = DataStore.teamData.names();
        JSONObject[] teams = new JSONObject[names.length()];
        for (int i = 0; i < names.length(); i++) {
            try {
                DataStore.updateTeamStats(names.getString(i));
                teams[i] = DataStore.teamData.getJSONObject(names.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        table = new SortableTableView<>(getContext());
        table.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(), TABLE_HEADERS));
        table.setColumnCount(2);
        table.setDataAdapter(new TeamTableDataAdapter(getContext(), teams));
        table.setColumnComparator(0, new TeamNumberComparator());
        table.setColumnComparator(1, new TeamOverallScoreComparator());

        table.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(getResources().getColor(R.color.colorAltRow), Color.WHITE));

        table.setHeaderBackgroundColor(getResources().getColor(R.color.colorAccent));

        table.sort(1, SortingOrder.DESCENDING);
        return table;
    }


    public static class TeamNumberComparator implements java.util.Comparator<JSONObject> {
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            try {
                int first = Integer.decode(o1.getString("key").substring(3));
                int second = Integer.decode(o2.getString("key").substring(3));
                return first - second;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    public class TeamOverallScoreComparator implements java.util.Comparator<JSONObject> {
        @Override
        public int compare(JSONObject o1, JSONObject o2) {
            try {
                double first = TeamTableDataAdapter.getOverallScore(o1.getJSONObject("stats"));
                double second = TeamTableDataAdapter.getOverallScore(o2.getJSONObject("stats"));
                return (int) (first * 100 - second * 100);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}