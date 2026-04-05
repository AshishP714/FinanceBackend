# Finance Data Processing and Access Control Backend

A backend REST API for a finance dashboard system built with Java Spring Boot and MySQL.
Supports role-based access control, financial record management, and dashboard analytics.

## Tech Stack
- Java 17
- Spring Boot 3.2
- Spring Security + JWT
- MySQL
- Maven

## Setup Instructions

### 1. Create Database
Open MySQL and run:
CREATE DATABASE finance_db;

### 2. Configure Database
Open src/main/resources/application.properties and update:
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD

### 3. Run the Project
./mvnw spring-boot:run

Server starts at: http://localhost:8080

## Default Users (Auto-Created on First Run)

Role     | Email                | Password
---------|----------------------|----------
Admin    | admin@finance.com    | password123
Analyst  | analyst@finance.com  | password123
Viewer   | viewer@finance.com   | password123

These users are automatically created on first startup for testing purposes.

## Role Permissions

Action                  | VIEWER | ANALYST | ADMIN
------------------------|--------|---------|------
View transactions       | YES    | YES     | YES
Create transaction      | NO     | NO      | YES
Update transaction      | NO     | NO      | YES
Delete transaction      | NO     | NO      | YES
View dashboard summary  | YES    | YES     | YES
View recent activity    | YES    | YES     | YES
View category breakdown | NO     | YES     | YES
View monthly trends     | NO     | YES     | YES
View weekly summary     | NO     | YES     | YES
Manage users            | NO     | NO      | YES

## API Endpoints

### Authentication
POST   /api/auth/register    - Register new user
POST   /api/auth/login       - Login and get token

### Transactions
POST   /api/transactions         - Create transaction (ADMIN only)
GET    /api/transactions         - Get all transactions (ALL roles)
GET    /api/transactions/{id}    - Get single transaction (ALL roles)
PUT    /api/transactions/{id}    - Update transaction (ADMIN only)
DELETE /api/transactions/{id}    - Soft delete (ADMIN only)

### Dashboard
GET    /api/dashboard/summary             - Total income, expenses, net (ALL)
GET    /api/dashboard/category-breakdown  - Per category totals (ADMIN, ANALYST)
GET    /api/dashboard/monthly-trends      - Monthly data (ADMIN, ANALYST)
GET    /api/dashboard/recent-activity     - Last 10 records (ALL)
GET    /api/dashboard/weekly-summary      - Last 7 days (ADMIN, ANALYST)

### Users (Admin Only)
GET    /api/users                - Get all users
GET    /api/users/{id}           - Get user by ID
PATCH  /api/users/{id}/role      - Change user role
PATCH  /api/users/{id}/status    - Activate or deactivate user
DELETE /api/users/{id}           - Delete user

## Testing with Postman

1. Call POST /api/auth/login with admin credentials
2. Copy the token from the response
3. Add this header to all other requests:
   Authorization: Bearer your_token_here

## Assumptions

- Default role on register is VIEWER
- Only ADMIN can create, update, delete transactions
- Deleted transactions are soft deleted, never removed from database
- JWT token expires in 24 hours
- Sample users are seeded automatically on first run for easy testing

## Tradeoffs

- Soft delete chosen over hard delete to preserve financial history
- JWT chosen over sessions for stateless authentication
- MySQL used for reliable relational data storage