package com.adamkleo.backend.exception;

public class AssignmentNotFoundException extends RuntimeException {
    public AssignmentNotFoundException(Integer employeeId, Integer projectId) {
        super("El empleado con ID " + employeeId + " no está asignado al proyecto con ID " + projectId + ".");
    }
}
