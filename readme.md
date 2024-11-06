# Документация

## Содержание

1. [Введение](#введение)
2. [Конфигурация](#конфигурация)
3. [Эндпоинты](#эндпоинты)
4. [Безопасность](#безопасность)
5. [Api Documentation](#api-documentation)
## Введение

Этот проект представляет собой Spring Boot приложение с настройками безопасности и различными эндпоинтами для
регистрации пользователей, аутентификации и доступа к данным.

## Конфигурация

Конфигурация безопасности определена в файле `src/main/java/org/example/registration/configuration/SecurityConfig.java`.
Она включает настройки для CSRF, CORS, управления сессиями и провайдеров аутентификации.

## Эндпоинты

Приложение предоставляет несколько эндпоинтов:

- `/login`: Аутентифицирует пользователя и возвращает JWT токен.
- `/registration`: Регистрирует нового пользователя.
- `/reset-password`: Сбрасывает пароль пользователя.
- `/unsecured`: Доступен без аутентификации.
- `/admin`: Доступен только пользователям с ролью `ADMIN`.
- `/info`: Возвращает информацию о аутентифицированном пользователе.

## Безопасность

Конфигурация безопасности использует JWT для аутентификации и включает пользовательские фильтры и
обработчики.

# Api Documentation

## Registration

- **Endpoint:** `/registration`
- **Method:** `POST`
- **Request Body:**

json
{
"username": "name",
"email" : "mail",
"password": "password",
"confirmPassword" : "password"
}

- **Response:**
  - **Success:** `201 Created`
  - **Failure:**
    - `400 Bad Request` if passwords do not match
    - `400 Bad Request` if name is already contains in db

## Authentication

- **Endpoint:** `/login`
- **Method:** `POST`
- **Request Body:**

{
"username": "name",
"password": "password",
"rememberMe" : true/false
}

- **Response:**
    - **Success:** `200 OK` with JWT token (30 days if `rememberMe` is true, otherwise 10 minutes)
    - **Failure:** `401 Unauthorized`

## Password Reset

- **Endpoint:** `/reset-password`
- **Method:** `POST`
- **Request Body:**

json
{
"name": "string",
"newPassword": "string",
"confirmPassword": "string",
"confirmationCode": "0000"
}

- **Response:**
  - **Success:** `200 OK`
  - **Failure:**
    - `400 Bad Request` if passwords do not match
    - `400 Bad Request` if the confirmation code is incorrect

