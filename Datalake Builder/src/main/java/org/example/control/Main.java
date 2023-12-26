package org.example.control;

import jakarta.jms.JMSException;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        try {
            EventStore eventStore = new FileEventStoreBroker(new File(args[0]));
            TopicSubscriber activeMQTopicSubscriber = new ActiveMQTopicSubscriber(args[0]);
            activeMQTopicSubscriber.subscribe("prediction.Weather", eventStore);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
