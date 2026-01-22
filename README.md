# Bookstore API Automation

Automated API test framework for the **Bookstore REST API**, implemented using **Java, JUnit 5, Rest Assured, Allure**, and fully **Dockerized** with **CI integration via GitHub Actions**.

The project demonstrates API test automation best practices, containerized execution, reporting, and CI/CD integration.

---

## Tech Stack

- Java 17
- JUnit 5
- Rest Assured
- Allure Reporting
- Maven
- Docker
- GitHub Actions (CI)

---

## Project Structure

```
src
 ├── main
 │   ├── java
 │   │   ├── client
 │   │   │   ├── authors
 │   │   │   ├── books
 │   │   │   └── BaseApiClient.java
 │   │   ├── config
 │   │   ├── model
 │   │   ├── spec
 │   │   └── util
 │   └── resources
 └── test
     ├── java
     │   └── tests
     └── resources
         └── schemas
```

**Notes:**
- `src/main/java` contains reusable framework code (clients, specs, config, DTOs, utilities).
- `src/test/java` contains the actual test classes.
- `src/test/resources/schemas` contains JSON schemas used for validation.

---

## Configuration

The framework supports runtime configuration via **environment variables** or **system properties**.

| Variable | Description |
|--------|------------|
| BASE_URL | Base URL of the Bookstore API |

Example:

```bash
export BASE_URL=https://fakerestapi.azurewebsites.net/api/v1
```

---

## Running Tests Locally (Maven)

### Run tests

```bash
mvn clean test
```

### Generate Allure report

```bash
mvn allure:report
```

### View Allure report

```bash
cd target/site/allure-maven-plugin
python3 -m http.server 8000
```

Open in browser:

```
http://localhost:8000
```

> **Important:**  
> Do not open `index.html` directly using `file://`.  
> Allure is a single-page application and requires an HTTP server to properly load JSON data.

---

## Running Tests in Docker

### Build Docker image

```bash
docker build -t bookstore-api-tests .
```

### Run tests inside Docker container

```bash
docker run --rm \
  -e BASE_URL=https://fakerestapi.azurewebsites.net/api/v1 \
  bookstore-api-tests
```

The Docker container automatically executes the full test suite on startup.

---

## Reporting (Allure)

Allure is used to generate an HTML test execution report that provides:

- Pass/fail status for each test case
- Grouping by Epic / Feature / Story
- Detailed HTTP request and response attachments

---

## CI/CD Pipeline (GitHub Actions)

The project includes a CI pipeline configured using **GitHub Actions**.

### Pipeline steps

1. Checkout source code
2. Build Docker image
3. Run tests inside Docker container
4. Generate Allure report
5. Upload reports as CI artifacts

### Generated Artifacts

- **allure-report** – HTML test execution report
- **allure-results** – Raw Allure results

---

## Viewing CI Test Report

1. Go to **GitHub → Actions**
2. Open a workflow run
3. Download the **allure-report** artifact
4. Unzip the artifact
5. Run:

```bash
python3 -m http.server 8000
```

6. Open:

```
http://localhost:8000
```

---

## Notes on API Behavior

- The tested system is a **public demo API**.
- Some negative scenarios reflect API limitations rather than functional defects.
- Test data is generated to minimize conflicts during execution.
---