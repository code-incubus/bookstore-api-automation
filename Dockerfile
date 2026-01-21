FROM maven:3.9.8-eclipse-temurin-17

WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Copy source code
COPY src/ src/

ENV BASE_URL="https://fakerestapi.azurewebsites.net/api/v1"

CMD ["mvn", "-q", "clean", "test"]