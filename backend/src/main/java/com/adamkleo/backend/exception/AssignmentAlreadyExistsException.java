package com.adamkleo.backend.exception;

public class AssignmentAlreadyExistsException extends RuntimeException {
    public AssignmentAlreadyExistsException(Integer employeeId, Integer projectId) {
        super("El empleado con ID " + employeeId + " ya está asignado al proyecto con ID " + projectId + ".");
    }
}
