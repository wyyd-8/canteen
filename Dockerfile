# 使用 Java 21 镜像进行编译
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
# 预下载依赖
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# 运行镜像
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
