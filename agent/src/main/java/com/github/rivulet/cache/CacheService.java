package com.github.rivulet.cache;

import com.github.rivulet.metastore.MetadataService;
import com.github.rivulet.store.Deserializer;
import com.github.rivulet.store.S3Service;
import com.github.rivulet.store.Serializer;
import com.github.rivulet.types.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class CacheService {
    @Autowired
    private S3Service s3Service;
    @Autowired
    private MetadataService metastore;
    @Autowired
    private Serializer serializer;
    @Autowired
    private Deserializer deserializer;

    private JedisPool pool;

    public CacheService() {
        this.pool = new JedisPool("localhost", 6379);
    }

    public boolean pollCache(String key) {
        Jedis jedis=pool.getResource();
        boolean found=jedis.exists(key);
        jedis.close();
        return found;
    }

    public Message retrieveFromCache(String key) {
        Jedis jedis=pool.getResource();
        key="Data_"+key;
        byte[] val=jedis.get(key.getBytes());
        System.out.println(Arrays.toString(val));
        try {
            Message message = deserializer.deserializeMessage(val);
            jedis.close();
            return message;
        }
        catch(Exception e) {
            System.out.println(e);
            jedis.close();
            return null;
        }
    }

    public Message retrieveFromS3AndMapToCache(String key) {
        Jedis jedis=pool.getResource();
        String objectID=metastore.getMappedObject(key);
        if (objectID.isEmpty()) {
            System.out.println("Object ID is null for "+key);
            return null;
        }
        String objectName=objectID+".dat";
        System.out.print("Object name is "+objectName);
        List<Message> messages=s3Service.downloadFile("dragonbucket", objectName);
        Message resultMessage=null;
        for (int i=0;i<messages.size();i++) {
            try {
                String indexKey = "Index_" + objectID + i;
                System.out.println("IndexKey is "+indexKey);
                String topicPartitionOffsetKey = metastore.getTopicPartitionOffsetKey(indexKey);
                System.out.println("Topic Partition OffsetKey for this IndexKey is "+topicPartitionOffsetKey);
                String dataKey="Data_" + topicPartitionOffsetKey;
                if (Objects.equals(topicPartitionOffsetKey, key)) {
                    resultMessage=messages.get(i);
                }
                jedis.set(dataKey.getBytes(), serializer.serialize(messages.get(i)));
            }
            catch(Exception e) {
                System.out.println(e);
            }
        }
        System.out.println("Result Message is "+resultMessage);
        jedis.close();
        return resultMessage;



    }
}
