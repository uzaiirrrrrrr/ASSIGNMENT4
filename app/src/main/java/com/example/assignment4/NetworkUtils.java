package com.example.assignment4;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {

    private static final String API_URL = "https://jsonplaceholder.typicode.com/users";

    public interface ParkingDataListener {
        void onDataFetched(List<ParkingSlot> slots);

        void onError(String error);
    }

    public static void fetchParkingSlots(final ParkingDataListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(API_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    if (urlConnection.getResponseCode() != 200) {
                        if (listener != null)
                            listener.onError("Connection Failed: " + urlConnection.getResponseCode());
                        return;
                    }

                    reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }

                    List<ParkingSlot> slots = parseJSON(buffer.toString());
                    if (listener != null)
                        listener.onDataFetched(slots);

                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null)
                        listener.onError("Error: " + e.getMessage());
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                    try {
                        if (reader != null)
                            reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private static List<ParkingSlot> parseJSON(String jsonString) {
        List<ParkingSlot> slots = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject user = jsonArray.getJSONObject(i);

                String name = user.optString("name", "Unknown Slot");
                // Mocking location from address object
                String location = "Zone A";
                if (user.has("address")) {
                    JSONObject address = user.getJSONObject("address");
                    location = address.optString("city", "Unknown City");
                }

                // Mocking occupied status and price based on ID
                int id = user.optInt("id", 0);
                boolean isOccupied = (id % 2 == 0);
                double price = 5.0 + (id * 0.5);

                slots.add(new ParkingSlot(name, location, isOccupied, price));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return slots;
    }
}
