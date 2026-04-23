# Feature-First Structure

This branch rewrites the project into a feature-first package structure.

## Current top-level layout

```text
src/main/java/com/agshin/extapp
  features/
    analytics/
    audit/
    category/
    expense/
    file/
    user/
  shared/
    api/
    config/
    exception/
    persistence/
    security/
```

## What belongs in `features`

Each business capability owns its own code.

### `features/category`

Category-related HTTP models, service logic, entity, mapper, and repository.

### `features/expense`

Expense and recurring-expense flows:

- controller
- request/response DTOs
- expense entities and enums
- repositories and mappers
- validation
- recurring job and scheduler

### `features/user`

User registration, sign-in, password reset, and current-user access:

- controller
- request/response DTOs
- user entities and enums
- repositories and mapper
- user services and auth service

### `features/file`

File upload/download and email support:

- file controller
- storage service
- email service
- file metadata entity and repository

### `features/analytics`

Analytics endpoint, service, and returned DTOs.

### `features/audit`

Audit logging and audit infrastructure:

- audit entity and enum
- repository
- logger
- aspect
- annotation

## What belongs in `shared`

Only cross-cutting pieces that are not owned by one feature.

### `shared/api`

Reusable API-level types:

- `ApplicationConstants`
- `GenericResponse`
- `PagedResponse`
- global controller advice

### `shared/config`

Application-wide Spring configuration:

- security config
- async config
- scheduler config
- JPA config
- Liquibase config
- Swagger config
- auditor provider
- file storage properties

### `shared/security`

Cross-feature security support:

- JWT utility
- security filters
- custom user details
- custom user details service
- admin authentication token

### `shared/exception`

Application-wide exception types.

### `shared/persistence`

Shared persistence base classes such as `BaseAuditEntity`.

## Folder rules

Use these rules when adding new code:

1. Put code inside a feature folder when it mainly serves one business area.
2. Put code in `shared` only when multiple features genuinely depend on it.
3. Prefer keeping controller, DTOs, service, entity, mapper, and repository close to each other inside the same feature.
4. Avoid recreating global `controllers`, `services`, `repositories`, `mappers`, or `model` folders.

## Why this structure is better

The old layered layout made a single feature spread across many unrelated top-level folders. That made navigation harder even in a small monolith.

With feature-first packaging:

- one feature is mostly in one place
- changes are easier to track
- the monolith stays simple without pretending to be microservices
- shared infrastructure is still centralized where it makes sense
