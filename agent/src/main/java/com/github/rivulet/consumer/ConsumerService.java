package com.github.rivulet.consumer;

import com.github.rivulet.cache.CacheService;
import com.github.rivulet.types.ConsumerMessage;
import com.github.rivulet.types.Message;
import com.github.rivulet.types.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsumerService {
    @Autowired
    private CacheService cacheService;

    public ConsumerMessage getMessage(String topicPartitionOffsetKey) {
        String dataKey=topicPartitionOffsetKey;
        boolean found=cacheService.pollCache("Data_"+dataKey);
        System.out.println("Found variable in cache was: "+found);
        if (found) {
            Message result=cacheService.retrieveFromCache(dataKey);
            System.out.println("Value found for querying this topicpartitionOffset key is: "+result.toString());
            TopicPartition topicPartition=new TopicPartition(result.topic, result.partition);
            ConsumerMessage consumerMessage=new ConsumerMessage(result.key, result.value, topicPartition);
            return consumerMessage;
        } else {
            Message result=cacheService.retrieveFromS3AndMapToCache(dataKey);
            TopicPartition topicPartition=new TopicPartition(result.topic, result.partition);
            ConsumerMessage consumerMessage=new ConsumerMessage(result.key, result.value, topicPartition);
            return consumerMessage;
        }
    }

}
