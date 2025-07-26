# Blog API
### What this project is
RESTful Blog API built with Java 17 and Spring Boot.  
It supports full CRUD functionality for blog posts, comments, reactions (likes/dislikes), and secure user authentication using JWT tokens. 
The API follows REST conventions and best practices to provide a scalable and well-structured backend.

### Why I built this project
This personal project was created to deepen my understanding of backend web development and demonstrate my ability to design secure, scalable APIs from scratch.  
It is the first REST API I’ve built entirely independently — without tutorials or external guidance.

## 📚 API Documentation
Interactive API documentation is available via Swagger UI:
**https://adampyramide.github.io/BlogAPI/**

## 🧪 Try the API
The API is hosted on Render — please note it may take a few seconds to respond to the first request due to server spin-up.

### 🔗 Base URL
All requests should use the following base URL:
**https://blogapi-vzsx.onrender.com/api/**

### 📥 Importing Endpoints
Import this OpenAPI document into tools like Postman or Insomnia:
**[docs/api-docs.json](docs/api-docs.json)**

### 🔐 Authentication
Some endpoints require a JWT token.
1. Register or log in to receive a token.
2. Include the token in your request headers: ```Authorization: Bearer YOUR_TOKEN_HERE```

Refer to the API docs for details on the login and registration endpoints.

## ⚙️ Features
### ✅ Secure User Authentication
- Register and login with username and password
- Passwords securely hashed
- JWT-based authentication

### 📝 Blog Post Management
- Full CRUD for blog posts
- Pagination (sorted by newest first)
- Filter by author ID
- Bulk deletion support

### 💬 Comment System
- Full CRUD for comments
- Nested replies using `parentCommentId`
- Paginated by post or by user
- Bulk deletion support

### 👍 Reactions System
- Like or dislike blog posts
- Single reaction per user per post
- Ability to change or remove reaction
- Filter reactions by type (`LIKE`, `DISLIKE`)
- Pagination for reactions

### 👤 User Profiles
- View public user data by ID
- Retrieve posts and comments by a specific user

### 🔄 Pagination & Sorting
- Consistent support for `page`, `size`, and `sort` query params
- Responses include total count, page number, total pages, etc.

## 🛠️ Technologies Used
- Java 17
- Spring Boot
- PostgreSQL
- Maven
- IntelliJ IDEA
- Postman

## 📄 License
Licensed under the [MIT License](LICENSE).  
© 2025 Adam El Zahiri