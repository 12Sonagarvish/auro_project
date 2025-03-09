package com.garv.auro.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;
import java.util.Random;
@Slf4j

public class KafkaEventProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all"); // Ensure event delivery

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        Random random = new Random();

        while (true) {
            String event = "{\"id\": " + random.nextInt(10000) + ", \"temperature\": " + (20 + random.nextDouble() * 15) + ", \"humidity\": " + (30 + random.nextDouble() * 30) + "}";

            ProducerRecord<String, String> record = new ProducerRecord<>("events", event);

            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("Sent: " + event + " to partition " + metadata.partition());
                } else {
                    System.err.println("Error sending event: " + exception.getMessage());
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

