# Система управления новостями - News Management System

Тестовое задание для Clevertec

# Описание проекта

Микросервис server-config

Данный микросервис используется для получения настроек необходимых для запуска.

Сервис раздает настройки микросервисам news-service, comments-service и auth-service.

# Зависимости

* Java 21
* Spring Boot
* Gradle 8.5
* Spring Cloud Config
* Docker

# Реализована поддержка @Profile

Добавлены профили prod, dev и test.

Профиль prod применяется для запуска и взаимодействия сервисов в docker.

Профиль dev используется для локального запуска

Порт для запуска: 8888

По url  http://localhost:8888/{microservice-name}/{profilename}
можно просмотреть актуальные настройки.