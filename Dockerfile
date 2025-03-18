# 1. 베이스 이미지 설정
FROM openjdk:17-jdk

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 실행 권한 추가 및 빌드 진행
COPY target/dtect-springboot-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080