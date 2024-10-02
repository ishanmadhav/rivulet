package com.github.rivulet.wal;

import com.github.rivulet.store.Serializer;
import com.github.rivulet.types.WALEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class WriteAheadLogService {
    @Autowired
    private Serializer serializer;


    private List<String> walObjectNames;

    public void appendToWriteAheadLog(List<WALEvent> walList) {
        Instant currentTime=Instant.now();
        serializer.serializeToFile(walList, currentTime.toString()+".dat");
        walObjectNames.add(currentTime.toString()+".dat");
    }
}
