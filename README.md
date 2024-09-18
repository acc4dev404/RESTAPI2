# REST API 2
REST API service for loading test

## Port
Working on port `8000`

## Endpoints
### GET
`127.0.0.1:8000?login=login`

Response: `200`

```json
{
  "login": "login",
  "password": "login",
  "date": "2024-12-31T23:59:59.99999",
  "email": "login@mail.ru"
}
```

Error: `500`
```json
{
    "message": "Login not found"
}
```


### POST
`127.0.0.1:8000`

Request:
```json
{
  "login": "login",
  "password": "password",
  "email": "email"
}
```

Response: `200`

```json
{
  "message": "User created"
}
```

Error: `400`

```json
{
  "message": "JSON is incorrect"
}
```

Error: `400`

```json
{
  "message": "Login already exists or the user data is incorrect"
}
```


## Run
```cmd
docker build . -t rest-api
docker run -d -p 8000:8000 rest-api
```