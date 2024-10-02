package com.github.rivulet.metastore;

import com.github.rivulet.store.Serializer;
import com.github.rivulet.types.Message;
import com.github.rivulet.types.WALEvent;
import com.github.rivulet.wal.WALEventBuilder;
import com.github.rivulet.wal.WriteAheadLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class MetadataService {
    private JedisPool pool;
    @Autowired
    private WriteAheadLogService wal;
    @Autowired
    private Serializer serializer;

    public MetadataService() {
        this.pool = new JedisPool("localhost", 6379);
    }

    public void set(String key, String val) {
        try {
            Jedis jedis = pool.getResource();
            jedis.set(key, val);
            jedis.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public String get(String key) {
        try {
            Jedis jedis = pool.getResource();
            String val=jedis.get(key);
            jedis.close();
            return val;
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

    public void delete(String key) {
        try{
            Jedis jedis = pool.getResource();
            jedis.del(key);
            jedis.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public long getTopicPartitionMaxOffset(String topicPartition) {
        System.out.println("Checking offset max");
        Jedis jedis=pool.getResource();
        long maxOffset=Long.parseLong(jedis.get(topicPartition));
        jedis.close();
        return maxOffset;


    }


    // Assigns offsets at commit time to all messages in the commited buffer
    // Maps the messages to the commited object in the metadata store
    public void mapMessagesToObject(List<Message> messages, String objectID) {
        Jedis jedis=pool.getResource();
        List < WALEvent> walList=new ArrayList<>(messages.size());
        int i=0;
        for (Message message:messages) {
            // Gets the topic partition to check
            String topicPartition=message.topic+message.partition;
            // increments and fetches the current offset for the topic partition key
            long offset=jedis.incr(topicPartition);
            String topicPartitionOffsetKey=topicPartition+offset;
            jedis.set(topicPartitionOffsetKey, objectID);
            jedis.set("Index_"+objectID+i, topicPartitionOffsetKey);
            i++;
            WALEventBuilder builder=new WALEventBuilder();
            WALEvent logEvent=builder.setKey(topicPartitionOffsetKey)
                    .setValue(Long.toString(offset))
                    .setEventType("MESSAGE_OBJECT_MAP")
                    .build();
            walList.add(logEvent);
        }
        jedis.close();

    }

    public String getMappedObject(String topicPartitionOffsetKey) {
        Jedis jedis=pool.getResource();
        boolean found=jedis.exists(topicPartitionOffsetKey);
        System.out.println("Mapepd object exsits");
        if (!found) {
            return "";
        }
        String objectID=jedis.get(topicPartitionOffsetKey);
        System.out.println("Object ID of the mapped object "+objectID);
        jedis.close();
        return objectID;
    }

    public String getTopicPartitionOffsetKey(String indexKey) {
        Jedis jedis=pool.getResource();
        String topicPartitionOffsetKey=jedis.get(indexKey);
        jedis.close();
        return topicPartitionOffsetKey;
    }

    public void flushDB() {
        Jedis jedis=pool.getResource();
        try{
            jedis.flushDB();
            System.out.println("DB flushed");
            jedis.close();
        }
        catch(Exception e) {
            System.out.println(e.toString());
            jedis.close();
        }
    }
}
