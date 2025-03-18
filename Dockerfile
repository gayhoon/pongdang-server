# 1. 베이스 이미지 설정
FROM openjdk:17-jdk

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 실행 권한 추가 및 빌드 진행
COPY . /app
RUN chmod +x ./gradlew  # 실행 권한 추가
RUN ./gradlew clean build --no-daemon  # Gradle 빌드 실행

# 4. JAR 파일 실행
CMD ["java", "-jar", "build/libs/*.jar"]