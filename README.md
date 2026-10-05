# Enterprise ERP System

A full-stack **Enterprise Resource Planning (ERP) System** built with **Spring Boot, React, PostgreSQL, JWT Security, Docker, and Cloud Deployment**.

The system provides role-based management for **HR, Employees, Inventory, Sales, Accounting, Reporting, Authentication, and Audit Logging** through a centralized web application.

---

## 🚀 Live Application

**Frontend:**
https://enterprise-erp-system-frontend.vercel.app

**Backend API:**
https://enterprise-erp-system.onrender.com

**Health Check:**
https://enterprise-erp-system.onrender.com/actuator/health

---

## 📌 Project Overview

The Enterprise ERP System is designed to simulate a real-world enterprise management platform where different departments can manage their operations from a single application.

The application implements:

* Secure JWT-based authentication
* Role-based authorization
* Employee and department management
* Attendance and leave management
* Product and inventory management
* Customer management
* Sales order processing
* Stock management
* Accounting and financial transactions
* Reports and dashboards
* Audit logging
* Production database integration
* RESTful APIs
* Cloud deployment

---

## 🏗️ System Architecture

```text
                    ┌─────────────────────────┐
                    │       React Frontend    │
                    │        Vercel           │
                    └────────────┬────────────┘
                                 │
                                 │ REST API / JSON
                                 ▼
                    ┌─────────────────────────┐
                    │    Spring Boot Backend  │
                    │        Render           │
                    └────────────┬────────────┘
                                 │
                  ┌──────────────┴──────────────┐
                  │                             │
                  ▼                             ▼
        ┌──────────────────┐          ┌──────────────────┐
        │ JWT Authentication│          │ Business Modules │
        │ Spring Security  │          │ HR / Sales /     │
        │                  │          │ Inventory /      │
        └──────────────────┘          │ Accounting       │
                                      └────────┬─────────┘
                                               │
                                               ▼
                                  ┌────────────────────────┐
                                  │ PostgreSQL Database     │
                                  │         Neon            │
                                  └────────────────────────┘
```

---

# ✨ Key Features

## 🔐 Authentication & Security

* User registration and login
* JWT-based authentication
* Password encryption using BCrypt
* Role-based access control
* Active/inactive user management
* Employee login creation
* Protected REST APIs
* CORS configuration for production frontend
* Security configuration using Spring Security

### Supported Roles

| Role              | Access                              |
| ----------------- | ----------------------------------- |
| ADMIN             | Full system access                  |
| HR_ADMIN          | HR and employee management          |
| SALES_MANAGER     | Customers and sales management      |
| ACCOUNTANT        | Accounting and financial management |
| INVENTORY_MANAGER | Products and inventory management   |
| EMPLOYEE          | Employee self-service               |

---

# 👥 HR Management

The HR module provides:

* Employee management
* Department management
* Employee login creation
* Attendance tracking
* Leave management
* Employee self-service
* Role-based HR access

### Employee Management

HR administrators can:

* Create employees
* Update employee information
* Delete employees
* Assign departments
* View employee details

---

# 📦 Inventory Management

The inventory module provides:

* Product management
* Product categories
* Supplier management
* Stock tracking
* Stock transactions
* Reorder level management
* Automatic stock reduction during sales
* Stock restoration when orders are updated/deleted

The system prevents sales when available inventory is insufficient.

Example:

```text
Available Stock: 5
Requested Quantity: 6

Result:
Insufficient stock
```

---

# 🛒 Sales Management

The sales module includes:

* Customer management
* Sales order creation
* Sales order updates
* Sales order deletion
* Order item management
* Automatic total calculation
* Stock validation
* Automatic inventory deduction
* Stock restoration
* Payment/order status management

Example order flow:

```text
Customer
   ↓
Sales Order
   ↓
Order Items
   ↓
Stock Validation
   ↓
Inventory Deduction
   ↓
Order Confirmation
```

---

# 💰 Accounting

The accounting module provides:

* Account management
* Financial transactions
* Credit/debit tracking
* Account balances
* Accounting summary reports

### Example

```text
Total Credit: ₹50,000
Total Debit:  ₹0
Net Balance:  ₹50,000
```

---

# 📊 Dashboard & Reporting

Role-specific dashboards provide relevant business information.

Examples include:

* Employee statistics
* Department information
* Inventory/product information
* Sales statistics
* Financial summaries
* Business reports

Dashboard content is dynamically controlled based on the authenticated user's role.

---

# 📝 Audit Logging

The system maintains audit information for important operations.

Audit logs help track:

* User actions
* Business operations
* System activities
* Important data changes

This provides better traceability and supports enterprise compliance requirements.

---

# 🛡️ API Security

The backend uses:

* Spring Security
* JWT authentication
* BCrypt password hashing
* Role-based endpoint authorization
* CORS configuration
* Protected REST endpoints

Example:

```text
POST /api/auth/login
POST /api/auth/register

GET  /api/hr/employees
POST /api/hr/employees

GET  /api/sales/customers
POST /api/sales/customers

GET  /api/accounting/reports/summary
```

---

# 🛠️ Technology Stack

## Backend

* Java 17
* Spring Boot 3.4.5
* Spring Security
* Spring Data JPA
* Hibernate
* REST APIs
* JWT
* Maven
* PostgreSQL
* Spring Boot Actuator

## Frontend

* React
* JavaScript
* Vite
* Axios
* HTML5
* CSS3

## Database

* PostgreSQL
* Neon PostgreSQL

## DevOps & Deployment

* Git
* GitHub
* Docker
* Render
* Vercel

---

# 📁 Project Structure

```text
enterprise-erp-system/
│
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com/
│   │       │       └── erp/
│   │       │
│   │       └── resources/
│   │           └── application.yml
│   │
│   ├── Dockerfile
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   └── App.jsx
│   │
│   ├── package.json
│   └── vite.config.js
│
├── docker-compose.yml
├── SECURITY.md
├── COMPLIANCE.md
└── README.md
```

---

# 🗄️ Database Modules

The PostgreSQL database contains the following major tables:

```text
users
employees
departments
attendance
leaves

categories
products
suppliers
stock_transactions

customers
sales_orders
sales_order_items

accounts
financial_transactions

audit_logs
```

The database uses foreign-key relationships to maintain data integrity between modules.

---

# 🔄 Data Relationships

```text
Departments
     │
     ▼
Employees
     │
     ├── Attendance
     ├── Leaves
     └── Employee Login

Categories
     │
     ▼
Products
     │
     └── Stock Transactions
             │
             └── Suppliers

Customers
     │
     ▼
Sales Orders
     │
     ▼
Sales Order Items
     │
     ▼
Products

Accounts
     │
     ▼
Financial Transactions
```

---

# ⚙️ Local Development

## Prerequisites

Install the following:

* Java 17+
* Maven
* Node.js
* npm
* PostgreSQL
* Git

---

## Backend Setup

Navigate to the backend:

```bash
cd backend
```

Configure database and JWT environment variables:

```text
DB_URL=jdbc:postgresql://localhost:5432/erpdb
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_secret
```

Run the backend:

```bash
mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

Health check:

```text
http://localhost:8080/actuator/health
```

---

## Frontend Setup

Navigate to frontend:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Start development server:

```bash
npm run dev
```

Frontend runs on:

```text
http://localhost:5173
```

---

# 🔑 Environment Variables

For security, sensitive credentials are **not stored in the GitHub repository**.

Backend:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

Frontend:

```text
VITE_API_BASE_URL
```

Example:

```text
VITE_API_BASE_URL=https://enterprise-erp-system.onrender.com/api
```

Never commit:

```text
.env
.env.local
passwords
database credentials
JWT secrets
API keys
```

---

# 🐳 Docker

The backend includes a Dockerfile for containerized deployment.

Build:

```bash
docker build -t enterprise-erp .
```

Run:

```bash
docker run -p 8080:8080 enterprise-erp
```

---

# ☁️ Production Deployment

The application is deployed using a cloud-based architecture.

### Frontend

```text
React + Vite
       ↓
     Vercel
```

### Backend

```text
Spring Boot
     ↓
   Docker
     ↓
   Render
```

### Database

```text
PostgreSQL
     ↓
    Neon
```

---

# 🔍 Monitoring

Spring Boot Actuator is enabled for application monitoring.

Health endpoint:

```text
/actuator/health
```

Additional monitoring endpoints include:

```text
/actuator/info
/actuator/metrics
```

---

# 🧪 Testing

The project can be tested using:

* Browser-based frontend testing
* REST API testing
* Authentication testing
* Role authorization testing
* CRUD operation testing
* Inventory stock validation
* Sales order testing
* Accounting report testing
* Production deployment testing

Important scenarios tested include:

```text
✓ User registration
✓ User login
✓ JWT authentication
✓ Role-based authorization
✓ Employee CRUD
✓ Department management
✓ Attendance
✓ Leave management
✓ Product CRUD
✓ Customer CRUD
✓ Sales orders
✓ Stock deduction
✓ Stock validation
✓ Stock restoration
✓ Accounting transactions
✓ Financial reports
✓ Audit logging
✓ Production database
✓ Frontend-backend communication
```

---

# 📈 Production Database

The production application uses **Neon PostgreSQL**.

The development PostgreSQL database was migrated to the production database while preserving:

* Users
* Employees
* Departments
* Products
* Categories
* Suppliers
* Customers
* Sales Orders
* Sales Order Items
* Stock Transactions
* Accounts
* Financial Transactions
* Attendance
* Leaves
* Audit Logs

---

# 🔒 Security

Security considerations are documented in:

```text
SECURITY.md
```

The application follows secure practices including:

* Password hashing
* JWT authentication
* Environment-based secrets
* Role-based authorization
* CORS configuration
* Protected API endpoints
* No database credentials committed to Git

---

# 📋 Compliance

Enterprise-related compliance considerations are documented in:

```text
COMPLIANCE.md
```

The system includes mechanisms supporting:

* Auditability
* Access control
* Data integrity
* User accountability
* Secure credential handling

---

# 🎯 Project Objectives

This project demonstrates practical implementation of:

* Enterprise application architecture
* Full-stack development
* REST API development
* Spring Boot
* Spring Security
* JWT authentication
* Database design
* JPA/Hibernate
* Role-based authorization
* React frontend development
* Cloud deployment
* Docker
* CI/CD-ready project structure
* Monitoring
* Audit logging
* Enterprise data management

---

# 🚀 Future Enhancements

Potential future improvements include:

* Advanced analytics dashboard
* Real-time notifications
* WebSocket/SSE integration
* Elasticsearch integration
* Machine-learning based predictions
* Email notifications
* Advanced reporting
* Redis caching optimization
* Kubernetes deployment
* Automated CI/CD pipelines
* Mobile application

---

# 👨‍💻 Author

**Anjum Patel**

Full-Stack / Backend Developer

Technologies:

```text
Java
Spring Boot
Spring Security
JWT
REST APIs
React
PostgreSQL
Docker
Git
GitHub
Cloud Deployment
```

---

## ⭐ If you find this project useful

Feel free to explore the repository, review the architecture, and provide feedback.

**Enterprise ERP System — Full Stack Enterprise Application**
