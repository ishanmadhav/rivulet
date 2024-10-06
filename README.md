Here's the formatted version of your README.md file, with proper markdown syntax for GitHub:

# rivulet

S3 and DragonFlyDB/Redis backed distributed messaging queue

This is a high-performance distributed messaging queue implementation inspired by Warpstream. It uses S3 as its primary store for Kafka-like messages.

It supports high throughput via its distributed architecture, and persistence via direct-to-S3 storage. Low latency required for messaging is maintained via the use of Redis-like DragonFlyDB KV store. 

You will need Minio and DragonFlyDB setup on your system (in local or as Docker containers or K8s pods) to be able to use this. 

Once the DragonFlyDB and Minio have been setup and are running, just run `docker compose up` from the root of the directory to start a distributed messaging queue with 2 agents and 1 service. Agents are independent of each other and can be added as per the desired load. You can use any of the clients to produce or consume messages. Currently, the Go client is the more complete implementation.

## Setup Instructions

### Setting up Minio

Run this in your GNU terminal:

```bash
mkdir -p ~/minio/data

docker run \
   -p 9000:9000 \
   -p 9001:9001 \
   --name minio \
   -v ~/minio/data:/data \
   -e "MINIO_ROOT_USER=ROOTNAME" \
   -e "MINIO_ROOT_PASSWORD=CHANGEME123" \
   quay.io/minio/minio server /data --console-address ":9001"
```

Create a bucket with the name 'dragonbucket'.

### Setting up DragonFlyDB

You can also use Redis instead of DragonFlyDB as it's compatible.

```bash
docker run --network=host --ulimit memlock=-1 docker.dragonflydb.io/dragonflydb/dragonfly
```

### Running the distribtued messaing queue
Run the following in any termianl:
```bash
docker compose up
```
