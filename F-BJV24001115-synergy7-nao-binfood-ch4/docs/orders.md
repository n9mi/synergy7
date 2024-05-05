# Order API Documentation

## 1. Create order
### Url
```
[POST] /api/v1/orders
```
### Header
```
[X-API-TOKEN] : user token (from 'token' in 'users' table)
```
### Request
```
{
    "destinationAddress": "string"
}
```
### Response
```
{
    "data": {
        "id": "string",
        "orderAt": "string",
        "destinationAddress": "string"
        "completedAt": "string"
    },
    "errors": ""
}
```

## 2. Get all orders from user
### Url
```
[GET] /api/v1/orders
```
### Header
```
[X-API-TOKEN] : user token (from 'token' in 'users' table)
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
            "destinationAddress": "string",
            "orderAt": "string",
            "completedAt": "string"
        }
    ],
    "errors": ""
}
```

## 3. Get order and its details by id
### Url
```
[GET] /api/v1/orders/{id}
```
### Param
```
(string) id : order id
```
### Header
```
[X-API-TOKEN] : user token (from 'token' in 'users' table)
```
### Request
```

```
### Response
```
{
    "data": {
        "id": "string",
        "destinationAddress": "string",
        "orderAt": "string",
        "completedAt": "string",
        "orderDetails": [
            {
                "orderId": "string",
                "orderDetailId": "string",
                "merchantId": "string",
                "merchantName": "string",
                "productId": "string"
                "productName": "string",
                "quantity": 0,
                "totalPrice": 0,
            }
        ]
    },
    "errors": ""
}
```

## 4. Add order detail
Creating an order detail, will return error if merchant is closed or when order is already marked as complete.
### Url
```
[POST] /api/v1/orders/{id}
```
### Param
```
(string) id : order id
```
### Header
```
[X-API-TOKEN] : user token (from 'token' in 'users' table)
```
### Request
```
{
    "productId": "string",
    "quantity": 0,
}
```
### Response
```
{
    "data": {
        "orderDetailId": "string",
        "orderId": "string",
        "merchantId": "string",
        "merchantName": "string",
        "productId": "string"
        "productName": "string",
        "quantity": 0,
        "totalPrice": 0,
    },
    "errors": ""
}
```

## 5. Update order details
Updating an order detail, will return error if merchant is closed or when order is already marked as complete.
### Url
```
[PUT] /api/v1/orders/{orderId}/details/{orderDetailId}
```
### Param
```
(string) orderId : order id
(string) orderDetailId : order detail id
```
### Header
```
[X-API-TOKEN] : user token (from 'token' in 'users' table)
```
### Request
```
{
    "productId": "string",
    "quantity": 0,
}
```
### Response
```
{
    "data": {
        "orderDetailId": "string",
        "orderId": "string",
        "merchantId": "string",
        "merchantName": "string",
        "productId": "string"
        "productName": "string",
        "quantity": 0,
        "totalPrice": 0,
    },
    "errors": ""
}
```

## 6. Delete order details
### Url
```
[DELETE] /api/v1/orders/{orderId}/details/{orderDetailId}
```
### Param
```
(string) orderId : order id
(string) orderDetailId : order detail id
```
### Header
```
[X-API-TOKEN] : user token (from 'token' in 'users' table)
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

## 7. Checkout order
Mark order as completed. Will return error if order already marked as completed before. 
### Url
```
[POST] /api/v1/orders/{id}/checkout
```
### Param
```
(string) id : order id
```
### Header
```
[X-API-TOKEN] : user token (from 'token' in 'users' table)
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