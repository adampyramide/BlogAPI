# GetUserById

Finds a user with the specified id and returns user data if found.

**Method:** `GET`

**URL:** `/api/users/:id`

**Body:** None

## Success Responses

**Condition:** User is logged in

**Code:** `200 OK`

**Content:**
```json
{
    "id": 0,
    "username": "theirUsername"
}
```

## Error Responses

**Condition:** User is not logged in

**Code:** `401 UNAUTHORIZED`

**Content:** None

### OR

**Condition:** No user with specified id found

**Code:** `404 NOT FOUND`

**Content:** None