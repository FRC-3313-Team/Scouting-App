package com.team3313.frcscoutingapp.fragments;

/**
 * Created by oa10712 on 3/7/2018.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;
import android.util.Log;
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

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.team3313.frcscoutingapp.DataStore;
import com.team3313.frcscoutingapp.MainActivity;
import com.team3313.frcscoutingapp.R;
import com.team3313.frcscoutingapp.RESTGetter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;


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
                JSONObject body = null;
                try {
                    body = new JSONObject("{\"activationCode\": \"" + apiKey.getText().toString() + "\"}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest registerRequest = new JsonObjectRequest(Request.Method.POST,
                        DataStore.SERVER + "/api/device/register",
                        body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            DataStore.config.put("apiKey", response.get("token"));
                            DataStore.saveConfig();
                            Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();


                            JsonObjectRequest statusRequest = new JsonObjectRequest
                                    (Request.Method.GET, DataStore.SERVER + "/api/device/status", null, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                DataStore.config.put("regional", response.get("regional"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, null) {
                                public Map<String, String> getHeaders() {

                                    Map<String, String> mHeaders = new ArrayMap<String, String>();
                                    try {
                                        mHeaders.put("device-token", DataStore.config.getString("apiKey"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return mHeaders;
                                }
                            };
                            MainActivity.myRequestQueue.add(statusRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
                MainActivity.myRequestQueue.add(registerRequest);
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