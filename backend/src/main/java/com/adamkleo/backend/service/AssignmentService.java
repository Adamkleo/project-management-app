package com.adamkleo.backend.service;

import com.adamkleo.backend.entity.Employee;
import com.adamkleo.backend.entity.Project;
import com.adamkleo.backend.entity.ProjectAssignment;
import com.adamkleo.backend.entity.AssignmentId;
import com.adamkleo.backend.exception.AssignmentAlreadyExistsException;
import com.adamkleo.backend.exception.AssignmentNotFoundException;
import com.adamkleo.backend.exception.EmployeeNotFoundException;
import com.adamkleo.backend.exception.ProjectNotFoundException;
import com.adamkleo.backend.repository.EmployeeRepository;
import com.adamkleo.backend.repository.ProjectAssignmentRepository;
import com.adamkleo.backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AssignmentService {

    private final ProjectAssignmentRepository assignmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    public AssignmentService(ProjectAssignmentRepository assignmentRepository,
                             EmployeeRepository employeeRepository,
                             ProjectRepository projectRepository) {
        this.assignmentRepository = assignmentRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
    }

    public void assignEmployeeToProject(Integer projectId, Integer employeeId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        AssignmentId id = new AssignmentId(projectId, employeeId);
        if (assignmentRepository.existsById(id)) {
            throw new AssignmentAlreadyExistsException(employeeId, projectId);
        }


        ProjectAssignment assignment = new ProjectAssignment(project, employee, LocalDate.now());
        assignmentRepository.save(assignment);
    }

    public void unassignEmployeeFromProject(Integer projectId, Integer employeeId) {
        AssignmentId id = new AssignmentId(projectId, employeeId);

        if (!assignmentRepository.existsById(id)) {
            throw new AssignmentNotFoundException(employeeId, projectId);
        }

        assignmentRepository.deleteById(id);
    }


    public List<ProjectAssignment> getAssignmentsByProject(Integer projectId) {
        return assignmentRepository.findByProjectId(projectId);
    }

    public List<ProjectAssignment> getAssignmentsByEmployee(Integer employeeId) {
        return assignmentRepository.findByEmployeeId(employeeId);
    }

    public boolean hasAssignedEmployees(Integer projectId) {
        return assignmentRepository.existsByProjectId(projectId);
    }

    public boolean isEmployeeAssignedToAnyProject(Integer employeeId) {
        return assignmentRepository.existsByEmployeeId(employeeId);
    }
}
