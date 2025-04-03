package com.adamkleo.backend.repository;

import com.adamkleo.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    // Custom queries will go here if needed later
}
