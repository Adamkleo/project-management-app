package com.adamkleo.backend.exception;

public class ProjectAlreadyTerminatedException extends RuntimeException {
    public ProjectAlreadyTerminatedException(Integer id) {
        super("El proyecto con ID " + id + " ya está dado de baja.");
    }
}
