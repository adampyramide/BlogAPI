# GetPostsByUserId

Finds posts that match the author's user id with the specified user id and returns them.

**Method:** `GET`

**URL:** `/api/users/:id/posts`

**Body:** None

## Success Responses

**Condition:** User is logged in

**Code:** `200 OK`

**Content:** A list of:
```json
{
  "title": "myTitle",
  "body": "myBody",
  "createTime": "2024-06-30T10:00:00"
}
```

## Error Responses

**Condition:** User is not logged in

**Code:** `401 UNAUTHORIZED`

**Content:** None