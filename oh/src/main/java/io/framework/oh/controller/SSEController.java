package io.framework.oh.controller;

import io.framework.oh.service.SSEService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class SSEController {

    private final SSEService sseService;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect() {
        String clientId = UUID.randomUUID().toString();
        log.info("New SSE connection established for client: {}", clientId);
        return sseService.createEmitter(clientId);
    }

    @PostMapping("/send/{clientId}")
    public void sendEvent(
            @PathVariable String clientId,
            @RequestParam String eventName,
            @RequestBody Object data) {
        log.info("Sending event '{}' to client: {}", eventName, clientId);
        sseService.sendEvent(clientId, eventName, data);
    }

    @PostMapping("/broadcast")
    public void broadcastEvent(
            @RequestParam String eventName,
            @RequestBody Object data) {
        log.info("Broadcasting event '{}' to all clients", eventName);
        sseService.broadcastEvent(eventName, data);
    }
} 