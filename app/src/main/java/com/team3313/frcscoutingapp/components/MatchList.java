package com.team3313.frcscoutingapp.components;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.team3313.frcscoutingapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

/**
 * Created by oa10712 on 3/14/2018.
 */

public class MatchList extends LinearLayout {
    private static final String[] TABLE_HEADERS = {"Match ID", "Red 1", "Red 2", "Red 3", "Blue 1", "Blue 2", "Blue 3"};
    ArrayList<String> matches = new ArrayList();
    SortableTableView<JSONObject> table;

    public MatchList(Context context, ArrayList<String> list) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.parseInt(o1.substring(2)) - Integer.parseInt(o2.substring(2));
            }
        });
        matches = list;
        View matches = createMatches();
        addView(matches);
    }

    private View createMatches() {
        table = new SortableTableView<>(getContext());
        table.setHeaderAdapter(new SimpleTableHeaderAdapter(getContext(), TABLE_HEADERS));
        table.setColumnCount(7);
        table.setHeaderBackgroundColor(getResources().getColor(R.color.colorAccent));


        JSONObject[] matchJson = new JSONObject[matches.size()];
        for (int i = 0; i < matches.size(); i++) {
            try {
                JSONObject match = new JSONObject();
                match.put("key", matches.get(i));
                matchJson[i] = match;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        table.setDataAdapter(new MatchListTableDataAdapter(getContext(), matchJson));

        table.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(getResources().getColor(R.color.colorAltRow), Color.WHITE));
        return table;
    }
}
