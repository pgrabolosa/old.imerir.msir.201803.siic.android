package com.imerir.pgrabolosa.siicdemo.Model;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class DataManager {
    public static class NotReadyException extends Exception { }

    /// Singleton instance
    public final static DataManager shared = new DataManager();

    /// Whenever
    public final ArrayList<Converter.Conversion> conversions = new ArrayList<>();

    public void loadRates() {
        new AsyncTask<Void, Void, Float>() {
            @Override
            protected Float doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://www.floatrates.com/daily/usd.json");
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setUseCaches(false);

                    connection.connect(); // send the request
                    if (connection.getResponseCode() != 200) {
                        return null;
                    }

                    // fetch the raw json text
                    String rawJson;
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        rawJson = br.lines().collect(Collectors.joining());
                        br.close();
                    }

                    // try to read json
                    JSONObject json = new JSONObject(rawJson);
                    return Float.valueOf((float)json.getJSONObject("eur").getDouble("rate"));

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Float v) {
                super.onPostExecute(v);
                if (v != null) {
                    float rate = v.floatValue();
                    eurConverter = new LinearConverter(rate);
                }
            }
        }.execute();
    }

    private Converter eurConverter;

    public float convertToEur(float value, boolean saveResult) throws NotReadyException {
        if (eurConverter == null) {
            // if this is thrown, make sure loadRates() was called and succeeded
            throw new NotReadyException();
        }

        Converter.Conversion result = eurConverter.convert(value);

        if (saveResult) {
            conversions.add(result);
        }

        return result.outputValue;
    }

}
