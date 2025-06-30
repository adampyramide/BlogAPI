# DeleteBodyPost

Delete a blog post with the specified id.

**Method:** `DELETE`

**URL:** `/api/blog-posts/:id`

**Body:** None

## Success Responses

**Condition:** Post is from logged in user

**Code:** `204 NO CONTENT`

**Content:** None

## Error Responses

**Condition:** Post is not from logged in user

**Code:** `401 UNAUTHORIZED`

**Content:** None

### OR

**Condition:** No blog post found with specified id

**Code:** `404 NOT FOUND`

**Content:** None