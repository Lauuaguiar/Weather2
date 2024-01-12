package org.example.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.model.Location;
import org.example.model.POI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OpenTripMapSupplier implements POISupplier {
    private static String apiKey;

    public OpenTripMapSupplier(String apiKey) {
        OpenTripMapSupplier.apiKey = apiKey;
    }

    public static String getApi(double latitude, double longitude) {
        return String.format("https://api.opentripmap.com/0.1/en/places/radius?radius=1000&lon=%f&lat=%f&kinds=interesting_places&apikey=%s",
                longitude, latitude, apiKey);
    }

    public static JsonObject getJson(String api) {
        try {
            URL url = new URL(api);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return JsonParser.parseString(response.toString()).getAsJsonObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private POI createPOI(JsonObject placeObj, Location location) {
        String name = placeObj.getAsJsonObject("properties").get("name").getAsString();
        String kinds = placeObj.getAsJsonObject("properties").get("kinds").getAsString();
        JsonObject geometry = placeObj.getAsJsonObject("geometry");
        double lat = geometry.getAsJsonArray("coordinates").get(1).getAsDouble();
        double lon = geometry.getAsJsonArray("coordinates").get(0).getAsDouble();
        String ss = "activity-provider";
        return new POI(ss, name, kinds, lat, lon, location);
    }

    @Override
    public List<POI> getPOIs(Location location) {
        List<POI> poiList = new ArrayList<>();
        try {
            Locale.setDefault(Locale.ENGLISH);
            JsonObject jsonResponse = getJson(getApi(location.getLat(), location.getLon()));
            JsonArray places = jsonResponse.getAsJsonArray("features");

            int count = 0;
            for (JsonElement place : places) {
                if (count >= 5) {
                    break;
                }

                JsonObject placeObj = place.getAsJsonObject();
                poiList.add(createPOI(placeObj, location));
                count++;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return poiList;
    }
}
