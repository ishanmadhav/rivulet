package com.github.rivulet.store;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rivulet.types.Message;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

@Component
public class Serializer {
//    public String serializeToFile(List<Message> messagesToPublish, String fileName) {
//        // Serialize the messagesToPublish list to a binary file
//        try (FileOutputStream fos = new FileOutputStream(fileName);
//             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
//            oos.writeObject(messagesToPublish);
//            return fileName;
//        } catch (IOException e) {
//            System.err.println("Error serializing messages to file: " + e.getMessage());
//            return "";
//        }
//    }


    private final ObjectMapper objectMapper = new ObjectMapper();
public <T> String serializeToFile(List<T> messagesToPublish, String fileName) {
    // Serialize the messagesToPublish list to a binary file
    try (FileOutputStream fos = new FileOutputStream(fileName);
         ObjectOutputStream oos = new ObjectOutputStream(fos)) {
        oos.writeObject(messagesToPublish);
        return fileName;
    } catch (IOException e) {
        System.err.println("Error serializing messages to file: " + e.getMessage());
        return "";
    }
}

public byte[] serialize(Message message) throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream(bos)) {
        oos.writeObject(message);
        return bos.toByteArray();
    }
}

}
