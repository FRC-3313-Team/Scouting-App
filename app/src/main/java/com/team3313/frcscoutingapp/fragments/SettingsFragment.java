package com.team3313.frcscoutingapp.fragments;

/**
 * Created by oa10712 on 3/7/2018.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.team3313.frcscoutingapp.DataStore;
import com.team3313.frcscoutingapp.R;
import com.team3313.frcscoutingapp.RESTGetter;

import org.json.JSONException;
import org.json.JSONObject;


public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    TableLayout tableLayout;
    ScrollView scrollView;
    Spinner spinner;
    EditText apiKey;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        tableLayout = new TableLayout(getContext());
        scrollView = new ScrollView(getContext());

        TableRow stationRow = new TableRow(getContext());
        TextView spinnerLabel = new TextView(getContext());
        spinnerLabel.setText("Driver Station to Watch: ");
        stationRow.addView(spinnerLabel);
        spinner = new Spinner(getContext());
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.driver_station, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        try {
            spinner.setSelection(DataStore.config.getInt("station"));
        } catch (JSONException e) {
        }
        stationRow.addView(spinner);


        TableRow apiRow = new TableRow(getActivity());
        TextView apiLabel = new TextView(getContext());
        apiLabel.setText("Registration Code");
        apiRow.addView(apiLabel);
        apiKey = new EditText(getContext());
        apiKey.setWidth(300);
        apiRow.addView(apiKey);

        TableRow saveRow = new TableRow(getContext());
        Button saveButton = new Button(getActivity());
        saveButton.setText("Attempt Registration");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = "{\"activationCode\": " + apiKey.getText().toString() + "}";
                final RESTGetter.HttpsSubmitTask t = new RESTGetter.HttpsSubmitTask(DataStore.SERVER + "/api/device/register", body) {

                    @Override
                    protected void customEnd(String r) {
                        try {
                            System.out.println(r);

                            JSONObject response = new JSONObject(r);
                            DataStore.config.put("apiKey", response.get("token"));
                            DataStore.saveConfig();
                            Toast.makeText(getContext(), r, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
e.printStackTrace();
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                t.execute();
            }
        });
        saveRow.addView(saveButton);

        TableRow refreshRow = new TableRow(getContext());

        Button refreshButton = new Button(getActivity());
        refreshButton.setText("Synchronize data");
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataStore.manualRefresh();
            }
        });
        refreshRow.addView(refreshButton);

        tableLayout.addView(stationRow);
        tableLayout.addView(apiRow);
        tableLayout.addView(saveRow);
        tableLayout.addView(refreshRow);
        scrollView.addView(tableLayout);
        return scrollView;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        try {
            DataStore.config.put("station", pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}