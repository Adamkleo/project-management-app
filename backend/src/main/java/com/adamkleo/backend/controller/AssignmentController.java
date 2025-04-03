package com.adamkleo.backend.controller;

import com.adamkleo.backend.entity.ProjectAssignment;
import com.adamkleo.backend.service.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/{projectId}/assign/{employeeId}")
    public ResponseEntity<Void> assignEmployee(@PathVariable Integer projectId, @PathVariable Integer employeeId) {
        assignmentService.assignEmployeeToProject(projectId, employeeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projectId}/unassign/{employeeId}")
    public ResponseEntity<Void> unassignEmployee(@PathVariable Integer projectId, @PathVariable Integer employeeId) {
        assignmentService.unassignEmployeeFromProject(projectId, employeeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ProjectAssignment>> getAssignmentsByProject(@PathVariable Integer projectId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByProject(projectId));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<ProjectAssignment>> getAssignmentsByEmployee(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(assignmentService.getAssignmentsByEmployee(employeeId));
    }
}
