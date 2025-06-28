# REST API Documentation

## Open Endpoints
Open endpoints require no authentication.

### Auth
Endpoints related to user authentication

- [Register](auth/Register.md) `POST /api/register`
- [Login](auth/Login.md) `POST /api/login`

## Closed Endpoints
Closed enpoints require a valid token which can be fetched with the Login endpoint.
