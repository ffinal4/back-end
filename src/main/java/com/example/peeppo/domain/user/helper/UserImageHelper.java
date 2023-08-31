package com.example.peeppo.domain.user.helper;

import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.image.entity.UserImage;
import com.example.peeppo.domain.image.repository.UserImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserImageHelper {
    private final UserImageRepository userImageRepository;

    @Transactional
    public void userImageDelete(UserImage image){
        userImageRepository.delete(image);
    }
}
