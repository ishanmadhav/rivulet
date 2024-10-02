package com.github.rivulet.consumer;

import com.github.rivulet.cache.CacheService;
import com.github.rivulet.metastore.MetadataService;
import com.github.rivulet.types.ConsumerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/consume")
public class ConsumerController {
    @Autowired
    private MetadataService metastore;
    @Autowired
    private ConsumerService consumerService;

    @GetMapping("")
    public ConsumerMessage consume(@RequestParam(name="topic", required = true) String topic, @RequestParam(name="partition", required = true) String partition, @RequestParam(name="offset", required = true) long offset) {
        System.out.println("Consume endpoint was hit");
        String topicPartition=topic+partition;
        long maxOffsetForTopicPartition=metastore.getTopicPartitionMaxOffset(topicPartition);
        System.out.println("Curr max offset is: "+maxOffsetForTopicPartition);
        if (offset>maxOffsetForTopicPartition) {
            if (offset > maxOffsetForTopicPartition) {
                // Return a 400 Bad Request error if the offset is invalid
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Offset exceeds max offset for the topic partition.");
            }
        }
        String topicPartitionOffsetKey=topic+partition+offset;
        System.out.println("TopicPartitionOffsetKey: "+topicPartitionOffsetKey);
        ConsumerMessage message=consumerService.getMessage(topicPartitionOffsetKey);
        return message;
    }

}
