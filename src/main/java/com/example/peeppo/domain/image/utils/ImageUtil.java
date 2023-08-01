package com.example.peeppo.domain.image.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class ImageUtil {


    public boolean validateFile(MultipartFile file) {
        // 지원하는 파일 확장자 리스트
        List<String> fileExtensions = Arrays.asList("jpg", "png", "webp", "heif", "heic", "gif", "jpeg");

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
        long maxSize = 20 * 1024 * 1024; // 20MB
        long fileSize = file.getSize();

        if (fileSize > maxSize) {
            throw new IllegalArgumentException("파일의 크기는 최대 20MB입니다.");
        }
        return true;
    }

    public List<String> uploadFileToS3(List<MultipartFile> images, AmazonS3 amazonS3, String bucket) {
        List<String> uploadedFileUuids = new ArrayList<>();
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
            PutObjectRequest request = null;
            try {
                request = new PutObjectRequest(bucket, imageUuid, image.getInputStream(), metadata);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // S3 버킷에 PUT(등록 요청)
            amazonS3.putObject(request);

            uploadedFileUuids.add(imageUuid);
        }

        return uploadedFileUuids;
    }

    public void deleteFileFromS3(String imageUuid, AmazonS3 amazonS3, String bucket) {
        amazonS3.deleteObject(bucket, imageUuid);
    }
}