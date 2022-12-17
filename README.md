RESTful messenger application

<h2>Endpoints</h2>
1. /people
2. /chats
3. /messages

<h3>Request examples</h3>

<h4>/people</h4>

1. Post (/people)

Request Body:

```json
{
  "firstname": "Pasha",
  "lastname": "Levchenko",
  "username": "Pussia",
  "age": 18,
  "description": "My description",
  "phoneNumber": "+1234567890"
}
```

Responses:

- (code 200) Person saved successfully
- (code 400) Validation Failed

2. Get (/people/{ id or username })

Responses:
- (code 200):
```json
{
  "id": 1,
  "firstname": "Pasha",
  "lastname": "Levchenko",
  "username": "Pussia",
  "age": 18,
  "description": "My description",
  "phoneNumber": "+1234567890"
}
```
- (code 404) Person with id/username ... not found

3. Delete (/people/{id})

Responses:
- (code 200) Person deleted successfully
- (code 404) Person with id ... not found

4. Patch (/people/{id})

Request Body:
```json
{
  "firstname": "Pasha",
  "lastname": "Levchenko",
  "username": "Pussia",
  "age": 18,
  "description": "My description",
  "phoneNumber": "+1234567890"
}
```

Responses:

- (code 200) Person updated successfully
- (code 400) Validation Failed
- (code 404) Person with id ... not found


<h4>/messages</h4>

1. Post (/messages)

Request Body:

```json
{
  "message": "Message",
  "date": "2042-05-28T21:12:01.000Z",
  "personId": "1",
  "chatId": "1"
}
```

Responses:

- (code 200) Message saved successfully
- (code 400) Validation Failed / Date format is wrong

2. Get (/messages/{id})

Responses:
- (code 200):
```json
{
  "id": 1,
  "message": "Message",
  "date": "2042-05-28T21:12:01.000Z",
  "personId": "1",
  "chatId": "1"
}
```
- (code 404) Message with id ... not found

3. Get (/messages/chat/{id})

Responses:
- (code 200):
```json
{
  "id": 1,
  "message": "Message",
  "date": "2042-05-28T21:12:01.000Z",
  "personId": "1",
  "chatId": "1"
}
```
- (code 404) Message with id ... not found

4. Delete (/messages/{id})

Responses:
- (code 200) Message deleted successfully
- (code 404) Message with id ... not found

5. Patch (/message/{id})

Request Body:
```json
{
  "message": "New message",
  "date": "2042-05-28T21:12:01.000Z"
}
```

Responses:

- (code 200) Message updated successfully
- (code 400) Validation Failed / Date format is wrong
- (code 404) Message with id ... not found


<h4>/chats</h4>

1. Post (/chat)

Request Body:

```json
{
  "firstId": 1,
  "secondId": 2
}
```

Responses:

- (code 200) Chat added successfully
- (code 400) Validation Failed
- (code 404) Person with id ... not found

2. Get (/chats/{id})

Responses:
- (code 200):
```json
{
  "firstId": 1,
  "secondId": 2,
  "messages": []
}
```
- (code 404) Chat with id ... not found

3. Get (/chats)

Response:
- (code 200):
```json
[
    {
        "firstId": 1,
        "secondId": 2,
        "messages": []
    }
]
```

4. Delete (/people/{id})

Responses:
- (code 200) Chat deleted successfully
- (code 404) Chat with id ... not found

