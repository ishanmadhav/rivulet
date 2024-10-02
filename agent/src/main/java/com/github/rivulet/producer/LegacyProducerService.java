package com.github.rivulet.producer;

import com.github.rivulet.store.S3Service;
import com.github.rivulet.types.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LegacyProducerService {
    @Autowired
    private S3Service s3Service;

    private final List<Message> buffer;
    private final Lock bufferLock;

    public LegacyProducerService() {
        this.buffer = new ArrayList<>();
        this.bufferLock = new ReentrantLock();
    }

    public boolean produceMessage() {
        return s3Service.bucketExists("dragonbucket2");
    }

    // Adds message to in-memory buffer queue
    public void addMessageToBuffer(Message message) {
        bufferLock.lock();
        try {
            buffer.add(message);
        } finally {
            bufferLock.unlock();
        }
    }

    // Will commit metadata to MetaStore and upload messages to S3 Object store
    public void publishMessages() {
        List<Message> messagesToPublish;
        bufferLock.lock();
        try {
            messagesToPublish = new ArrayList<>(buffer);
            buffer.clear();
        } finally {
            bufferLock.unlock();
        }

        // Perform the actual publishing outside the lock
        // This is just a placeholder - implement your actual publishing logic here
        for (Message message : messagesToPublish) {
            // Publish message to S3 and commit metadata
            System.out.println("Publishing message: " + message);
        }
    }

    public void clearBuffer() {
        bufferLock.lock();
        try {
            buffer.clear();
        } finally {
            bufferLock.unlock();
        }
    }

    // Method to get the current size of the buffer (for testing or monitoring)
    public int getBufferSize() {
        bufferLock.lock();
        try {
            return buffer.size();
        } finally {
            bufferLock.unlock();
        }
    }
}