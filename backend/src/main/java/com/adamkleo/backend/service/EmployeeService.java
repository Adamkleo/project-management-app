package com.adamkleo.backend.service;

import com.adamkleo.backend.dto.EmployeeBasicDTO;
import com.adamkleo.backend.entity.Employee;
import com.adamkleo.backend.exception.EmployeeAlreadyTerminatedException;
import com.adamkleo.backend.exception.EmployeeAssignedException;
import com.adamkleo.backend.exception.EmployeeNotFoundException;
import com.adamkleo.backend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AssignmentService assignmentService;

    public EmployeeService(EmployeeRepository employeeRepository, AssignmentService assignmentService) {
        this.employeeRepository = employeeRepository;
        this.assignmentService = assignmentService;
    }

    public List<Employee> getActiveEmployees() {
        return employeeRepository.findAllByEndDateIsNull();
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> addEmployees(List<Employee> employees) {
        return employeeRepository.saveAll(employees);
    }

    public void terminateEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        if (employee.getEndDate() != null) {
            throw new EmployeeAlreadyTerminatedException(id);
        }

        if (assignmentService.hasAssignedProject(id)) {
            throw new EmployeeAssignedException(id);
        }

        employee.setEndDate(LocalDate.now());
        employeeRepository.save(employee);
    }

    public EmployeeBasicDTO toBasicDto(Employee e) {
        return new EmployeeBasicDTO(
                e.getId(),
                e.getFirstName(),
                e.getLastName1(),
                e.getLastName2()
        );
    }

    public List<EmployeeBasicDTO> getAllBasicEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeBasicDTO> basicDTOs = new ArrayList<>();

        for (Employee e : employees) {
            basicDTOs.add(toBasicDto(e));
        }

        return basicDTOs;
    }


    public Page<Employee> getActiveEmployees(Pageable pageable) {
        return employeeRepository.findAllByEndDateIsNull(pageable);
    }


}
