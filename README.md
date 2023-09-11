
I've utilized a monolithic architecture for the backend application. For exception handling, I employed global exception handling using Spring AOP. For security measures, JWT token authentication was implemented, ensuring every endpoint is secure. To optimize currency rate retrieval, I integrated Redis caching to swiftly access data, eliminating the need to query the endpoint repeatedly.
Redis is configured at `localhost:6379`.

Sign-up endpoint:
```
POST http://localhost:8668/api/auth/signup 
{
    "pin": "user",
    "password": "12345"
}
```

Sign-in endpoint:
```
POST http://localhost:8668/api/auth/signin
{
    "email": "user",
    "password": "12345"
}
```
Retrieve account balance lists:

GET http://localhost:8668/api/account
Header: Authorization: Bearer fhjaiowe849234


Account to account transfer endpoint:

POST http://localhost:8668/api/transaction
Header: Authorization: Bearer fhjaiowe849234
```
{
    "fromAccountNumber": "4B066AF9361342DC87EE15F7F9AEBCDD",
    "toAccountNumber": "4B066AF9361342DC87EE15F7F9AEDD",
    "amount": 500
}
```

Currency rates endpoint:

GET http://localhost:8668/api/currency?currencyFrom=AZN&currencyTo=USD
Header: Authorization: Bearer fhjaiowe849234


