# Multi-stage build for User Service
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /workspace/app

# Copy parent pom and common module first (for better caching)
COPY pom.xml .
COPY modules/common modules/common

# Copy user-service module
COPY modules/user-service modules/user-service

# Build the project
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -f modules/user-service/pom.xml clean package -DskipTests && \
    mkdir -p target && \
    cp modules/user-service/target/*.jar target/app.jar

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

# Install wget for healthcheck
RUN apk add --no-cache wget

# Create non-root user
RUN addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser

WORKDIR /app

# Copy jar from builder
COPY --from=builder --chown=appuser:appuser /workspace/app/target/app.jar app.jar

# Create logs directory
RUN mkdir -p /app/logs && chown -R appuser:appuser /app/logs

USER appuser

# JVM optimizations
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8083

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8083/api/v1/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
