package com.adamkleo.backend.service;

import com.adamkleo.backend.entity.Project;
import com.adamkleo.backend.exception.ProjectAlreadyTerminatedException;
import com.adamkleo.backend.exception.ProjectAssignedException;
import com.adamkleo.backend.exception.ProjectNotFoundException;
import com.adamkleo.backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AssignmentService assignmentService;

    public ProjectService(ProjectRepository projectRepository, AssignmentService assignmentService) {
        this.projectRepository = projectRepository;
        this.assignmentService = assignmentService;
    }


    public List<Project> getActiveProjects() {
        return projectRepository.findAll()
                .stream()
                .filter(p -> p.getTerminationDate() == null)
                .toList();
    }

    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    public void terminateProject(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));

        if (project.getTerminationDate() != null) {
            throw new ProjectAlreadyTerminatedException(id);
        }

        if (assignmentService.hasAssignedEmployees(id)) {
            throw new ProjectAssignedException(id, project.getDescription());
        }

        project.setTerminationDate(LocalDate.now());
        projectRepository.save(project);
    }

}
