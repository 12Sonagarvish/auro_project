package com.garv.auro.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class KafkaConsumerService {
    private final List<String> eventStorage = new ArrayList<>();

    @KafkaListener(topics = "events", groupId = "event-api-group")
    public void consume(ConsumerRecord<String, String> record) {
        eventStorage.add(record.value());
    }

    public List<String> getProcessedEvents() {
        return eventStorage;
    }
}
