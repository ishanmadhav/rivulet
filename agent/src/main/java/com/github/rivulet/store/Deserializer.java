package com.github.rivulet.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rivulet.types.Message;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class Deserializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public List<Message> deserializeMessages(String filePath) {
        List<Message> messages = null;

        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            // Read the list of messages from the file
            messages = (List<Message>) ois.readObject();
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error deserializing object: " + e.getMessage());
        }

        return messages;
    }

    // Adjusted deserializeMessages method
    public List<Message> deserializeMessages(InputStream inputStream) {
        List<Message> messages = null;

        try (ObjectInputStream ois = new ObjectInputStream(inputStream)) {
            // Read the list of messages from the InputStream
            messages = (List<Message>) ois.readObject();
        } catch (IOException e) {
            System.err.println("Error reading from stream: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Error deserializing object: " + e.getMessage());
        }

        return messages;
    }

    public Message deserializeMessage(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Message) ois.readObject();
        }
    }

}
