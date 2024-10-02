# rivulet
S3 and DragonFlyDB/Redis backed distributed messaging queue
This is a high performance distributed messaging queue implementation inspired by Warpstream. It uses S3 as its primary store for Kafka-like messages.
It supports high throughput via its distributed architecture, and persitence via direct-to-S3 storaage. Low latency required for messaging is maintained via the 
use of Redis like DragonFlyDB KV store. 



You will need Minio and DraagonFlyDB setup on your system (in local or as Docker containers or K8s pods) to be able to use this.
