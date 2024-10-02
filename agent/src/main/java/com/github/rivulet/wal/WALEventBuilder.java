package com.github.rivulet.wal;

import com.github.rivulet.types.WALEvent;

public class WALEventBuilder {
    private String key;
    private String value;
    // Replace with enum later
    private String eventType;

    public WALEventBuilder setKey(String key) {
        this.key=key;
        return this;
    }

    public WALEventBuilder setValue(String value) {
        this.value=value;
        return this;
    }

    public WALEventBuilder setEventType(String eventType) {
        this.eventType=eventType;
        return this;
    }

    public WALEvent build() {
        return new WALEvent(key, value, eventType);
    }
}
