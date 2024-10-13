# Система управления новостями - News Management System

Тестовое задание для Clevertec

# Описание проекта

Микросервис comments-service

Данный микросервис работает с комментариями. Архитектурный стиль: REST. Вывод данных в формате JSON

Сервис связывается с микросервисом news-service с использованием Open Feign

### Сущности:

Комментарии (Comment):

- id
- time
- text
- username
- news_id.

# Функциональные возможности

Программа реализует API для выполнения CRUD(создание, чтение, изменение, удаление) операций для комментариев.

При получении списка комментариев используется пагинация

Реализован полнотекстовый поиск

# Зависимости

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate Validation
* PostgreSQL
* Spring Web
* Lombok
* MapStruct
* Gradle 8.5
* Hibernate ORM
* Lucene
* OpenFeign

При написании интеграционных тестов использовались зависимости Testcontainers и WireMock

---

# API руководство

В этом справочном руководстве по API представлена подробная информация о доступных методах и параметрах API системы.

<details>

## Содержание

* Комментарии
* Ответ об ошибке

### Комментарии

Описание: Этот метод добавляет новый комментарий.
Endpoint: /comments
HTTP Method: POST

Parameters:

| Name        | Type   | Description                          |
|-------------|--------|--------------------------------------|
| newsId      | Long   | Идентификатор новости                |
| username    | String | Имя пользователя                     |
| text        | String | Текст комментария                    |


Пример запроса:
http://localhost:8082/comments

{
"newsId": 1,
"username": "Patrik",
"text": "This is new comment."
}

---
Описание: Этот метод получает комментарий по id.
Endpoint: /comments/{commentId}
HTTP Method: GET

Пример запроса:
http://localhost:8082/comments/1

---
Описание: Этот метод обновляет комментарий.
Endpoint: /comments/{commentId}
HTTP Method: PUT

Parameters:

| Name        | Type   | Description                          |
|-------------|--------|--------------------------------------|
| newsId      | Long   | Идентификатор новости                |
| username    | String | Имя пользователя                     |
| text        | String | Текст комментария                    |

Пример запроса:
http://localhost:8082/comments/1

{
"newsId": 1,
"username": "Patrik",
"text": "This is updated comment."
}

---
Описание: Этот метод удаляет комментарий по id.
Endpoint: /comments/{commentsId}
HTTP Method: DELETE

Пример запроса:
http://localhost:8082/comments/1

---
Описание: Этот метод выводит все комментарии (с пагинацией).
Endpoint: /comments
HTTP Method: GET

Примеры запроса:
http://localhost:8082/comments
http://localhost:8082/comments?pageNumber=1

---
Описание: Этот метод выполняет полнотекстовый поиск.
Endpoint: /comments/search
HTTP Method: GET

Примеры запроса:
http://localhost:8082/comments/search?text=content&fields=username&fields=text&limit=15


### Ответ об ошибке

Пример ответа об ошибке

{
"status": 404,
"message": "No such comment with id 234"
}

</details>