package com.adamkleo.backend.repository;

import com.adamkleo.backend.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findAllByEndDateIsNull();

    Page<Employee> findAllByEndDateIsNull(Pageable pageable);

}
