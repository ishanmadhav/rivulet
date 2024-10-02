package com.github.rivulet.types;

public class ConsumerMessage implements ProducerRecord {
    private TopicPartition topicPartition;
    private String key;
    private String value;

    public ConsumerMessage(String key, String value, TopicPartition topicPartition) {
        this.topicPartition = topicPartition;
        this.key = key;
        this.value = value;
    }

    @Override
    public TopicPartition getTopicPartition() {
        return topicPartition;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setTopicPartition(TopicPartition topicPartition) {
        this.topicPartition = topicPartition;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "topicPartition=" + topicPartition.topic + " " + topicPartition.partition +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}