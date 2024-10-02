package com.github.rivulet.types;

public class TopicPartition {
    public String topic;
    public String partition;

    public TopicPartition(String topic, String partition) {
        this.topic=topic;
        this.partition=partition;
    }
}
