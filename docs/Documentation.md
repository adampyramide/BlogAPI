# REST API Documentation

## Open Endpoints
Open endpoints require no authentication.

### Auth
Endpoints related to user authentication.
- [Register](auth/Register.md) `POST /api/register`
- [Login](auth/Login.md) `POST /api/login`

## Closed Endpoints
Closed enpoints require a valid token which can be fetched with the Login endpoint.

### Users
Endpoints related to users.
- [GetUserById](blog-posts/GetUserById.md) `GET /api/users/:id`
- [GetPostsByUserId](blog-posts/GetPostsByUserId.md) `GET /api/users:/id/posts`

### Blog-posts
Endpoints related to blog posts.
- [GetBlogPosts](blog-posts/GetBlogPosts.md) `GET /api/blog-posts`
- [CreateBlogPost](blog-posts/CreateBlogPost.md) `POST /api/blog-posts`
- [EditBlogPost](blog-posts/EditBlogPost.md) `PUT /api/blog-posts/:id`
- [DeleteBlogPost](blog-posts/DeleteBlogPost.md) `DELETE /api/blog-posts/:id`