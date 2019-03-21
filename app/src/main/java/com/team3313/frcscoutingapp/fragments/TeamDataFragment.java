package com.team3313.frcscoutingapp.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.team3313.frcscoutingapp.DataStore;
import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.components.TeamButtons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamDataFragment extends TeamFragment {
    private static final String ARG_TEAM_KEY = "frc3313";
    private static int COLUMNS = 4;
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

        JSONObject teamStats = null;
        try {
            teamStats = DataStore.teamData.getJSONObject(teamKey).getJSONObject("stats");
        } catch (JSONException e) {
        }

        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout buttonRow = new TeamButtons(getContext(), this);
        linearLayout.addView(buttonRow);

        GridLayout grid = new GridLayout(getContext());
        Iterator<String> keys = teamStats.keys();

        int row = 0;
        while (keys.hasNext()) {
            for (int i = 0; i < COLUMNS; i++) {
                if (keys.hasNext()) {
                    String next = keys.next();

                    TextView label = new TextView(getContext());
                    label.setText(next);
                    label.setTypeface(null, Typeface.BOLD);
                    GridLayout.LayoutParams gridParams = new GridLayout.LayoutParams(
                            GridLayout.spec(row, 1, GridLayout.CENTER, 1),
                            GridLayout.spec(i, 1, GridLayout.CENTER, 1));
                    grid.addView(label, gridParams);

                    try {
                        Object o = teamStats.get(next);
                        if (o instanceof JSONArray) {
                            JSONArray arr = (JSONArray) o;

                            HashMap<Double, Integer> map = new HashMap<>();
                            for (int j = 0; j < arr.length(); j++) {
                                if (map.containsKey(arr.getDouble(j))) {
                                    map.put(arr.getDouble(j), map.get(arr.getDouble(j)) + 1);
                                } else {
                                    map.put(arr.getDouble(j), 1);
                                }
                            }
                            final List<BarEntry> dataList = new ArrayList<>();

                            Set<Map.Entry<Double, Integer>> entries = map.entrySet();
                            List<Map.Entry<Double, Integer>> entriesList = new ArrayList<>();
                            for (Map.Entry<Double, Integer> entry : entries) {
                                entriesList.add(entry);
                            }
                            Collections.sort(entriesList, new Comparator<Map.Entry<Double, Integer>>() {
                                @Override
                                public int compare(Map.Entry<Double, Integer> o1, Map.Entry<Double, Integer> o2) {
                                    return (int) (o1.getKey() - o2.getKey());
                                }
                            });

                            for (Map.Entry<Double, Integer> entry : entriesList) {
                                dataList.add(new BarEntry((float) (double) entry.getKey(), (float) entry.getValue()));
                            }


                            BarChart chart = new BarChart(getContext());

                            BarDataSet set = new BarDataSet(dataList, "data");
                            set.setValueFormatter(new ValueFormatter() {
                                @Override
                                public String getFormattedValue(float value) {

                                    return String.valueOf((int) value);
                                }
                            });

                            BarData barData = new BarData(set);

                            chart.setData(barData);
                            chart.setMinimumWidth(200);
                            chart.setMinimumHeight(150);
                            chart.setDrawMarkers(false);
                            chart.setDrawValueAboveBar(false);
                            chart.setDrawGridBackground(false);
                            Description desc = new Description();
                            desc.setText("");
                            chart.setDescription(desc);
                            chart.getXAxis().setGranularity(1);

                            chart.invalidate();


                            gridParams = new GridLayout.LayoutParams(
                                    GridLayout.spec(row + 1, 1, GridLayout.CENTER, 1),
                                    GridLayout.spec(i, 1, GridLayout.CENTER, 1));
                            grid.addView(chart, gridParams);

                        } else {
                            TextView value = new TextView(getContext());
                            value.setText(this.truncateDecimal(teamStats.getDouble(next), 2) + "");

                            gridParams = new GridLayout.LayoutParams(
                                    GridLayout.spec(row + 1, 1, GridLayout.CENTER, 1),
                                    GridLayout.spec(i, 1, GridLayout.CENTER, 1));
                            grid.addView(value, gridParams);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    i = COLUMNS;
                }
            }
            row += 2;
        }
        linearLayout.addView(grid);
        return linearLayout;
    }

    private static BigDecimal truncateDecimal(double x, int numberofDecimals) {
        if (x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }
}
