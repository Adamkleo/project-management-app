package com.adamkleo.backend.dto;

public class EmployeeBasicDTO {
    private Integer id;
    private String firstName;
    private String lastName1;
    private String lastName2;

    public EmployeeBasicDTO() {
    }

    public EmployeeBasicDTO(Integer id, String firstName, String lastName1, String lastName2) {
        this.id = id;
        this.firstName = firstName;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName1() {
        return lastName1;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }
}
