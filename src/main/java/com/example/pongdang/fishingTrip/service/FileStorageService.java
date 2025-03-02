package com.example.pongdang.fishingTrip.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${server.url}") // ✅ application.properties에서 서버 URL 가져오기
    private String serverUrl;

    private final String uploadDir = "uploads/"; // ✅ 업로드 디렉토리

    @Transactional
    public String saveFile(MultipartFile file) {
        try {
            // ✅ uploads 폴더가 없으면 자동 생성
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // ✅ 파일 이름 설정 (중복 방지를 위해 타임스탬프 추가)
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // ✅ 파일 저장
            Files.write(filePath, file.getBytes());

            // ✅ 클라이언트가 접근할 수 있는 절대 URL 반환
            return serverUrl + "/uploads/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
