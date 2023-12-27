package org.example.control;

import jakarta.jms.JMSException;
import org.example.control.EventStore;

import java.util.List;

public interface TopicSubscriber {
    public void subscribe(List<String> topics, EventStore eventStore) throws JMSException;
}
