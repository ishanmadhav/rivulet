package com.github.rivulet.types;

import java.io.Serializable;
import java.time.Instant;


public class WALEvent implements Serializable {
    private Instant timestamp;
    private String key;
    private String value;
    // Replace with enum later
    private String eventType;

    public WALEvent(String key, String value, String eventType) {
        this.timestamp=Instant.now();
        this.key=key;
        this.value=value;
        this.eventType=eventType;
    }
}
