package com.adamkleo.backend.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AssignmentId implements Serializable {

    private Integer projectId;
    private Integer employeeId;

    public AssignmentId() {}

    public AssignmentId(Integer projectId, Integer employeeId) {
        this.projectId = projectId;
        this.employeeId = employeeId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssignmentId that)) return false;
        return Objects.equals(projectId, that.projectId) && Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, employeeId);
    }
}
