# Register

Creates a new user with the provided username and password.

**Method:** `POST`

**URL:** `/api/auth/register`

**Request Body:**
```json
{
  "username": "yourUsername",
  "password": "yourPassword"
}
```

## Success Responses

**Condition:** No account with username exists

**Code:** `201 CREATED`

**Content:** JWT Token string

## Error Responses

**Condition:** Account with username exists

**Code:** `409 CONFLICT`