package com.spring.principle.examples.consumer

import com.spring.principle.examples.consumer.Constants.*
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.*
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*

class AvroConsumer {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AvroConsumer::class.java)
    }

    fun consume() {
        val configs = Properties()
        configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
        configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
        configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = KafkaAvroDeserializer::class.java.name
        configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
        configs["schema.registry.url"] = SCHEMA_REGISTRY_URL

        val consumer = KafkaConsumer<String, GenericRecord>(configs)
        consumer.subscribe(Collections.singletonList(TOPIC_AVRO))

        while (true) {
            val records: ConsumerRecords<String, GenericRecord> = consumer.poll(Duration.ofSeconds(1))
            for (record: ConsumerRecord<String, GenericRecord> in records) {
                logger.info("{} | {} | record | {} | \n", record.partition(), record.timestamp(), record.value())
            }

            // background 에서 OffsetCommitCallback() 진행
            consumer.commitAsync(OffsetCommitCallback { offsets: Map<TopicPartition, OffsetAndMetadata>, e: Exception? ->
                if (e != null) {
                    System.err.println("Commit Failed \n")
                } else {
                    System.out.printf("Commit Succeeded $offsets \n")
                }
                if (e != null) {
                    logger.error("Commit failed for offsets {} \n", offsets, e)
                }
            })
        }
    }
}