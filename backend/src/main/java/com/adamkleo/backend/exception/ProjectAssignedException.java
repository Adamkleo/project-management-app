package com.adamkleo.backend.exception;

public class ProjectAssignedException extends RuntimeException {
    public ProjectAssignedException(Integer id, String description) {
        super("No se puede dar de baja el proyecto \"" + description + "\" porque tiene empleados asignados.");
    }
}
