package com.example.peeppo.domain.image.helper;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.peeppo.domain.goods.entity.Goods;
import com.example.peeppo.domain.image.entity.Image;
import com.example.peeppo.domain.image.entity.UserImage;
import com.example.peeppo.domain.image.repository.ImageRepository;
import com.example.peeppo.domain.image.repository.UserImageRepository;
import com.example.peeppo.domain.user.entity.User;
import com.example.peeppo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageHelper {
    private final ImageRepository imageRepository;
    private final UserImageRepository userImageRepository;

    @Transactional
    public List<Image> saveImagesToS3AndRepository(List<MultipartFile> images, AmazonS3 amazonS3, String bucket, Goods goods) {
        for (MultipartFile image : images) {
            if (!validateFile(image)) {
                throw new IllegalStateException("파일 검증 실패");
            }
        }

        List<Image> newImageList = new ArrayList<>();
        // 새 S3 객체 업로드
        for (MultipartFile image : images) {

            String imageName = image.getOriginalFilename(); // 파일의 원본명
            String extension = StringUtils.getFilenameExtension(Paths.get(imageName).toString()); // 확장자명
            String imageUuid = UUID.randomUUID() + "." + extension; // 해당 파일의 고유한 이름

            // 업로드할 파일의 메타데이터 생성(확장자 / 파일 크기.byte)
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + extension);
            metadata.setContentLength(image.getSize());

            // 요청 객체 생성(버킷명, 파일명, 스트림, 메타정보)
            PutObjectRequest request;
            try {
                request = new PutObjectRequest(bucket, imageUuid, image.getInputStream(), metadata);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Image img = new Image(imageUuid, amazonS3.getUrl(bucket, imageUuid).toString(), goods);
            // S3 버킷에 등록
            amazonS3.putObject(request);
            newImageList.add(img);

        }

        imageRepository.saveAll(newImageList);

        return newImageList;
    }

    @Transactional
    public String saveUserImages(MultipartFile image, AmazonS3 amazonS3, String bucket, User user) {
        if (!validateFile(image)) {
            throw new IllegalStateException("파일 검증 실패");
        }

        String imageName = image.getOriginalFilename(); // 파일의 원본명
        String extension = StringUtils.getFilenameExtension(Paths.get(imageName).toString()); // 확장자명
        String imageUuid = UUID.randomUUID() + "." + extension; // 해당 파일의 고유한 이름

        // 업로드할 파일의 메타데이터 생성(확장자 / 파일 크기.byte)
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        metadata.setContentLength(image.getSize());

        // 요청 객체 생성(버킷명, 파일명, 스트림, 메타정보)
        PutObjectRequest request;
        try {
            request = new PutObjectRequest(bucket, imageUuid, image.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // S3 버킷에 등록
        amazonS3.putObject(request);
        UserImage img = new UserImage(imageUuid, amazonS3.getUrl(bucket, imageUuid).toString(), user);

        userImageRepository.save(img);

        return amazonS3.getUrl(bucket, imageUuid).toString();
    }

    @Transactional
    public void deleteFileFromS3(String imageUuid, AmazonS3 amazonS3, String bucket) {
        amazonS3.deleteObject(bucket, imageUuid);
    }

    @Transactional
    public void repositoryImageDelete(List<Image> imageList) {
        imageRepository.deleteAll(imageList);
    }

    public boolean validateFile(MultipartFile file) {
        // 지원하는 파일 확장자 리스트
        List<String> fileExtensions = Arrays.asList("jpg", "png", "webp", "heif", "gif", "jpeg");

        String path = Paths.get(file.getOriginalFilename()).toString(); // 원본 파일명으로 파일 경로 생성
        String extension = StringUtils.getFilenameExtension(path); // 확장자명

        // 파일 확장자 null check
        if (extension == null) {
            throw new IllegalArgumentException("사용할 수 없는 확장자입니다.");
        }

        // 파일 확장자 검증
        if (!fileExtensions.contains(extension.toLowerCase())) {
            throw new IllegalArgumentException("지원되지 않는 확장자 형식입니다.");
        }

        // 파일 크기 검증
        long maxSize = 10 * 1024 * 1024; // 10MB
        long fileSize = file.getSize();

        if (fileSize > maxSize) {
            throw new IllegalArgumentException("파일의 크기는 최대 10MB입니다.");
        }
        return true;
    }


    private static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
