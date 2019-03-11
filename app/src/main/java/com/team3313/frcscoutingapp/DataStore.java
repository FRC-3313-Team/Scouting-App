package com.team3313.frcscoutingapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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

    private static RESTGetter.HttpsRequestTaskArray updateTeamData() throws JSONException {
        return new RESTGetter.HttpsRequestTaskArray(SERVER + "/api/scout/teams", "{\"regional\":\"" + config.get("regional") + "\"}") {
            @Override
            protected void customEnd(JSONArray r) {
                try {

                    for (int i = 0; i < r.length(); i++) {
                        JSONObject team = r.getJSONObject(i);

                        teamData.put(team.getString("team"), team);
                    }
                    writeToFile(teamData.toString(), "teams.json");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private static RESTGetter.HttpsRequestTaskArray updateMatchData() throws JSONException {
        return new RESTGetter.HttpsRequestTaskArray(SERVER + "/api/scout/matches", "{\"regional\":\"" + config.get("regional") + "\"}") {
            @Override
            protected void customEnd(JSONArray r) {
                for (int i = 0; i < r.length(); i++) {
                    try {
                        JSONObject match = r.getJSONObject(i);
                        JSONArray teams = match.getJSONArray("data");
                        for (int j = 0; j < teams.length(); j++) {
                            JSONObject data = teams.getJSONObject(j);
                            data.put("match", match.getString("match"));
                            matchTable.put(match.getString("match"), teams.getJSONObject(j).getString("position"), data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

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

    public static void manualRefresh() {

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
                    updateTeamData().execute("device-token:" + config.getString("apiKey"));
                } catch (JSONException e) {
                    e.printStackTrace();
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
    private static JSONArray sortJsonArray(JSONArray array, final String field) throws JSONException {
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
                if (teamData.getJSONObject(names.getString(i)).has("pit")) {
                    final JSONObject pit = teamData.getJSONObject(names.getString(i)).getJSONObject("pit");

                    System.out.println("found pit data for " + names.getString(i) + ":" + pit.names());
                    if (pit.getBoolean("updated")) {
                        JSONObject pitUpload = new JSONObject(pit.toString());
                        pitUpload.remove("updated");
                        toUpload.put(pitUpload);
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

        final RESTGetter.HttpsSubmitTask t = new RESTGetter.HttpsSubmitTask(SERVER + "/api/scout/upload", toUpload.toString()) {
            @Override
            protected void customEnd(String r) {

                if (!r.startsWith("fail")) {

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
            }
        };
        t.execute();

    }

    public static void autoSave() {
        System.out.println("Autosaving");
        saveConfig();
        writeToFile(teamData.toString(), "teams.json");
        JSONArray saveMatches = new JSONArray();

        for (String key : matchTable.rowKeySet()) {
            try {
                JSONObject match = new JSONObject();
                match.put("match", match.getString("match"));
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
        //TODO calculation for team stats
        /*
        System.out.println("Updating stats for " + teamKey);
        try {
            JSONObject teamObject = teamData.getJSONObject(teamKey);

            double scale = 0;
            double swth = 0;
            double exchange = 0;
            int climb = 0;
            int cross = 0;
            int autoScale = 0;
            int autoSwitch = 0;
            double played = 0;
            for (JSONObject match : matchData.values()) {
                if (match.getString("team_key").equalsIgnoreCase(teamKey)) {

                    System.out.println(match.toString());
                    played++;
                    JSONObject auto = match.getJSONObject("auto");
                    JSONObject tele = match.getJSONObject("tele");

                    if (auto.getBoolean("passedLine")) {
                        cross++;
                    }
                    if (auto.getBoolean("switch")) {
                        autoSwitch++;
                    }
                    if (auto.getBoolean("scale")) {
                        autoScale++;
                    }
                    if (tele.getBoolean("climb")) {
                        climb++;
                    }
                    scale += tele.getInt("scale");
                    swth += tele.getInt("switch");
                    exchange += tele.getInt("exchange");
                }
            }
            teamObject.put("played", played);
            if (played == 0) {
                played = 1;// this is for /0 errors
            }
            teamObject.put("scale", scale / played);
            teamObject.put("switch", swth / played);
            teamObject.put("exchange", exchange / played);


            teamObject.put("climb", climb / played);
            teamObject.put("cross", cross / played);
            teamObject.put("autoScale", autoScale / played);
            teamObject.put("autoSwitch", autoSwitch / played);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
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
