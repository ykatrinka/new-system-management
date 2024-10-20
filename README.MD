# Система управления новостями - News Management System

# Описание проекта

Программа реализует API для выполнения CRUD(создание, прочтение, изменение, удаление).

Добавлена возможность полнотекстового поиска. Используется кеширование.

- Архитектурный стиль: REST.
- Вывод данных в формте JSON
- PostgreSQL
- Docker Compose для контейнеризации проекта.
- Liquibase для миграции базы данных.
- JWT token Spring Security

Код документирован @JavaDoc, подключен Swagger (OpenAPI 3.0)

- Реализовано четыре микросервиса:

- news-service
- comments-service
- server-config
- auth-service

Микросервисы взаимодействуют между собой во время работы (Spring Cloud OpenFeign).

Микросервис server-config реализован на базе Spring Cloud Config. Настройки хранятся удаленно (git). Микросервис раздает
настройки микросервисам с учетом профилей.

Микросервис auth-service реализует авторизацию пользователей с учетом ролей.

Добавлены профили prod, dev и test.

- Профиль prod применяется для запуска и взаимодействия сервисов в docker.
- Профиль dev используется для локального запуска

News-service реализует RESTFul Api для работы с новостями

[Подробнее о news-service](news-service/README.md)

Comments-service реализует RESTFul Api для работы с комментариями

[Подробнее о comments-service](comments-service/README.md)

[Подробнее о server-config](server-config/README.md)

[Подробнее о auth-servcice](auth-service/README.md)

## Инструкция по запуску приложения

1. Клонировать репозиторий из Git на свой локальный компьютер:  
   `git clone https://github.com/ykatrinka/new-system-management`

2. Установите корректные данные для подключения к базе данных в файле application.yaml (src/main/resources)
   Структура таблиц базы данных и демо данные загружаются автоматически

3. Установить environment variable: JWT_SECRET для работы с токеном.

## Инструкция по запуску приложения с использованием Docker

Перед запуском приложения необходимо убедиться, что на вашем ПК установлено и запущенно приложение Docker.

1. Клонировать репозиторий из Git на свой локальный компьютер:  
   `git clone https://github.com/ykatrinka/new-system-management`

2. Установить environment variable: JWT_SECRET для работы с токеном.

3. Перейти в корневую директорию проекта и запустить docker-compose файл:  
   `docker compose up -d`

После выполнения данных шагов приложение будет успешно запущенно.

### Использование Swagger (Open API)

Перейдя по адресу

http://localhost:8081/swagger-ui/index.html

У вас будет возможность обратиться ко всем возможным endpoints сервиса News.

Перейдя по адресу

http://localhost:8082/swagger-ui/index.html

У вас будет возможность обратиться ко всем возможным endpoints сервиса Comments.

Перейдя по адресу

http://localhost:8003/swagger-ui/index.html

У вас будет возможность обратиться ко всем возможным endpoints сервиса auth-service.

