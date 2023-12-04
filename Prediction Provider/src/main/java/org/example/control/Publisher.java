package org.example.control;

import org.example.model.Weather;

public interface Publisher {
    void publishWeatherData(Weather weather);
}
