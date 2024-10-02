export type Message = {
    topicPartition: {
        topic: String,
        partition: String
    }
    key: String,
    value: String
}