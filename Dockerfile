FROM maven:3.9.8-eclipse-temurin-17

WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Copy source
COPY src ./src

# Default config (can be overridden with -e BASE_URL=...)
ENV BASE_URL="https://fakerestapi.azurewebsites.net/api/v1"

# Container runs tests by default
CMD ["mvn", "-q", "clean", "test"]