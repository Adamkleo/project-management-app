package com.adamkleo.backend.service;

import com.adamkleo.backend.entity.Employee;
import com.adamkleo.backend.exception.EmployeeAlreadyTerminatedException;
import com.adamkleo.backend.exception.EmployeeNotFoundException;
import com.adamkleo.backend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
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

        employee.setEndDate(LocalDate.now());
        employeeRepository.save(employee);
    }


}
