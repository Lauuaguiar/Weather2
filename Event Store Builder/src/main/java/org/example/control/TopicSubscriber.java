package org.example.control;

import jakarta.jms.JMSException;
import java.util.concurrent.BlockingQueue;

public interface TopicSubscriber {
    BlockingQueue<String> subscribeToTopic(String brokerURL) throws JMSException;
}
