package com.github.rivulet.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rivulet.metastore.MetadataService;
import com.github.rivulet.store.Deserializer;
import com.github.rivulet.store.S3Service;
import com.github.rivulet.store.Serializer;
import com.github.rivulet.types.Message;
import com.github.rivulet.types.ProducerMessage;
import com.github.rivulet.wal.WriteAheadLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProducerService {
    @Autowired
    private S3Service s3Service;
    @Autowired
    private Serializer serializer;
    @Autowired
    private Deserializer deserializer;
    @Autowired
    private MetadataService metastore;



    private final ConcurrentLinkedQueue<Message> buffer;
    private final AtomicInteger bufferSize;
    private int batchNumber=0;
    // To be replaced with mutex
    private boolean commitInProgress;
    public ProducerService() {
        this.buffer = new ConcurrentLinkedQueue<>();
        this.bufferSize = new AtomicInteger(0);
    }

    public boolean produceMessage(ProducerMessage producerMessage) {
        try {
            Message message = buildMessageFromProducerMessage(producerMessage);
            addMessageToBuffer(message);
            return true;
        }
        catch(Exception e) {
            return false;
        }

    }

    public Message buildMessageFromProducerMessage(ProducerMessage producerMessage) {
        MessageBuilder builder=new MessageBuilder();
        Message msg=builder.setTopic(producerMessage.getTopicPartition().topic)
                .setPartition(producerMessage.getTopicPartition().partition)
                .setKey(producerMessage.getKey())
                .setValue(producerMessage.getValue())
                .build();
        return msg;
    }

    // Adds message to in-memory buffer queue
    public void addMessageToBuffer(Message message) throws IllegalArgumentException {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        buffer.offer(message);
        bufferSize.incrementAndGet();
    }

    // Will commit metadata to MetaStore and upload messages to S3 Object store
    public void publishMessages() {
        List<Message> messagesToPublish = new ArrayList<>(bufferSize.get());
        Message message;
        while ((message = buffer.poll()) != null) {
            messagesToPublish.add(message);
            bufferSize.decrementAndGet();
        }

        if (messagesToPublish.size()>0) {
            String objectID=UUID.randomUUID().toString();
            String objectName=objectID+".dat";
            serializer.serializeToFile(messagesToPublish, objectName);
            boolean done=s3Service.uploadFile("dragonbucket", objectName, objectName);
            if (done) {
                metastore.mapMessagesToObject(messagesToPublish, objectID);
                // Create a File object representing the file
                File file = new File(objectName);

                // Check if the file exists and delete it
                if (file.exists()) {
                    boolean isDeleted = file.delete();
                    if (isDeleted) {
                        System.out.println("File deleted successfully.");
                    } else {
                        System.out.println("Failed to delete the file.");
                    }
                } else {
                    System.out.println("File does not exist.");
                }
            } else {
                System.out.println("Not able to commit to object store");
            }
        }

        System.out.println("Batch size ");
        System.out.println(messagesToPublish.size());
        batchNumber++;
        System.out.println(batchNumber);

    }

    public void clearBuffer() {
        buffer.clear();
        bufferSize.set(0);
    }

    // Method to get the current size of the buffer (for testing or monitoring)
    public int getBufferSize() {
        return bufferSize.get();
    }

    @Scheduled(fixedRate=5000)
    public void commitCurrentBuffer() {
        if (commitInProgress) {
            return;
        }
        commitInProgress=true;
        publishMessages();
        commitInProgress=false;
    }

}