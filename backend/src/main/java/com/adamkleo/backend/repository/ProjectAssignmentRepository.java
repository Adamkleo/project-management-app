package com.adamkleo.backend.repository;

import com.adamkleo.backend.entity.AssignmentId;
import com.adamkleo.backend.entity.ProjectAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectAssignmentRepository extends JpaRepository<ProjectAssignment, AssignmentId> {

    List<ProjectAssignment> findByProjectId(Integer projectId);

    List<ProjectAssignment> findByEmployeeId(Integer employeeId);

    @Query("SELECT pa FROM ProjectAssignment pa WHERE pa.project.terminationDate IS NULL")
    List<ProjectAssignment> findAssignmentsOfActiveProjects();

    boolean existsByProjectId(Integer projectId);

    boolean existsByEmployeeId(Integer employeeId);



}
