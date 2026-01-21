from maven:3.9.8-eclipse-temurin-17

WORKDIR /app

# Copy pom and maven wrapper for dependency caching
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .

# Downloading dependencies (cache layer)
RUN mvn -q -DskipTests dependency:go-offline

# Copy source
Copy src/ src/

# Default config (can be overriden)
ENV BASE_URL="https://fakerestapi.azurewebsites.net/api/v1"

# Run tests when container starts
CMD ["mvn", "-q", "clean", "test"]