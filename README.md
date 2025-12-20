# API

* **user-service** — CRUD API для управления пользователями
* **notification-service** — сервис email-уведомлений о событиях пользователей

Формат данных: **JSON**
Протокол: **HTTP/REST**
Аутентификация: отсутствует

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
  "createdAt": "2024-01-01T12:00:00"
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
    "createdAt": "2024-01-01T12:00:00"
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
