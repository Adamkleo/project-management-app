package com.adamkleo.backend.exception;

public class EmployeeAlreadyTerminatedException extends RuntimeException {
    public EmployeeAlreadyTerminatedException(Integer id) {
        super("El empleado con ID " + id + " ya está dado de baja.");
    }
}
