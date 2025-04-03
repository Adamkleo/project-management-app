package com.adamkleo.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "PR_EMPLEADOS_PROYECTO")
public class ProjectAssignment {

    @EmbeddedId
    private AssignmentId id;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "ID_PROYECTO")
    private Project project;

    @ManyToOne
    @MapsId("employeeId")
    @JoinColumn(name = "ID_EMPLEADO")
    private Employee employee;

    @Column(name = "F_ALTA")
    private LocalDate assignmentDate;

    public ProjectAssignment() {}

    public ProjectAssignment(Project project, Employee employee, LocalDate assignmentDate) {
        this.id = new AssignmentId(project.getId(), employee.getId());
        this.project = project;
        this.employee = employee;
        this.assignmentDate = assignmentDate;
    }

    public AssignmentId getId() {
        return id;
    }

    public void setId(AssignmentId id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDate getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }
}
