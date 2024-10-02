package com.github.rivulet.types;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    public String topic;
    public String partition;
    public String key;
    public String value;
    public String id;

    public Message(String topic, String partition, String key, String value) {
        this.topic = topic;
        this.partition = partition;
        this.key = key;  // Corrected key assignment
        this.value = value;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Message{" +
                "topic='" + topic + '\'' +
                ", partition='" + partition + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
