package com.example.bhrdjawabarat;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class PrayerTimesAPIManager {

    private static final String TAG = PrayerTimesAPIManager.class.getSimpleName();
    private static final String API_BASE_URL = "https://muslimsalat.com/";
    private static final String API_KEY = "81be86d781828322e36c319e99e674a8";

    // Interface for the callback when prayer times are fetched
    public interface PrayerTimesListener {
        void onPrayerTimesFetched(PrayerTimes prayerTimes);
    }

    // Method to fetch prayer times for a given city
    public static void fetchPrayerTimes(String city, final PrayerTimesListener listener) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Build the API URL
                    String apiUrl = buildApiUrl(city);

                    URL url = new URL(apiUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        String response = convertStreamToString(in);

                        // Extract JSON data from JSONP response
                        String jsonData = extractJsonFromJsonp(response);

                        // Parse the JSON response
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONObject timings = jsonObject.getJSONArray("items").getJSONObject(0);

                        // Extract prayer times from the JSON response
                        PrayerTimes prayerTimes = new PrayerTimes(
                                timings.getString("fajr"),
                                timings.getString("shurooq"),
                                timings.getString("dhuhr"),
                                timings.getString("asr"),
                                timings.getString("maghrib"),
                                timings.getString("isha")
                        );

                        // Notify the listener with the fetched prayer times
                        listener.onPrayerTimesFetched(prayerTimes);
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (IOException | JSONException e) {
                    // Handle errors
                    Log.e(TAG, "Error fetching prayer times: " + e.getMessage());
                }
            }
        });
    }

    // Helper method to convert InputStream to String
    private static String convertStreamToString(InputStream is) {
        Scanner scanner = new Scanner(is).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    private static String encodeCity(String city) {
        try {
            return URLEncoder.encode(city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return city;
        }
    }

    private static String buildApiUrl(String city) {
        // Encode the city to ensure it's URL-safe
        String encodedCity = encodeCity(city);

        // Construct the API URL using the city
        return "https://muslimsalat.com/" + encodedCity + ".json";
    }


    // Helper method to extract JSON data from JSONP response
    private static String extractJsonFromJsonp(String jsonpResponse) {
        // Extract the JSON data by removing the function call
        int startIndex = jsonpResponse.indexOf('(');
        int endIndex = jsonpResponse.lastIndexOf(')');
        if (startIndex != -1 && endIndex != -1) {
            return jsonpResponse.substring(startIndex + 1, endIndex);
        }
        return jsonpResponse;
    }
}
