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
        this.apiKey = apiKey;
    }

    public static String getApi(Location location) {
        return "https://api.openweathermap.org/data/2.5/forecast?lat=" + location.getLat() +
                "&lon=" + location.getLon() + "&appid=" + apiKey + "&units=metric";
    }

    public static JsonObject getJson(String api) throws IOException {
        Document result = Jsoup.connect(api).ignoreContentType(true).get();
        JsonParser parser = new JsonParser();
        return parser.parse(result.text()).getAsJsonObject();
    }

    @Override
    public List<Weather> getWeather(Location location) {
        List<Weather> weatherList = new ArrayList<>();
        try {
            String apiUrl = getApi(location);
            JsonObject jsonResult = getJson(apiUrl);
            JsonArray list = jsonResult.getAsJsonArray("list");

            for (int i = 0; i < list.size(); i++) {
                JsonObject listItem = list.get(i).getAsJsonObject();
                long timestamp = listItem.get("dt").getAsLong();
                Instant instant = Instant.ofEpochSecond(timestamp);

                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

                if (localDateTime.getHour() == 12) {
                    float temperature = listItem.getAsJsonObject("main").get("temp").getAsFloat();
                    float precipitation = listItem.get("pop").getAsFloat();
                    int clouds = listItem.getAsJsonObject("clouds").get("all").getAsInt();
                    int humidity = listItem.getAsJsonObject("main").get("humidity").getAsInt();
                    float windSpeed = listItem.getAsJsonObject("wind").get("speed").getAsFloat();
                    Weather weather = new Weather(instant, temperature, precipitation, windSpeed, humidity, clouds, location);
                    weatherList.add(weather);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return weatherList;

    }



}
