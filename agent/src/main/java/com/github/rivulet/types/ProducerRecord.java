package com.github.rivulet.types;

public interface ProducerRecord {
    TopicPartition getTopicPartition();
    String getKey();
    String getValue();

    void setTopicPartition(TopicPartition topicPartition);
    void setKey(String key);
    void setValue(String value);
}