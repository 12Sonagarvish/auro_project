package com.garv.auro.controller;

import com.garv.auro.model.Event;
import com.garv.auro.repository.EventRepository;
import com.garv.auro.service.KafkaConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventRepository eventRepository;
    private final KafkaConsumerService kafkaConsumerService;

    @GetMapping("/all")
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @PostMapping
    public String createEvent(@RequestParam String eventType, @RequestParam String eventData) {
        return "Event published!";
    }

    @GetMapping
    public List<String> getProcessedEvents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        List<String> allEvents = kafkaConsumerService.getProcessedEvents();
        int fromIndex = Math.min(page * size, allEvents.size());
        int toIndex = Math.min(fromIndex + size, allEvents.size());
        log.info("Returning {} events from index {} to {}", size, fromIndex, toIndex);
        return allEvents.subList(fromIndex, toIndex);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable String id) {
        Optional<Event> event = eventRepository.findById(Long.valueOf(id));
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ExceptionHandler(Exception.class)
    public String handleError(Exception e) {
        log.error("Error in API", e);
        return "Internal Server Error";
    }
}

