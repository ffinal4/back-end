package com.example.peeppo.domain.user.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPageEvent {
    private final Boolean isCheck;
}
