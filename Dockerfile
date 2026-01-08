# --- Stage 1: Build (PENTING: Pakai Image yang sudah ada JDK 21) ---
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

# Copy semua file project ke dalam container
COPY . .

# Berikan izin eksekusi ke file gradlew
RUN chmod +x gradlew

# Build pakai WRAPPER (./gradlew), bukan 'gradle' biasa
# Ini kunci biar dia pake Java yang ada di container ini
RUN ./gradlew clean bootJar -x test

# --- Stage 2: Run ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Ambil hasil build (perhatikan path build/libs/)
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8181

ENTRYPOINT ["java", "-jar", "app.jar"]