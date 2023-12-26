package org.example.control;

import jakarta.jms.JMSException;

import java.util.concurrent.BlockingQueue;

public interface TopicSubscriber {
    void subscribe(String topic, EventStore eventStore) throws JMSException;
}
