package org.example.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.example.model.POI;
import org.example.model.Weather;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private List<String> topics;
    private String brokerURL;
    private String directoryPath;

    public void processMessages() throws JMSException {
        ActiveMQTopicSubscriber subscriber = new ActiveMQTopicSubscriber(brokerURL);
        List<Message> receivedMessages = subscriber.subscribe(topics);

        if (!receivedMessages.isEmpty()) {
            List<POI> poiList = new ArrayList<>();
            List<Weather> weatherList = new ArrayList<>();

            for (Message receivedMessage : receivedMessages) {
                try {
                    if (receivedMessage instanceof TextMessage textMessage) {
                        String messageText = textMessage.getText();
                        JsonObject jsonObject = JsonParser.parseString(messageText).getAsJsonObject();
                        String ssValue = jsonObject.get("ss").getAsString();

                        if (ssValue.equals("activity-provider")) {
                            POI poi = convertToPOI(String.valueOf(jsonObject));
                            poiList.add(poi);
                        } else {
                            Weather weather = convertToWeather(String.valueOf(jsonObject));
                            weatherList.add(weather);
                        }
                    }
                } catch (JMSException | JsonSyntaxException e) {
                    System.err.println("Error al procesar el mensaje: " + e.getMessage());
                }
            }

            SqliteWeatherStore sqliteWeatherStore = new SqliteWeatherStore();
            for (Weather weather : weatherList) {
                sqliteWeatherStore.save(weather, weather.getLocation(), weather.getTs());
            }

            SqlitePOIStore sqlitePOIStore = new SqlitePOIStore();
            for (POI poi : poiList) {
                sqlitePOIStore.save(poi, poi.getLocation());
            }
        } else {
            List<POI> poiList = DatalakeProcessor.readFilesInFolderForPOI(directoryPath + File.separator + "points.Of.Interest" + File.separator + "activity-provider");
            List<Weather> weatherList = DatalakeProcessor.readFilesInFolderForWeather(directoryPath + File.separator + "prediction.Weather" + File.separator + "prediction-provider");

            SqliteWeatherStore sqliteWeatherStore = new SqliteWeatherStore();
            for (Weather weather : weatherList) {
                sqliteWeatherStore.save(weather, weather.getLocation(), weather.getTs());
            }

            SqlitePOIStore sqlitePOIStore = new SqlitePOIStore();
            for (POI poi : poiList) {
                sqlitePOIStore.save(poi, poi.getLocation());
            }
        }
    }

    private POI convertToPOI(String messageText) {
        Gson gson = new Gson();
        return gson.fromJson(messageText, POI.class);
    }

    private Weather convertToWeather(String messageText) {
        Gson gson = new Gson();
        return gson.fromJson(messageText, Weather.class);
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public void setBrokerURL(String brokerURL) {
        this.brokerURL = brokerURL;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }
}
