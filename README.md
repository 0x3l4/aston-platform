# API

* **user-service** — CRUD API для управления пользователями
* **notification-service** — сервис email-уведомлений о событиях пользователей

Формат данных: **JSON**
Протокол: **HTTP/REST**

---

## user-service

Базовый URL:

```
/users
```

### Модель пользователя (`UserDto`)

```json
{
  "id": 1,
  "name": "Ivan Ivanov",
  "email": "user@example.com",
  "age": 25,
  "createdAt": "2025-01-01T12:00:00"
}
```

---

### Получить всех пользователей

**GET** `/users`

**Ответ 200 OK**

```json
[
  {
    "id": 1,
    "name": "Ivan Ivanov",
    "email": "user@example.com",
    "age": 25,
    "createdAt": "2025-01-01T12:00:00"
  }
]
```

---

### Получить пользователя по ID

**GET** `/users/{id}`

**Ответы**:

* `200 OK` — пользователь найден
* `404 Not Found` — пользователь не найден

---

### Создать пользователя

**POST** `/users`

Тело запроса (`CreateUserRequest`):

```json
{
  "name": "Ivan Ivanov",
  "email": "user@example.com",
  "age": 25
}
```

Валидация:

* `name`: 3–30 символов, не пустое
* `email`: корректный email
* `age`: от 13 до 130

**Ответ 201 Created**

* Header `Location: /users/{id}`
* Тело: `UserDto`

---

### Обновить пользователя

**PUT** `/users/{id}`

Тело запроса (`UpdateUserRequest`):

```json
{
  "name": "Ivan Petrov",
  "email": "new@example.com",
  "age": 30
}
```

Валидация аналогична созданию пользователя.

**Ответ 200 OK** — обновлённый `UserDto`

---

### Удалить пользователя

**DELETE** `/users/{id}`

**Ответ 204 No Content**

При удалении публикуется событие пользователя.

---

## notification-service

Базовый URL:

```
/api/notifications
```

### Отправка уведомления

**POST** `/api/notifications`

```json
{
  "email": "user@example.com",
  "eventType": "CREATE"
}
```

`eventType`:

* `CREATE` — пользователь создан
* `DELETE` — пользователь удалён

Ответ: `200 OK`, тело отсутствует.

---

## События

user-service публикует события `CREATE` и `DELETE`, которые обрабатываются notification-service и преобразуются в email-уведомления.

---

## Настройка и запуск приложения

Для локального запуска сервисов используются переменные окружения, задаваемые через файл `.env`.

В корне репозитория присутствует шаблон:

```
.env.example
```

### Шаги настройки

1. Скопируйте файл примера:

```
cp .env.example .env
```

2. Заполните значения переменных в файле `.env`:

```env
DB_URL=jdbc:postgresql://localhost:5432/mydb
DB_USER=postgres
DB_PASS=secret

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=example@gmail.com
MAIL_PASSWORD=your_app_password
```

---

### Описание переменных

#### База данных (user-service)

* `DB_URL` — JDBC URL PostgreSQL
* `DB_USER` — пользователь базы данных
* `DB_PASS` — пароль пользователя базы данных

Используется для хранения и управления пользователями.

#### Почтовый сервер (notification-service)

* `MAIL_HOST` — SMTP хост
* `MAIL_PORT` — SMTP порт
* `MAIL_USERNAME` — email отправителя
* `MAIL_PASSWORD` — пароль приложения (использовался Yandex App Password)

Используется для отправки email-уведомлений о событиях пользователя.

---

3. Настройки Kafka (v4.1.1 + KRaft) в `infra/server.properties`
