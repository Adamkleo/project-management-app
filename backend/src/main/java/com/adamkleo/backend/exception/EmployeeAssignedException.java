
package com.adamkleo.backend.exception;


public class EmployeeAssignedException extends RuntimeException {
    public EmployeeAssignedException(Integer id) {
        super("No se puede dar de baja el empleador con id \"" + id + "\" porque tiene proyectos asignados.");
    }
}
