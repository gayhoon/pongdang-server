# 1. OpenJDK 17 이미지 사용
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle 빌드 파일 및 소스 복사
COPY . /app

# 4. Gradle 설치 및 프로젝트 빌드
RUN apt-get update && apt-get install -y curl && \
    curl -s "https://get.sdkman.io" | bash && \
    source "$HOME/.sdkman/bin/sdkman-init.sh" && \
    sdk install gradle 8.0 && \
    gradle build --no-daemon

# 5. JAR 파일을 컨테이너로 복사
COPY build/libs/*.jar app.jar

# 6. 실행 명령어
CMD ["java", "-jar", "app.jar"]
