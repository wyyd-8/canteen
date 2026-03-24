# Stage 1: Build Frontend
FROM node:20-slim AS frontend-build
WORKDIR /app/frontend
COPY canteen-frontend/package*.json ./
RUN npm install
COPY canteen-frontend/ ./
RUN npm run build

# Stage 2: Build Backend
FROM maven:3.9.6-eclipse-temurin-21 AS backend-build
WORKDIR /app/backend
COPY pom.xml .
# 预下载依赖
RUN mvn dependency:go-offline
COPY src ./src
# 将前端构建产物复制到 Spring Boot 的静态资源目录
COPY --from=frontend-build /app/frontend/dist ./src/main/resources/static
RUN mvn clean package -DskipTests

# Stage 3: Final Image
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=backend-build /app/backend/target/*.jar app.jar
EXPOSE 8080
# 生产环境配置，连接到 docker-compose 中的 mysql 服务
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
