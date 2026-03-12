# Market — backend для интернет‑магазина

Backend‑сервис для интернет‑магазина: пользователи регистрируются и авторизуются по JWT, создают магазины, добавляют товары, формируют корзину и оформляют заказы. Проект задуман как основной pet‑project с упором на продакшн‑подход: безопасная аутентификация, миграции БД, кэширование через Redis и аккуратный слой REST‑API.

## Основной функционал

- **Пользователи и авторизация**
  - Регистрация нового пользователя (`/auth/register`).
  - Логин с выдачей JWT‑токена (`/auth/login`).
  - Шифрование пароля через `BCryptPasswordEncoder`.
  - Роли и права доступа через Spring Security.

- **Товары и магазины**
  - CRUD‑операции над магазинами и товарами.
  - Фильтрация и поиск товаров (через `ProductSpecification`).
  - Каталог товаров доступен анонимно (часть эндпоинтов `GET /api/products`, `GET /api/shops` открыты).

- **Корзина и заказы**
  - Добавление и изменение количества товаров в корзине.
  - Создание заказов на основе содержимого корзины.
  - Статусы заказов (`OrderStatus`).
  - Проверка баланса пользователя (`NotEnoughMoneyException` и др.).

- **Инфраструктура и качества жизни**
  - Централизованный обработчик ошибок (`GlobalExceptionHandler`).
  - Миграции схемы БД через Flyway.
  - Кэширование часто запрашиваемых данных через Redis.
  - Документация REST‑API через OpenAPI/Swagger.

## Стек технологий

### Язык и платформа

- **Java 17**
- **Spring Boot 3.5.9**

### Spring‑стартеры и библиотеки

- **Spring Web**: построение REST‑контроллеров (`@RestController`).
- **Spring Data JPA**: работа с БД через репозитории (`UserRepository`, `ProductRepository`, `OrderRepository`, `ShopRepository` и др.).
- **Spring Security**: защита эндпоинтов, интеграция с JWT.
- **Spring Validation**: валидация входящих DTO.
- **Spring Cache + Spring Data Redis**: кэширование данных на уровне сервисов.
- **Spring Boot Actuator**: технические эндпоинты мониторинга.
- **Spring Boot Test**: запуск интеграционных и модульных тестов.

### Безопасность: Spring Security + JWT

- **Стратегия безопасности**
  - Открытые эндпоинты: `/auth/**`, а также часть публичного каталога (`/api/products`, `/api/shops`, `/api/products/**`).
  - Все остальные запросы требуют аутентификации.

- **JWT‑фильтр**
  - Класс `JwtAuthFilter` расширяет `OncePerRequestFilter` и обрабатывает каждый запрос.
  - Токен читается из заголовка `Authorization: Bearer <token>`.
  - Валидация и извлечение имени пользователя выполняются через `JwtUtil`.
  - После успешной проверки в `SecurityContextHolder` устанавливается `UsernamePasswordAuthenticationToken`.

- **Аутентификация**
  - `AuthController` предоставляет:
    - `POST /auth/register` — создание пользователя по `UserCreateDto`.
    - `POST /auth/login` — аутентификация по `LoginRequest` и выдача `AuthResponse` с JWT‑токеном.
  - Пароли хранятся в базе в зашифрованном виде (bcrypt).

### База данных: MySQL

- **СУБД**: **MySQL** (через драйвер `mysql-connector-j`).
- **Почему MySQL, а не PostgreSQL**:
  - Осознанный выбор MySQL **для разнообразия от PostgreSQL**, который часто используют «по умолчанию».
  - Практика работы с разными диалектами SQL и драйверами.
- **Конфигурация подключения** (файл `application.properties`):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/market
spring.datasource.username=user
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

### Миграции БД: Flyway

- Используется **Flyway** (`flyway-core`, `flyway-mysql`) для версионирования схемы.
- Все изменения схемы и начальные данные находятся в `src/main/resources/db/migration`:
  - `V1__initial_schema.sql` — базовая структура БД.
  - `V2__rename_user_to_username.sql` и последующие версии (V3…V7) — эволюция схемы (баланс, статусы заказов, категории и т.п.).
- На старте приложения Flyway автоматически применяет недостающие миграции.

### Кэширование: Redis

- **Технологии**:
  - `spring-boot-starter-cache`
  - `spring-boot-starter-data-redis`

- **Что кэшируется**:
  - Отдельные продукты (`ProductService.getProductById`) кэшируются под ключом `products`.
  - Магазины (`ShopService.getShopById`) кэшируются под ключом `shops`.
  - Кэш сильно разгружает БД при повторных чтениях популярных сущностей.

### Документация REST‑API: OpenAPI/Swagger

- Используется `springdoc-openapi-starter-webmvc-ui`.
- После запуска приложения Swagger UI доступен по адресу вида:
  - `http://localhost:8080/swagger-ui/index.html`
  - OpenAPI‑спецификация по `http://localhost:8080/v3/api-docs`.

### Прочие технологии и удобства

- **Lombok**: генерация геттеров/сеттеров, конструкторов и логгеров, уменьшение шаблонного кода.
- **MapStruct**: маппинг между сущностями и DTO (`ProductMapper`, `UserMapper`, `OrderMapper`, `CartMapper`, `ShopMapper` и др.).
- **Аспекты (AOP)**:
  - Аннотации `@GetEntity` и `@GetAllEntities` + аспектные классы `GetEntityAspect`, `GetAllEntitiesAspect` для повторно используемой логики выборки сущностей.

## Архитектура

- **Слоистая архитектура**
  - **Контроллеры** (`controller`) — принимают HTTP‑запросы, работают с DTO и возвращают DTO‑ответы.
  - **Сервисы** (`service`) — бизнес‑логика (управление корзиной, заказами, балансом, товарами и магазинами).
  - **Репозитории** (`repository`) — доступ к БД через Spring Data JPA.
  - **Модели/сущности** (`model`) — отображение таблиц MySQL.
  - **DTO** (`dto.*`) — запросы/ответы API.
  - **Мапперы** (`mapper`) — преобразование Entity ↔ DTO (MapStruct).

- **Доменные сущности**
  - `User` — пользователь с балансом и ролью.
  - `Shop` — магазин.
  - `Product` — товар, связанный с магазином и категорией (`ProductCategory`).
  - `CartItem` — позиция в корзине.
  - `Order` и `OrderItem` — заказ и его строки, статус заказа (`OrderStatus`).

## Запуск проекта локально

### Предварительные требования

- Java 17
- Maven 3.x
- MySQL (локально или в контейнере)
- Redis (локально или в контейнере)

### Настройка окружения

1. **Клонировать репозиторий**

```bash
git clone <url_репозитория>
cd market
```

2. **Настроить доступ к MySQL**

Отредактировать `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/market
spring.datasource.username=<ваш_пользователь>
spring.datasource.password=<ваш_пароль>
```

Создать БД `market` в MySQL

```sql
CREATE DATABASE market CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **Убедиться, что Redis запущен**

- По умолчанию Spring ожидает Redis на `localhost:6379`.
- Можно использовать локальный Redis или контейнер Docker.

### Запуск приложения

```bash
mvn clean package
mvn spring-boot:run
```

Приложение по умолчанию поднимается на порту `8080`.

## Профили приложения и безопасность

- В `application.properties` активен профиль:

```properties
spring.profiles.active=prod
```

- В профиле `prod` включён `SecurityConfig`:
  - Все защищённые эндпоинты требуют JWT‑токен.
  - Используется `JwtAuthFilter` и `JwtUtil`.
- Для упрощённой локальной разработки можно использовать профиль без безопасности (`NoSecurityConfig`), переключив активный профиль.

## Примеры работы с API (кратко)

1. **Регистрация пользователя**

```http
POST /auth/register
Content-Type: application/json

{
  "username": "user1",
  "password": "strong_password"
}
```

2. **Логин и получение JWT**

```http
POST /auth/login
Content-Type: application/json

{
  "username": "user1",
  "password": "strong_password"
}
```

В ответе придёт объект `AuthResponse` с полем `token`. Далее все защищённые запросы выполняются с заголовком:

```http
Authorization: Bearer <jwt-токен>
```

3. **Получение списка товаров (публично)**

```http
GET /api/products
```

4. **Доступ к защищённым ресурсам**

```http
GET /api/orders
Authorization: Bearer <jwt-токен>
```
