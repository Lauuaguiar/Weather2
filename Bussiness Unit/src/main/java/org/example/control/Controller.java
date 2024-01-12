package org.example.control;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
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

            SqliteWeatherStore sqliteWeatherStore = new SqliteWeatherStore();
            for (Weather weather : weatherList) {
                sqliteWeatherStore.save(weather, weather.getLocation(), weather.getTs());
            }
            SqlitePOIStore sqlitePOIStore = new SqlitePOIStore();
            for (POI poi : poiList) {
                sqlitePOIStore.save(poi, poi.getLocation());
            }
        } else {
            System.out.println("No messages received. Invoking DatalakeProcessor...");
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