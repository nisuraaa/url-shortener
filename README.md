# url-shortener

A REST API for shortening URLs, built with Spring Boot, PostgreSQL, and Flyway. Loosely follows the [roadmap.sh URL Shortening Service](https://roadmap.sh/projects/url-shortening-service) project brief.

## Stack

- Java 17, Spring Boot 4.1.1 (Spring MVC, Spring Data JPA, Bean Validation)
- PostgreSQL, versioned with Flyway migrations
- Lombok
- springdoc-openapi (Swagger UI)

## Running it

**1. Start the database**

```bash
docker compose up -d
```

This brings up a `postgres:16-alpine` container on `localhost:5432` with database `url-shortener-db`, user `user`, password `user`.

**2. Run the app**

```bash
./gradlew bootRun
```

The API listens on `http://localhost:8080`. Flyway applies migrations automatically on startup.

**3. (Optional) Point at a different database**

Override via environment variables — defaults match the docker-compose setup above:

| Variable | Default |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/url-shortener-db` |
| `DB_USERNAME` | `user` |
| `DB_PASSWORD` | `user` |

## API documentation

Interactive API docs (Swagger UI): `http://localhost:8080/swagger-ui.html`
Raw OpenAPI spec: `http://localhost:8080/v3/api-docs`

## Endpoints

All routes are under `/api/url-shortener`. There is no authentication.

### Create a short URL

```
POST /api/url-shortener
Content-Type: application/json

{ "originalUrl": "https://example.com/some/long/path" }
```

`201 Created`

```json
{ "shortCode": "aB3dEfGh", "originalUrl": "https://example.com/some/long/path" }
```

`400 Bad Request` if `originalUrl` is missing or not a valid `http(s)://` URL. Shortening the same URL twice returns the same `shortCode` rather than minting a new one.

### Resolve a short code

```
GET /api/url-shortener/{shortCode}
```

`200 OK`

```json
{
  "id": 1,
  "shortCode": "aB3dEfGh",
  "originalUrl": "https://example.com/some/long/path",
  "createdAt": "2026-09-07T12:34:56",
  "clicks": 4
}
```

`404 Not Found` if the short code doesn't exist. Each successful lookup increments the click count asynchronously.

### Update a short URL's target

```
PUT /api/url-shortener/{shortCode}
Content-Type: application/json

{ "originalUrl": "https://example.com/a/new/path" }
```

`200 OK`

```json
{ "shortCode": "aB3dEfGh", "originalUrl": "https://example.com/a/new/path" }
```

`404 Not Found` if the short code doesn't exist. `409 Conflict` if another short code already points at the given `originalUrl`.

### Delete a short URL

```
DELETE /api/url-shortener/{shortCode}
```

`204 No Content`. `404 Not Found` if the short code doesn't exist.

### Get click statistics

```
GET /api/url-shortener/{shortCode}/stats
```

`200 OK` — same shape as the resolve endpoint above, including `clicks`.

`404 Not Found` if the short code doesn't exist.

## Project layout

```
src/main/java/com/indezah/url_shortener/
├── controller/    UrlController — the five endpoints above
├── service/       UrlShortenerService (create/read/update/delete, code generation),
│                  ClickService (async click counting)
├── repository/    UrlRepository (Spring Data JPA)
├── entity/        Url
├── dto/           request/response bodies
├── exception/     UrlNotFoundException, UrlAlreadyExistsException, GlobalExceptionHandler
└── config/        OpenApiConfig
src/main/resources/db/migration/   Flyway migrations
```
