package com.adamkleo.backend.exception;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(Integer id) {
        super("Proyecto no encontrado con ID: " + id);
    }
}
