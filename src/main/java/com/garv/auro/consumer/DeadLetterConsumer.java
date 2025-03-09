package com.garv.auro.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeadLetterConsumer {

    @KafkaListener(topics = "events.DLQ", groupId = "event-group")
    public void consumeDeadLetterEvent(String message) {
        log.error("Received message in DLQ: {}", message);
    }
}

