# Payment Integration Service

A Spring Boot REST API that creates payments and checks their status
by calling an external partner API using Java's built-in `HttpClient`.

## Tech
Java 25, Spring Boot, Maven, `java.net.http.HttpClient`

## Endpoints
| Method | URL | What it does |
|---|---|---|
| POST | /payments | Create a payment (201) |
| GET | /payments | List all payments |
| GET | /payments/{id} | Get one payment (404 if missing) |
| GET | /payments/{id}/status | Ask the partner API and update the status |

Example request body:

```json
{"amount": 500, "method": "UPI", "merchantId": "M123"}
```

## Design
- **OOP:** a `PaymentProcessor` interface, an abstract base class, and
  `UpiProcessor` / `CardProcessor` implementations (abstraction,
  inheritance, polymorphism). A new payment method needs one new class
  and no change to the service.
- **HttpClient:** one reused client with connect and request timeouts,
  retries with backoff on 5xx and network errors, and no retry on 4xx.
- **Errors:** a global exception handler returns clear JSON with the
  right status (400, 404, 502).
- **Money** uses `BigDecimal`, not `double`.
- Payments are stored in memory (no database yet).

## Run
On Windows:

    mvnw.cmd spring-boot:run

On Mac/Linux:

    ./mvnw spring-boot:run

Then call http://localhost:8080/payments with Postman or curl.
The partner API URL and timeout are set in `application.properties`.

## Next steps
PostgreSQL storage, idempotency keys, and a React frontend.
