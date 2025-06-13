# Book Rental Shop

This is a simple book rental shop REST API application built with Java using Spring Boot.

## Environment

- Java 21
- Spring Boot 3
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Querydsl
- Flyway

## Package Structure

```txt
com.example.bookrentalshop
├── config
│   └── properties
├── controller
│   └── dto
├── domain
│   ├── command
│   ├── constant
│   ├── entity
│   ├── model
│   └── service
├── repository
└── support
    ├── error
    ├── exception
    └── security
```

## Description

### Endpoints

- Books: `/api/v1/books`
- Rentals: `/api/v1/rentals`
- Users: `/api/v1/users`

### Tests

The project includes unit tests, which are written separately for each layer: controller, service, and repository.

### OpenAPI/Swagger

The OpenAPI specification file is generated when the Gradle `openapi3` task is executed. It is available at
`build/api-spec/openapi3.yaml`.

### User data

It is necessary to set both admin and user passwords in the initialization data query file. The passwords must be encoded using BCrypt.
