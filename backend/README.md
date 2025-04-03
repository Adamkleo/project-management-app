# Backend - Project Management App

This directory contains the Java backend for employee and project management.

Java Spring Boot REST API for managing employees, projects, and their relationships.

---

## ✅ API Endpoints

### Employees

| Method | Endpoint                   | Description                |
|--------|----------------------------|----------------------------|
| GET    | `/employees`               | Get all active employees   |
| POST   | `/employees`               | Add a new employee         |
| POST   | `/employees/bulk`          | Add multiple employees     |
| PUT    | `/employees/{id}/terminate`| Terminate employee         |

### Projects

| Method | Endpoint                   | Description                |
|--------|----------------------------|----------------------------|
| GET    | `/projects`                | Get all active projects    |
| POST   | `/projects`                | Add a new project          |
| PUT    | `/projects/{id}/terminate` | Terminate project          |

### Assignments

| Method | Endpoint                                 | Description                      |
|--------|------------------------------------------|----------------------------------|
| GET    | `/assignments/project/{projectId}`       | Get assigned employees for project |
| GET    | `/assignments/employee/{employeeId}`     | Get projects for an employee     |
| POST   | `/assignments/{projectId}/assign/{employeeId}` | Assign employee to project |
| DELETE | `/assignments/{projectId}/unassign/{employeeId}` | Unassign employee from project |

---

## 🛠 Tech

- Spring Boot 3.4
- JPA (Hibernate)
- MySQL 8
- JUnit 5 + Mockito (test)
- Java 17

---

## ❌ Custom Exceptions

- `EmployeeNotFoundException`
- `EmployeeAlreadyTerminatedException`
- `ProjectNotFoundException`
- `ProjectAlreadyTerminatedException`
- `ProjectAssignedException`
- `AssignmentAlreadyExistsException`
- `AssignmentNotFoundException`

---

## ✅ Validation

Handled via:
- `jakarta.validation.constraints.*`
- Global exception handler (`@RestControllerAdvice`)

---

## 🔍 Test Coverage

Unit tests implemented with:
- `@Mock`, `@InjectMocks`, `MockitoExtension`
- `mvn test` or IntelliJ run

---

## ⚙️ Configuration

### application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/PRACTICA
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
