# REST Assured API Automation

![API Tests](https://github.com/AliHamzaGilani/restassured-api-automation/actions/workflows/api.yml/badge.svg)

API test framework for the public [Restful Booker](https://restful-booker.herokuapp.com/apidoc/index.html) API, built with **REST Assured 5**, **Java 17**, **TestNG**, **Jackson** and **AssertJ**.

## Highlights

- **Client layer** (`BookingClient`) keeps HTTP details out of tests
- Shared `RequestSpecification`; request and response logged only when validation fails
- **Java records** as request/response models, serialized with Jackson
- **Test data factory** creates a unique booking per test, so tests never share state
- **JSON Schema validation** of responses
- Full **CRUD coverage** plus auth and negative cases (403, 404, bad credentials)
- **Smoke and regression suites** by TestNG groups; classes run in parallel
- **GitHub Actions** on every push, PR and nightly

## Project structure

```
src/test/java/com/alihamza/api/
  client/   BookingClient
  models/   Booking, CreatedBooking
  data/     BookingFactory
  tests/    AuthTest, BookingTest
src/test/resources/
  schemas/  booking-schema.json
  suites/   smoke.xml, regression.xml
```

## Run it

Requires Java 17+ and Maven.

```bash
mvn test                       # full regression
mvn test -Dsuite=smoke
mvn test -DbaseUrl=http://localhost:3001
```

## Test coverage

| Endpoint | Scenarios |
| --- | --- |
| `GET /ping` | Health check and response time |
| `POST /auth` | Valid token, bad credentials |
| `POST /booking` | Create, response matches request |
| `GET /booking/{id}` | Read back with schema validation, 404 after delete |
| `GET /booking?firstname&lastname` | Search by guest name |
| `PUT /booking/{id}` | Full update, 403 without a valid token |
| `PATCH /booking/{id}` | Partial update keeps other fields |
| `DELETE /booking/{id}` | Delete and confirm it's gone |
