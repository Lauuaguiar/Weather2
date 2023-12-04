package org.example.model;

import java.time.Instant;



public class Weather {

    private final Instant ts;
    private final String ss;
    private final Instant predictionTime;

    private final float temperature;
    private final float precipitation;
    private final float wind;
    private final int humidity;
    private final int clouds;

    private final Location location;

    public Weather(Instant ts, String ss, Instant predictionTime, float temperature, float precipitation, float wind, int humidity, int clouds, Location location) {
        this.ts = ts;
        this.ss = ss;
        this.predictionTime = predictionTime;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.wind = wind;
        this.humidity = humidity;
        this.clouds = clouds;
        this.location = location;
    }
}
