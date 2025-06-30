# GetBlogPosts

Get all blog posts that exist.

**Method:** `GET`

**URL:** `/api/blog-posts`

**Body:** None

## Success Responses

**Condition:** User is logged in

**Code:** `200 OK`

**Content:** A list of:
```json
{
  "id": 0,
  "title": "myTitle",
  "body": "myBody",
  "createTime": "2024-06-30T10:00:00",
  "author": {
    "id": 0,
    "username": "myUsername"
  }
}
```

## Error Responses

**Condition:** User is not logged in

**Code:** `401 UNAUTHORIZED`

**Content:** None