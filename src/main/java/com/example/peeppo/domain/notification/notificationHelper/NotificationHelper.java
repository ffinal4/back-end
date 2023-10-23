package com.example.peeppo.domain.notification.notificationHelper;

import com.example.peeppo.domain.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationHelper {
    private final EmitterRepository emitterRepository;

    @Transactional
    public void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteByUserId(id);
            throw new RuntimeException("연결 오류!");
        }
    }
}
