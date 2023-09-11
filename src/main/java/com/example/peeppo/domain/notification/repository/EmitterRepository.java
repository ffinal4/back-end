package com.example.peeppo.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepository {

    public final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    public SseEmitter save(String id, SseEmitter sseEmitter) {
        sseEmitters.put(id, sseEmitter);
        return sseEmitter;
    }

    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    public Map<String, SseEmitter> findAllStartWithByUserId(String userId) {
        return sseEmitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Object> findAllEventCacheStartWithUserId(String userId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void deleteAllStartWithUserId(String userId) {
        sseEmitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(userId)) {
                        sseEmitters.remove(key);
                    }
                }
        );
    }

    public void deleteByUserId(String userId) {
        sseEmitters.remove(userId);
    }

    public void deleteAllEventCacheStartWithUserId(String userId) {
        eventCache.forEach(
                (key, data) -> {
                    if (key.startsWith(userId)) {
                        eventCache.remove(key);
                    }
                }
        );
    }



}
//동시성을 고려하여 ConcurrentHashMap을 이용해 구현해주고 이를 저장하고 꺼내는 식의 방식을 진행한다.
//Emitter와 이벤트를 찾는 부분에 있어 startsWith을 사용하는 이유는 현재 저장하는데 있어
// 뒤에 구분자로 회원의 ID를 사용하기 때문에 해당 회원과 관련된 Emitter와 이벤트들을 찾아오는 것이다.
