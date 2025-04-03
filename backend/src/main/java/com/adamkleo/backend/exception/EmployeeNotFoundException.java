package com.adamkleo.backend.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Integer id) {
        super("Empleado no encontrado con ID: " + id);
    }
}
