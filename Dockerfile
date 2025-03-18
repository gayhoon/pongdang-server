# 1. OpenJDK 17 기반의 슬림 버전 사용
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle 설치 (SDKMAN 대신 직접 다운로드)
RUN apt-get update && apt-get install -y curl unzip && \
    curl -sL https://services.gradle.org/distributions/gradle-8.0-bin.zip -o gradle.zip && \
    unzip gradle.zip -d /opt/gradle && \
    rm gradle.zip && \
    ln -s /opt/gradle/gradle-8.0/bin/gradle /usr/bin/gradle

# 4. 프로젝트 소스 복사 및 빌드
COPY . /app
RUN gradle build --no-daemon

# 5. JAR 파일을 컨테이너 내부로 복사
COPY build/libs/*.jar app.jar

# 6. 실행 명령어
CMD ["java", "-jar", "app.jar"]
