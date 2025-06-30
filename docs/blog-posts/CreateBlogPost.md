# EditBlogPost

Create a new blog post.

**Method:** `POST`

**URL:** `/api/blog-posts`

**Body:**
```json
{
  "title": "myTitle",
  "body": "myBody",
  "createTime": "2024-06-30T10:00:00"
}
```

## Success Responses

**Condition:** Post is from logged in user

**Code:** `204 NO CONTENT`

**Content:** None

## Error Responses

**Condition:** Post is not from logged in user

**Code:** `401 UNAUTHORIZED`

**Content:** None