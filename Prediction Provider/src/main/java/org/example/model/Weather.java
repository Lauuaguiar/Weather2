package org.example.model;

import java.time.Instant;



public class Weather {

    private final Instant ts;
    private final String ss;
    private final Instant instant;

    private final float temperature;
    private final float precipitation;
    private final float wind;
    private final int humidity;
    private final int clouds;

    Location location;

    public Weather(Instant ts, String ss, Instant instant, float temperature, float precipitation, float wind, int humidity, int clouds, Location location) {
        this.ts = ts;
        this.ss = ss;
        this.instant = instant;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.wind = wind;
        this.humidity = humidity;
        this.clouds = clouds;
    }


    public Instant getInstant() {
        return instant;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getPrecipitation() {
        return precipitation;
    }

    public float getWind() {
        return wind;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getClouds() {
        return clouds;
    }

    public Location getLocation() {
        return location;
    }
}