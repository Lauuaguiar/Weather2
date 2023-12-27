package org.example.control;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.model.Location;
import org.example.model.Weather;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


public class OpenWeatherMapSupplier implements WeatherSupplier {
    private static String apiKey;

    public OpenWeatherMapSupplier(String apiKey) {
        OpenWeatherMapSupplier.apiKey = apiKey;
    }

    public static String getApi(Location location) {
        return String.format("https://api.openweathermap.org/data/2.5/forecast?lat=%f&lon=%f&appid=%s&units=metric",
                location.getLat(), location.getLon(), apiKey);
    }

    public static JsonObject getJson(String api) {
        try {
            Document result = Jsoup.connect(api).ignoreContentType(true).get();
            return JsonParser.parseString(result.text()).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Weather createWeather(JsonObject listItem, Instant predictionTs, Location location) {
        JsonObject main = listItem.getAsJsonObject("main");
        JsonObject clouds = listItem.getAsJsonObject("clouds");
        JsonObject wind = listItem.getAsJsonObject("wind");

        float temperature = main.get("temp").getAsFloat();
        float precipitation = listItem.get("pop").getAsFloat();
        int cloudCoverage = clouds.get("all").getAsInt();
        int humidity = main.get("humidity").getAsInt();
        float windSpeed = wind.get("speed").getAsFloat();
        String ss = "prediction-provider";
        return new Weather( ss, predictionTs, temperature, precipitation, windSpeed, humidity, cloudCoverage, location);
    }

    @Override
    public List<Weather> getWeather(Location location) {
        List<Weather> weatherList = new ArrayList<>();
        JsonObject jsonResult = getJson(getApi(location));
        JsonArray list = jsonResult.getAsJsonArray("list");

        for (int i = 0; i < list.size(); i++) {
            JsonObject listItem = list.get(i).getAsJsonObject();
            long timestamp = listItem.get("dt").getAsLong();
            Instant instant = Instant.ofEpochSecond(timestamp);
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

            if (localDateTime.getHour() == 12) {
                weatherList.add(createWeather(listItem, instant, location));
            }
        }
        return weatherList;
    }
}
