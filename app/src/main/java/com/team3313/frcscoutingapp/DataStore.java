package com.team3313.frcscoutingapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.team3313.frcscoutingapp.fragments.SettingsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by oa10712 on 3/9/2018.
 */

public class DataStore {
    /**
     * Match key, team key, match object
     */
    //public static HashMap<String, JSONObject> matchData = new HashMap();
    public static Table<String, String, JSONObject> matchTable = HashBasedTable.create();
    public static String SERVER = "https://scout.tinybits.xyz";
    public static JSONObject config = new JSONObject();
    public static JSONObject teamData = new JSONObject();


    static void initLoad() {
        String configString = readFromFile("config.json");
        if (!Objects.equals(configString, "")) {
            try {
                config = new JSONObject(configString);
            } catch (JSONException e) {
                config = new JSONObject();
                e.printStackTrace();
            }
        } else {
            config = new JSONObject();
        }
        String teamString = readFromFile("teams.json");
        if (!Objects.equals(teamString, "")) {
            try {
                teamData = new JSONObject(teamString);
            } catch (JSONException e) {
                teamData = new JSONObject();
                e.printStackTrace();
            }
        }
        String matchString = readFromFile("match-data.json");
        if (!Objects.equals(matchString, "")) {
            try {
                JSONArray data = new JSONArray(matchString);

                for (int i = 0; i < data.length(); i++) {
                    try {
                        JSONObject match = data.getJSONObject(i);
                        JSONArray teams = match.getJSONArray("data");
                        for (int j = 0; j < teams.length(); j++) {
                            matchTable.put(match.getString("match"), teams.getJSONObject(j).getString("position"), teams.getJSONObject(j));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void manualRefresh(final SettingsFragment context) {

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                try {
                    uploadChanges();
                } catch (Exception ex) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                try {
                    final JsonObjectRequest teamRequest = new JsonObjectRequest
                            (Request.Method.GET, DataStore.SERVER + "/api/scout/teams?regional=" + config.getString("regional"), null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        JSONArray teams = response.getJSONArray("teams");


                                        for (int i = 0; i < teams.length(); i++) {
                                            JSONObject team = teams.getJSONObject(i);

                                            teamData.put(team.getString("key"), team);
                                        }
                                        writeToFile(teamData.toString(), "teams.json");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(context.getContext(), "Refreshed team data", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // As of f605da3 the following should work
                                    NetworkResponse response = error.networkResponse;
                                    if (error instanceof ServerError && response != null) {
                                        try {
                                            String res = new String(response.data,
                                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                            Log.e("Airror", res);
                                            // Now you can use any deserializer to make sense of data
                                            JSONObject obj = new JSONObject(res);
                                        } catch (UnsupportedEncodingException e1) {
                                            // Couldn't properly decode data to string
                                            e1.printStackTrace();
                                        } catch (JSONException e2) {
                                            // returned data is not JSONObject?
                                            e2.printStackTrace();
                                        }
                                    }
                                }
                            }) {
                        public Map<String, String> getHeaders() {

                            Map<String, String> mHeaders = new ArrayMap<String, String>();
                            try {
                                mHeaders.put("device-token", DataStore.config.getString("apiKey"));
                                mHeaders.put("Content-Type", "application/json");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return mHeaders;
                        }
                    };


                    final JsonObjectRequest matchRequest = new JsonObjectRequest
                            (Request.Method.GET, DataStore.SERVER + "/api/scout/matches?regional=" + config.getString("regional"), null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        Log.e("AIRROR", response.toString());
                                        JSONArray matches = response.getJSONArray("matches");


                                        for (int i = 0; i < matches.length(); i++) {
                                            JSONObject match = matches.getJSONObject(i);
                                            JSONArray teams = match.getJSONArray("data");
                                            for (int j = 0; j < teams.length(); j++) {
                                                JSONObject data = teams.getJSONObject(j);
                                                data.put("match", match.getString("match"));
                                                matchTable.put(match.getString("match"), teams.getJSONObject(j).getString("position"), data);
                                            }
                                        }
                                        Toast.makeText(context.getContext(), "Refreshed match data", Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, null) {
                        public Map<String, String> getHeaders() {

                            Map<String, String> mHeaders = new ArrayMap<String, String>();
                            try {
                                mHeaders.put("device-token", DataStore.config.getString("apiKey"));
                                mHeaders.put("Content-Type", "application/json");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return mHeaders;
                        }
                    };

                    JsonObjectRequest statusRequest = new JsonObjectRequest
                            (Request.Method.GET, DataStore.SERVER + "/api/device/status", null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        teamData = new JSONObject();
                                        matchTable = HashBasedTable.create();
                                        DataStore.config.put("regional", response.get("regional"));

                                        char[] def = response.getString("defaultDriverStation").toCharArray();
                                        int station = Integer.parseInt(String.valueOf(def[1])) - 1;
                                        if (def[0] == 'b') {
                                            station += 3;
                                        }
                                        DataStore.config.put("station", station);
                                        context.spinner.setSelection(DataStore.config.getInt("station"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    MainActivity.myRequestQueue.add(matchRequest);
                                    MainActivity.myRequestQueue.add(teamRequest);

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
                } catch (Exception ex) {

                }
            }

        }.execute();
    }

    private static void writeToFile(String data, String filename) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(MainActivity.instance.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private static String readFromFile(String filename) {

        String ret = "";

        try {
            InputStream inputStream = MainActivity.instance.openFileInput(filename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static void saveConfig() {
        writeToFile(config.toString(), "config.json");
    }

    public static String getTeamField(String teamKey, String key) {
        try {
            return teamData.getJSONObject(teamKey).getString(key);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    @NonNull
    public static JSONArray sortJsonArray(JSONArray array, final String field) throws JSONException {
        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            jsons.add(array.getJSONObject(i));
        }
        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                String lid = null;
                try {
                    lid = lhs.getString(field);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String rid = null;
                try {
                    rid = rhs.getString(field);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Here you could parse string id to integer and then compare.
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }

    public static ArrayList searchTeamMatches(String team) {
        ArrayList<String> ret = new ArrayList();
        for (String match : matchTable.rowKeySet()) {
            for (String pos : matchTable.columnKeySet()) {
                try {
                    if (matchTable.get(match, pos).getString("team").equals(team)) {
                        ret.add(match);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    public static void uploadChanges() {
        JSONArray toUpload = new JSONArray();

        for (final JSONObject item : matchTable.values()) {
            try {
                if (item.getBoolean("updated")) {
                    JSONObject match = new JSONObject(item.toString());
                    match.remove("updated");
                    match.remove("scouted");
                    match.remove("position");
                    match.put("type", "match");
                    match.put("regional", config.getString("regional"));
                    toUpload.put(match);
                }
            } catch (JSONException e) {
                //item is missing the 'updated' tag
            }
        }


        final JSONArray names = teamData.names();
        for (int i = 0; i < names.length(); i++) {
            try {
                if (teamData.getJSONObject(names.getString(i)).has("data")) {
                    final JSONObject team = teamData.getJSONObject(names.getString(i));
                    final JSONObject pit = team.getJSONObject("data");
                    Log.e("AIRROR4", "found pit data for " + names.getString(i) + ":" + pit.toString());
                    if (pit.has("updated") && pit.getBoolean("updated")) {

                        JSONObject pitUpload = new JSONObject(team.toString());
                        pitUpload.remove("updated");
                        pitUpload.remove("stats");
                        pitUpload.put("type", "team");
                        pitUpload.put("team", names.getString(i));

                        Log.e("Airror5", pitUpload.toString());
                        toUpload.put(pitUpload);
                    }else{
                        Log.e("AIRROR","not updated for " + names.getString(i));
                    }
                }
            } catch (JSONException e) {
                try {
                    System.out.print(names.getString(i));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }

        Log.e("AIRROR6", toUpload.toString());


        JsonArrayRequest uploadChanges = new JsonArrayRequest(Request.Method.POST, DataStore.SERVER + "/api/scout/upload", toUpload,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (final JSONObject item : matchTable.values()) {
                            try {
                                if (item.getBoolean("updated")) {
                                    item.remove("updated");
                                }
                            } catch (JSONException e) {
                                //item is missing the 'updated' tag
                            }
                        }

                        for (int i = 0; i < names.length(); i++) {
                            try {
                                if (teamData.getJSONObject(names.getString(i)).has("pit")) {
                                    JSONObject pit = teamData.getJSONObject(names.getString(i)).getJSONObject("pit");
                                    if (pit.getBoolean("updated")) {
                                        pit.remove("updated");
                                    }
                                }
                            } catch (JSONException e) {
                                //item is missing the 'updated' tag
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.e("Error: ", volleyError.getMessage());

            }
        }) {
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
        MainActivity.myRequestQueue.add(uploadChanges);
    }

    public static void autoSave() {
        System.out.println("Autosaving");
        saveConfig();
        writeToFile(teamData.toString(), "teams.json");
        JSONArray saveMatches = new JSONArray();

        for (String key : matchTable.rowKeySet()) {
            try {
                JSONObject match = new JSONObject();
                match.put("match", key);
                JSONArray data = new JSONArray();
                for (String position : matchTable.columnKeySet()) {
                    data.put(matchTable.get(key, position));
                }
                match.put("data", data);
                saveMatches.put(match);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        writeToFile(saveMatches.toString(), "match-data.json");

    }

    public static void updateTeamStats(String teamKey) {
        //TODO calculation for team stats changes every year

        System.out.println("Updating stats for " + teamKey);
        try {
            JSONObject teamStats = new JSONObject();

            double autoH = 0;
            double autoC = 0;
            double autoM = 0;
            double def = 0;
            double rct = 0;
            double rcm = 0;
            double rcb = 0;
            double rht = 0;
            double rhm = 0;
            double rhb = 0;
            double ph = 0;
            double pc = 0;
            double habs = 0;
            double habe = 0;
            double played = 0;
            for (JSONObject match : matchTable.values()) {
                if (match.getString("team").equalsIgnoreCase(teamKey)) {
                    if (match.getBoolean("scouted"))
                        played++;
                    JSONObject data = match.getJSONObject("data");
                    JSONObject auto = data.getJSONObject("auto");
                    JSONObject hab = data.getJSONObject("habitat");
                    JSONObject rocket = data.getJSONObject("rocket");
                    JSONObject pod = data.getJSONObject("pod");

                    if (auto.getBoolean("hatch")) {
                        autoH++;
                    }
                    if (auto.getBoolean("cargo")) {
                        autoC++;
                    }
                    if (auto.getBoolean("movement")) {
                        autoM++;
                    }
                    if (data.getBoolean("defense")) {
                        def++;
                    }
                    rct += rocket.getJSONArray("cargo").getInt(0);
                    rcm += rocket.getJSONArray("cargo").getInt(1);
                    rcb += rocket.getJSONArray("cargo").getInt(2);
                    rht += rocket.getJSONArray("hatch").getInt(0);
                    rhm += rocket.getJSONArray("hatch").getInt(1);
                    rhb += rocket.getJSONArray("hatch").getInt(2);

                    ph += pod.getInt("hatch");
                    pc += pod.getInt("cargo");

                    habs += hab.getInt("start");
                    habe += hab.getInt("end");
                }
            }
            teamStats.put("Played Matches", played);
            if (played == 0) {
                played = 1;// this is for /0 errors
            }
            teamStats.put("Auto Hatch %", autoH / played);
            teamStats.put("Auto Cargo %", autoC / played);
            teamStats.put("Auto Movement %", autoM / played);
            teamStats.put("Average Rct. Cargo Top", rct / played);
            teamStats.put("Average Rct. Cargo Mid", rcm / played);
            teamStats.put("Average Rct. Cargo Bot", rcb / played);
            teamStats.put("Average Pod Cargo", pc / played);
            teamStats.put("Average Rct. Hatch Top", rht / played);
            teamStats.put("Average Rct. Hatch Mid", rhm / played);
            teamStats.put("Average Rct. Hatch Bot", rhb / played);
            teamStats.put("Average Pod Hatches", ph / played);
            teamStats.put("Average Hab Start", habs / played);
            teamStats.put("Average Hab End", habe / played);
            teamStats.put("Defensive Games %", def / played);

            teamData.getJSONObject(teamKey).put("stats", teamStats);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static class TeamNumberComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int first = Integer.decode(o1.substring(3));
            int second = Integer.decode(o2.substring(3));
            return first - second;
        }
    }

}
