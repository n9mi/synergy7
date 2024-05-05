# Merchant API Documentation

## 1. Get all opened merchants
### Url
```
[GET] /api/v1/merchants
```
### Url query
```
int (page) : n-th page
int (pageSize) : number of item in page
```
### Request
```

```
### Response
```
{
    "data": [
        {
            "id": "string",
            "name": "string",
            "location": "string",
            "open": true
        }
    ],
    "errors": ""
}
```

## 2. Create merchant
### Url
```
[POST] /api/v1/merchants
```
### Request
```
{
    "name": "string",
    "location": "string",
    "open": true
}
```
### Response
```
{
    "data": {
            "id": "string",
            "name": "string",
            "location": "string",
            "open": true
    },
    "errors": ""
}
```

## 3. Update merchant
### Url
```
[PUT] /api/v1/merchants/{id}
```
### Param
```
String (id) : merchant id
```
### Request
```
{
    "name": "string",
    "location": "string",
    "open": true
}
```
### Response
```
{
    "data": {
            "id": "string",
            "name": "string",
            "location": "string",
            "open": true
    },
    "errors": ""
}
```

## 4. Delete merchant
### Url
```
[DELETE] /api/v1/merchants/{id}
```
### Param
```
String (id) : merchant id
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