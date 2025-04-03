# Database

This directory contains the SQL script used to create and initialize the **MySQL** database for the Project Management Application.

## Contents

- `mysql_DDL_PRACTICA.sql`: SQL script to create the schema, tables, and relationships used by the backend.

## Database Schema

The schema is defined under the `PRACTICA` schema and includes:

- `EM_EMPLEADOS`: Employee records.
- `PR_PROYECTOS`: Project records.
- `PR_EMPLEADOS_PROYECTO`: Join table representing a many-to-many relationship between employees and projects. It also includes an assignment date (`F_ALTA`).

Primary and foreign keys are explicitly defined.

### Relationships

- An employee can be assigned to **many projects**, and a project can have **many employees**.
- The assignment table enforces referential integrity with foreign keys pointing to the employee and project tables.

### Diagram

```mermaid
erDiagram
    EM_EMPLEADOS {
        INT ID_EMPLEADO PK
        VARCHAR TX_NIF
        VARCHAR TX_NOMBRE
        VARCHAR TX_APELLIDO1
        VARCHAR TX_APELLIDO2
        DATE F_NACIMIENTO
        VARCHAR N_TELEFONO1
        VARCHAR N_TELEFONO2
        VARCHAR TX_EMAIL
        DATE F_ALTA
        DATE F_BAJA
        CHAR CX_EDOCIVIL
        CHAR B_FORMACIONU
    }

    PR_PROYECTOS {
        INT ID_PROYECTO PK
        VARCHAR TX_DESCRIPCION
        DATE F_INICIO
        DATE F_FIN
        DATE F_BAJA
        VARCHAR TX_LUGAR
        VARCHAR TX_OBSERVACIONES
    }

    PR_EMPLEADOS_PROYECTO {
        INT ID_PROYECTO PK
        INT ID_EMPLEADO PK
        DATE F_ALTA
    }

    EM_EMPLEADOS ||--o{ PR_EMPLEADOS_PROYECTO : "ID_EMPLEADO"
    PR_PROYECTOS ||--o{ PR_EMPLEADOS_PROYECTO : "ID_PROYECTO"
```