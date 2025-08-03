# Blog API
RESTful API built with Java 17, Spring Boot & PostgreSQL. Features blog posts, comments, reactions, etc.

Built independently to showcase my API design skills — no tutorials, just self-driven research and implementation.

### Design Decisions
See **[docs/adr](adr)** for details on the technical and architectural decisions made.

### API Documentation
**[API Documentation](https://adampyramide.github.io/BlogAPI/)** is available via Swagger UI.

## Try the API
The API is hosted on Render — please note it may take a few seconds to respond to the first request due to server cold start.

### Base URL
All requests should use this base URL:
**https://blogapi-vzsx.onrender.com/api/**

### Importing Endpoints
Import this OpenAPI document into tools like Postman or Insomnia:
**[docs/openapi/api-docs.json](docs/api-docs.json)**

### Authentication
Some endpoints require a JWT token.
1. Register or log in to receive a token.
2. Include the token in your request headers: ```Authorization: Bearer YOUR_TOKEN_HERE```

Refer to the API docs for details on the login and registration endpoints.

## Technologies Used
- Java 17
- Spring Boot
    - Spring Security
    - Hibernate
    - MapStruct
    - Lombok
- Maven
- PostgreSQL
- Cloudinary
- OpenAPI
- Docker
- Postman
- IntelliJ IDEA

## Features
### Secure User Authentication
- Register and login with username and password
- Passwords securely hashed
- JWT-based authentication

### Blog Post Management
- Full CRUD for blog posts
- Pagination (sorted by newest first)
- Filter by author ID
- Bulk deletion support

### Comment System
- Full CRUD for comments
- Nested replies using `parentCommentId`
- Paginated by post or by user
- Bulk deletion support

### Reactions System
- Like or dislike blog posts
- Single reaction per user per post
- Ability to change or remove reaction
- Filter reactions by type (`LIKE`, `DISLIKE`)
- Pagination for reactions

### User Profiles
- View public user data by ID
- Retrieve posts and comments by a specific user

### Pagination & Sorting
- Consistent support for `page`, `size`, and `sort` query params
- Responses include total count, page number, total pages, etc.

## License
Licensed under the [MIT License](LICENSE).  
© 2025 Adam El Zahiri