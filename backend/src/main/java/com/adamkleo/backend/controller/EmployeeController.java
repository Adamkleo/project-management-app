package com.adamkleo.backend.controller;

import com.adamkleo.backend.dto.EmployeeBasicDTO;
import com.adamkleo.backend.entity.Employee;
import com.adamkleo.backend.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getActiveEmployees() {
        return employeeService.getActiveEmployees();
    }

    @GetMapping("/paginated")
    public Page<Employee> getPaginatedActiveEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeService.getActiveEmployees(pageable);
    }


    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody @Valid Employee employee) {
        Employee saved = employeeService.addEmployee(employee);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Employee>> addEmployees(@RequestBody @Valid List<@Valid Employee> employees) {
        List<Employee> saved = employeeService.addEmployees(employees);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/terminate")
    public ResponseEntity<Void> terminateEmployee(@PathVariable Integer id) {
        employeeService.terminateEmployee(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/basic")
    public List<EmployeeBasicDTO> getBasicEmployees() {
        return employeeService.getAllBasicEmployees();
    }
}
