# Blog API
A personal project I built to learn more about backend web development and show my experience and knowledge.

## API Documentation
API documentation for this project is available via Swagger UI.

**View the API docs here:**
https://adampyramide.github.io/BlogAPI/.

## Try the API
Hosted on render.com, expect windup time as I use the free host plan.

baseUrl: https://blogapi-vzsx.onrender.com/

Import this into Postman: [docs/api-docs.json](docs/api-docs.json)

Create an environment with "baseUrl" and "authToken"



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