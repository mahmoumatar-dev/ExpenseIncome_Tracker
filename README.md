# Expense & Income Tracker API

A **Spring Boot RESTful backend application** for managing family finances.
The system allows **parents to control and monitor spending**, while **children can perform limited expense transactions** under predefined budgets and limits.

The platform also includes a powerful **Admin Dashboard** that provides analytics and system monitoring.

---

# Project Overview

The **Expense & Income Tracker** is designed to help families manage their finances in a structured way.

The system introduces **role-based financial management**:

* **Parents** manage wallets, categories, budgets, and child accounts.
* **Children** can perform controlled expense transactions.
* **Admins** monitor system activity and analytics.

The backend is built using **Spring Boot with a layered architecture** and implements **secure authentication using JWT**.

---

# Key Features

## Authentication & Security

* JWT-based authentication
* Role-based authorization
* Secure endpoints for Admin, Parent, and Child users

---

## Parent Features

Parents can:

* Create and manage **wallets**
* Assign wallets to children
* Create **expense categories**
* Define **monthly budgets** for categories
* Create and manage **child accounts**
* Set **monthly spending limits** for children
* Suspend child accounts
* Perform **income and expense transactions**

---

## Child Features

Children can:

* View wallets assigned by their parent
* Perform **expense transactions only**
* Select a category when making a transaction
* Spend only within:

  * wallet balance
  * category budget
  * personal monthly limit

---

## Admin Features

Admins have full system visibility and can:

* View all users with pagination
* Suspend user accounts
* Delete user accounts
* View all transactions in the system

---

## Admin Dashboard Analytics

The Admin Dashboard provides powerful analytics such as:

* Total number of users
* Number of active parents and children
* Total transaction amount
* User distribution (parents vs children)
* Recent user registrations
* Monthly transaction volume
* Monthly user registrations

---

# System Architecture

The project follows a **clean layered architecture**:

```
Controller Layer
        ↓
Service Layer
        ↓
Repository Layer
        ↓
Database
```

Main modules:

* Controllers (REST APIs)
* Services (Business logic)
* Repositories (Data access)
* DTOs (Data transfer objects)
* Entities (Database models)
* Security (JWT authentication)

---

# Technologies Used

* Java 17
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* JWT Authentication
* Maven
* MySQL / PostgreSQL
* Lombok

---

# Database Design

Main entities:

* **User**
* **Wallet**
* **Transaction**
* **Category**
* **Budget**

Relationships include:

* Parent → Children
* User → Wallets
* User → Transactions
* Category → Budget

---

# API Examples

## Authentication

```
POST /api/auth/login
POST /api/auth/register
```

---

## Parent APIs

```
POST /api/parent/wallets
POST /api/parent/categories
POST /api/parent/transactions
POST /api/parent/children
```

---

## Child APIs

```
POST /api/child/transactions
GET /api/child/wallets
```

---

## Admin APIs

```
GET /api/admin/users
PATCH /api/admin/users/{id}/suspend
DELETE /api/admin/users/{id}
GET /api/admin/transactions
```

---

## Admin Analytics APIs

```
GET /api/admin/analytics/dashboard
GET /api/admin/analytics/user-distribution
GET /api/admin/analytics/recent-users
GET /api/admin/analytics/transactions-volume
GET /api/admin/analytics/user-registrations
```

---

# Running the Project

### 1 Clone the repository

```
git clone https://github.com/your-username/expense-income-tracker.git
```

### 2 Navigate to project directory

```
cd expense-income-tracker
```

### 3 Configure database

Update the `application.properties` file:

```
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
```

---

### 4 Run the application

```
mvn spring-boot:run
```

The server will start at:

```
http://localhost:8080
```

---

# Future Improvements

Possible enhancements:

* Redis caching
* Email notifications
* Real-time analytics dashboard
* Mobile application integration
* Microservices architecture

---

# Author

**Mahmoud Mohamed Matar**

Backend Developer | Java & Spring Boot
