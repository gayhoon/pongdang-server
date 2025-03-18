# 1. 기본 이미지 설정 (JDK 17 사용)
FROM openjdk:17-jdk-slim AS build

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle 캐시를 활용할 수 있도록 소스 코드 복사
COPY . /app

# 4. Gradle 빌드 실행 (JAR 파일 생성)
RUN ./gradlew clean build --no-daemon

# 5. 빌드된 JAR 파일을 실행 환경으로 복사
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# 6. 컨테이너 실행 시 JAR 파일 실행
CMD ["java", "-jar", "app.jar"]
