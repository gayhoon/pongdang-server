# OpenJDK 17 기반의 이미지 사용 (Java 17 환경)
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트의 JAR 파일을 컨테이너로 복사
COPY build/libs/*.jar app.jar

# 컨테이너에서 실행할 명령어
CMD ["java", "-jar", "app.jar"]

# 애플리케이션이 실행될 포트
EXPOSE 8080