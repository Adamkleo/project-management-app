package com.adamkleo.backend.controller;

import com.adamkleo.backend.entity.Project;
import com.adamkleo.backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> getActiveProjects() {
        return projectService.getActiveProjects();
    }

    @PostMapping
    public ResponseEntity<Project> addProject(@RequestBody @Valid Project project) {
        Project saved = projectService.addProject(project);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/terminate")
    public ResponseEntity<Void> terminateProject(@PathVariable Integer id) {
        projectService.terminateProject(id);
        return ResponseEntity.ok().build();
    }
}
