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
            System.out.println("Mensajes recibidos: " + receivedMessages.size());
            for (int i = 0; i < receivedMessages.size(); i++) {
                try {
                    if (receivedMessages.get(i) instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) receivedMessages.get(i);
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

                        System.out.println("Mensaje " + (i + 1) + " procesado correctamente.");
                    } else {
                        System.out.println("Mensaje " + (i + 1) + " no es un mensaje de texto.");
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
            System.out.println("No se recibió ningún mensaje. Invocando DatalakeProcessor...");
            System.out.println("Reading POI files from: " + directoryPath + File.separator + "points.Of.Interest" + File.separator + "activity-provider");
            System.out.println("Reading Weather files from: " + directoryPath + File.separator + "prediction.Weather" + File.separator + "prediction-provider");
            DatalakeProcessor processor = new DatalakeProcessor();
            List<POI> poiList = processor.readFilesInFolderForPOI(directoryPath + File.separator + "points.Of.Interest" + File.separator + "activity-provider");
            List<Weather> weatherList = processor.readFilesInFolderForWeather(directoryPath + File.separator + "prediction.Weather" + File.separator + "prediction-provider");
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
