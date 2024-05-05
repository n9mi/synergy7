# User API Documentation

## 1. Get all users
Get all authorized users who already have roles. Authorized users has not null "token" in "users" table.
### Url 
```
[GET] /api/v1/users
```
### Url query
```
int (page) : n-th page
int (pageSize) : number of item in page
```
### Request
```
-
```
### Response 
```
  {
    "data" : [
        {
            "id": "string",
            "username": "string",
            "email": "string",
            "createdDate": "string"
            "totalOrder": 0
        },
    ],
    "errors": ""
  }
```

## 2. Add user
Creating new user. Newly created user has empty "token" field.
### Url
```
[POST] /api/v1/users
```
### Request
```
{
    "email": "string",
    "username": "string",
    "password": "string"
}
```
### Response
```
{
    "data": "",
    "errors": ""
}
```

## 3. Update user
### Url
```
[PUT] /api/v1/users/{id}
```
### Url parameter
```
String (id) : user id
```
### Request
```
{
    "email": "string",
    "username": "string"
}
```
### Response
```
{
    "data": "",
    "errors": ""
}
```

## 4. Delete user
### Url
```
[DELETE] /api/v1/users/{id}
```
### Url parameter
```
String (id) : user id
```
### Request
```

```
### Response
```
{
    "data": "",
    "errors": ""
}
```

## 4. Generate dummy user
Generating dummy user. Dummy users are users that doesn't have token, email, and password.
### Url
```
[POST] /api/v1/users/generate/dummy
```
### Request
```

```
### Response
```
{
    "data": "",
    "errors": ""
}
```

## 5. Clean dummy user
Delete all dummy users
### Url
```
[POST] /api/v1/users/generate/dummy
```
### Request
```

```
### Response
```
{
    "data": "",
    "errors": ""
}
```

## 6. Authorize user
Give respective user token.
### Url
```
[POST] api/v1/users/{id}/authorize
```
### Url param
```
String (id) : user id
```
### Request
```

```
### Response
```
{
    "data": "",
    "errors": ""
}
```