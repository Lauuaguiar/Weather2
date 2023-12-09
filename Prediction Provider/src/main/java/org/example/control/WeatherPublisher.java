package org.example.control;

import org.example.model.Weather;

public interface WeatherPublisher {
    void publishWeatherData(Weather weather);
}
