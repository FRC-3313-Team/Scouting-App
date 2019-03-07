package com.team3313.frcscoutingapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by oa10712 on 3/8/2018.
 */

public class RESTGetter {

    /**
     * Converts the contents of an InputStream to a String.
     */
    static String readStream(InputStream stream)
            throws IOException {
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static abstract class HttpSubmitTask extends AsyncTask<String, String, String> {

        String urlString;
        String bodyString;

        HttpSubmitTask(String url, String body) {
            urlString = url;
            bodyString = body;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                for (String param : params) {
                    String[] split = param.split(":");
                    if (split.length == 2) {
                        connection.setRequestProperty(split[0], split[1]);
                    }
                }

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

                writer.write(bodyString);
                writer.flush();
                String ret = connection.getResponseCode() + " " + connection.getResponseMessage();
                return ret;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return "";
        }

        @Override
        protected void onPostExecute(String r) {
            customEnd(r);
        }

        protected abstract void customEnd(String r);
    }

    public static abstract class HttpsSubmitTask extends AsyncTask<String, String, String> {

        String urlString;
        String bodyString;

        public HttpsSubmitTask(String url, String body) {
            urlString = url;
            bodyString = body;
        }

        @Override
        protected String doInBackground(String... params) {
            try {


                URL url = new URL(urlString);

                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

                for (String param : params) {
                    String[] split = param.split(":");
                    if (split.length == 2) {
                        urlConnection.setRequestProperty(split[0], split[1]);
                    }
                }
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));

                writer.write(bodyString);

                writer.flush();

                writer.close();

                out.close();

                urlConnection.connect();


                //String ret = urlConnection.getResponseCode() + " " + readStream(urlConnection.getInputStream());
                String ret = "";
                if (urlConnection.getResponseCode() == 201 || urlConnection.getResponseCode() == 200) {
                    ret = readStream(urlConnection.getInputStream());
                } else if (urlConnection.getResponseCode() == 209) {
                    ret = urlConnection.getResponseMessage();
                }
                return ret;

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
                return "fail: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String r) {
            customEnd(r);
        }

        protected abstract void customEnd(String r);
    }

    public abstract static class HttpsRequestTask extends AsyncTask<String, Void, JSONObject> {
        String urlString;

        HttpsRequestTask(String url) {
            urlString = url;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                URL url = new URL(urlString);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                for (String param : params) {
                    String[] split = param.split(":");
                    if (split.length == 2) {
                        connection.setRequestProperty(split[0], split[1]);
                    }
                }
                connection.connect();


                //HttpResponse response = httpclient.execute(httpget);

                if (connection.getResponseCode() == 200) {

                    String server_response = readStream(connection.getInputStream());
                    Log.i("Server response", server_response);
                    return new JSONObject(server_response);
                } else {
                    Log.i("Server response", "Failed to get server response");
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject r) {
            customEnd(r);
        }

        protected abstract void customEnd(JSONObject r);
    }

    public abstract static class HttpsRequestTaskArray extends AsyncTask<String, Void, JSONArray> {
        String urlString;

        public HttpsRequestTaskArray(String url) {
            urlString = url;
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            try {

                URL url = new URL(urlString);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                for (String param : params) {
                    String[] split = param.split(":");
                    if (split.length == 2) {
                        connection.setRequestProperty(split[0], split[1]);
                    }
                }
                connection.connect();


                //HttpResponse response = httpclient.execute(httpget);

                if (connection.getResponseCode() == 200) {

                    String server_response = readStream(connection.getInputStream());
                    Log.i("Server response", server_response);
                    return new JSONArray(server_response);
                } else {
                    Log.i("Server response", "Failed to get server response");
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return new JSONArray();
        }


        @Override
        protected void onPostExecute(JSONArray r) {
            customEnd(r);
        }

        protected abstract void customEnd(JSONArray r);
    }

    public abstract static class HttpRequestTask extends AsyncTask<String, Void, JSONObject> {
        String urlString;

        HttpRequestTask(String url) {
            urlString = url;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                for (String param : params) {
                    String[] split = param.split(":");
                    if (split.length == 2) {
                        connection.setRequestProperty(split[0], split[1]);
                    }
                }
                connection.connect();


                //HttpResponse response = httpclient.execute(httpget);

                if (connection.getResponseCode() == 200) {

                    String server_response = readStream(connection.getInputStream());
                    Log.i("Server response", server_response);
                    return new JSONObject(server_response);
                } else {
                    Log.i("Server response", "Failed to get server response");
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject r) {
            customEnd(r);
        }

        protected abstract void customEnd(JSONObject r);
    }

    public abstract static class HttpRequestTaskArray extends AsyncTask<String, Void, JSONArray> {
        String urlString;

        public HttpRequestTaskArray(String url) {
            urlString = url;
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            try {

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                for (String param : params) {
                    String[] split = param.split(":");
                    if (split.length == 2) {
                        connection.setRequestProperty(split[0], split[1]);
                    }
                }
                connection.connect();


                //HttpResponse response = httpclient.execute(httpget);

                if (connection.getResponseCode() == 200) {

                    String server_response = readStream(connection.getInputStream());
                    Log.i("Server response", server_response);
                    return new JSONArray(server_response);
                } else {
                    Log.i("Server response", "Failed to get server response");
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return new JSONArray();
        }


        @Override
        protected void onPostExecute(JSONArray r) {
            customEnd(r);
        }

        protected abstract void customEnd(JSONArray r);
    }
}
