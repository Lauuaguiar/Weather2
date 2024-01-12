package org.example.control;

import jakarta.jms.JMSException;

import java.util.Arrays;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        try {
            EventStore eventStore = new FileEventStoreBroker(args[0]);
            List<String> topics = Arrays.asList("prediction.Weather", "points.Of.Interest");
            TopicSubscriber activeMQTopicSubscriber = new ActiveMQTopicSubscriber("tcp://localhost:61616");
            activeMQTopicSubscriber.subscribe(topics, eventStore);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
