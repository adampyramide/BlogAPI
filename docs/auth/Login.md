# Login

---

Log in to a user and return a JWT.

**Method:** `POST`

**URL:** `/api/auth/login`

---

**Request Body:**
```json
{
  "username": "yourUsername",
  "password": "yourPassword"
}
```

## Success Responses

---

**Condition:** Correct username and password provided

**Code:** `200 OK`

**Content:** JWT Token string

## Error Responses

---

**Condition:** Incorrect username or password provided

**Code:** `401 UNAUTHORIZED`