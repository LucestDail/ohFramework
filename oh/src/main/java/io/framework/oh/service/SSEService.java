package io.framework.oh.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SSEService {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(String clientId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        emitter.onCompletion(() -> {
            log.info("SSE completed for client: {}", clientId);
            emitters.remove(clientId);
        });
        
        emitter.onTimeout(() -> {
            log.info("SSE timeout for client: {}", clientId);
            emitters.remove(clientId);
        });
        
        emitter.onError(e -> {
            log.error("SSE error for client: {}", clientId, e);
            emitters.remove(clientId);
        });

        emitters.put(clientId, emitter);
        return emitter;
    }

    public void sendEvent(String clientId, String eventName, Object data) {
        SseEmitter emitter = emitters.get(clientId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data));
            } catch (IOException e) {
                log.error("Error sending SSE event to client: {}", clientId, e);
                emitters.remove(clientId);
            }
        }
    }

    public void broadcastEvent(String eventName, Object data) {
        emitters.forEach((clientId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data));
            } catch (IOException e) {
                log.error("Error broadcasting SSE event to client: {}", clientId, e);
                emitters.remove(clientId);
            }
        });
    }
} 