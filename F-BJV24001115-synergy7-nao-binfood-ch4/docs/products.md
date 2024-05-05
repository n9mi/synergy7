# Product API Documentation

## 1. Get all products
### Url
```
[GET] /api/v1/products
```
### Url query
```
int (page) : n-th page
int (pageSize) : number of item in page
String (merchantId) id of merchant
```
### Request
```

```
### Response
#### When {merchantId} is specified
```
{
    "data": [
        {
            "id": "string",
            "name": "string",
            "price": 0,
            "merchant": null
        }
    ],
    "errors": ""
}
```
#### When {merchantId} is not    specified
```
{
    "data": [
        {
            "id": "string",
            "name": "string",
            "price": 0,
            "merchant": {
                "id": "string",
                "name": "string",
                "location": "string",
                "open": boolean
            }
        }
    ],
    "errors": ""
}
```


## 2. Create product
### Url
```
[POST] /api/v1/merchants
```
### Request
```
{
    "name": "string",
    "price": 0,
    "merchantId": "string"
}
```
### Response
```
{
    "data": {
        "id": "string",
        "name": "string",
        "price": 0,
        "merchant": {
            "id": "string",
            "name": "string",
            "location": "string",
            "open": boolean
        }
    },
    "errors": null
}
```

## 3. Update product
### Url
```
[PUT] /api/v1/products/{id}
```
### Param
```
String (id) : product id
```
### Request
```
{
    "name": "string",
    "price": 0,
    "merchantId": "string"
}
```
### Response
```
{
    "data": {
        "id": "string",
        "name": "string",
        "price": 0,
        "merchant": {
            "id": "string",
            "name": "string",
            "location": "string",
            "open": boolean
        }
    },
    "errors": null
}
```

## 4. Delete product
### Url
```
[DELETE] /api/v1/product/{id}
```
### Param
```
String (id) : product id
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