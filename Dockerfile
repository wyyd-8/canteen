# 阶段 1: 编译前端
FROM node:20 AS frontend-build
WORKDIR /frontend
COPY canteen-frontend/package*.json ./
RUN npm install
COPY canteen-frontend/ ./
RUN npm run build

# 阶段 2: 编译后端
FROM maven:3.9.6-eclipse-temurin-21 AS backend-build
WORKDIR /app
COPY pom.xml .
# 复制前端编译产物到 Spring Boot 静态目录
COPY --from=frontend-build /frontend/dist ./src/main/resources/static
COPY src ./src
RUN mvn clean package -DskipTests

# 阶段 3: 运行镜像
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
