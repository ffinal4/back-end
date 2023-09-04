package com.example.peeppo.domain.user.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Async
@Transactional
@Component
@RequiredArgsConstructor
public class UserPageEventListener {

    @EventListener
    public void handleUserPageEvent(UserPageEvent userPageEvent){


    }
}
