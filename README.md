# Blog API
A personal project I built to deepen my backend web development skills and demonstrate my experience.

## API Documentation
Interactive API documentation is available via Swagger UI:
https://adampyramide.github.io/BlogAPI/.

## Try the API
You can test the Blog REST API using tools like Postman or curl. Hosted on render.com, **expect windup time on first request.**

**Base URL**

All requests should use the following base URL:
https://blogapi-vzsx.onrender.com

**Importing Endpoints**

You can import all API endpoints using the OpenAPI document:
[docs/api-docs.json](docs/api-docs.json)

**Authentication**

Some routes require a JWT token for authentication. You can get a token by registering or logging in. These return a JWT token. You’ll need to include this token in the Authorization header for any protected routes.

Refer to API documentation for the register and login endpoints.

## Utilized technologies
**Project**
- Java (Maven)
- Spring Boot
- MySQL

**Development**
- Intellij IDEA Community
- Postman

## Features
- Secure user registration with password hashing 
- User authentication using JWT tokens 
- Full CRUD operations for blog posts (Create, Read, Update, Delete)
- Paginated retrieval of all blog posts 
- Paginated retrieval of blog posts authored by a specific user 
- Full CRUD operations for comments on blog posts, including bulk deletion 
- Paginated retrieval of comments on a specific blog post 
- Paginated retrieval of comments made by a specific user 
- Fetch user profile data by user ID