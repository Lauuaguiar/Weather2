package org.example.control;

import jakarta.jms.Message;

import java.util.List;

public interface TopicSubscriber {
    public List<Message> subscribe(List<String> topics) ;
}
