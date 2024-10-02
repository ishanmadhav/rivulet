package com.github.rivulet.producer;

import com.github.rivulet.types.Message;

public class MessageBuilder {
    public String topic;
    public String partition;
    public String key;
    public String value;

    public MessageBuilder setTopic(String topic) {
        this.topic=topic;
        return this;
    }

    public MessageBuilder setPartition(String partition) {
        this.partition=partition;
        return this;
    }

    public MessageBuilder setKey(String key) {
        this.key=key;
        return this;
    }

    public MessageBuilder setValue(String value) {
        this.value=value;
        return this;
    }

    public Message build() {
        return new Message(
                this.topic, this.partition, this.key, this.value
        );
    }

}
