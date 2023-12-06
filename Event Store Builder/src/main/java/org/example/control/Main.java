package org.example.control;

import jakarta.jms.JMSException;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) {
        String brokerURL = "tcp://localhost:61616";

        TopicSubscriber activeMQTopicSubscriber = new ActiveMQTopicSubscriber();
        try {
            BlockingQueue<String> eventsQueue = activeMQTopicSubscriber.subscribeToTopic(brokerURL);

            EventStore fileEventStoreBroker = new FileEventStoreBroker();

            while (true) {
                String eventInfo = eventsQueue.take();
                fileEventStoreBroker.processEvent(eventInfo);
            }
        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
