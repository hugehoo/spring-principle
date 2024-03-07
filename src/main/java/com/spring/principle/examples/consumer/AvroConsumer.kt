package com.spring.principle.examples.consumer

import com.example.Customer
import com.spring.principle.examples.consumer.Constants.*
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig
import org.apache.kafka.clients.consumer.*
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
        configs[KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG] = true

        val consumer = KafkaConsumer<String, Customer>(configs)
        consumer.subscribe(Collections.singletonList(TOPIC_AVRO))
        while (true) {
            for (record: ConsumerRecord<String, Customer> in consumer.poll(Duration.ofSeconds(1))) {
                val name = record.value().name
                val favoriteColor = record.value().favoriteColor
                val favoriteNumber = record.value().favoriteNumber
                logger.info("name | {} | number | {} | color | {}", name, favoriteNumber, favoriteColor)
            }
        }
    }
}