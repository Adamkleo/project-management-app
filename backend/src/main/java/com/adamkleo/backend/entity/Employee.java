package com.adamkleo.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;


@Entity
@Table(name = "EM_EMPLEADOS", schema = "PRACTICA")
public class Employee {

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_EMPLEADO")
    private Integer id;


    // NIF
    @Column(name = "TX_NIF")
    @Pattern(
            regexp = "^[0-9]{8}[A-HJ-NP-TV-Z]$",
            message = "El NIF debe tener 8 números seguidos de una letra válida."
    )
    private String nif;

    // First name
    @Column(name = "TX_NOMBRE", nullable = false)
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+$", message = "El nombre solo puede contener letras y espacios.")
    @Size(min = 3, max = 40, message = "El nombre debe tener entre 3 y 40 caracteres.")
    private String firstName;

    // first last name
    @Column(name = "TX_APELLIDO1", nullable = false)
    @NotBlank(message = "El apellido no puede estar vacío.")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+$", message = "El apellido solo puede contener letras y espacios.")
    @Size(min = 3, max = 40, message = "El apellido debe tener entre 3 y 40 caracteres.")
    private String lastName1;


    // second last name
    @Column(name = "TX_APELLIDO2", nullable = false)
    @NotBlank(message = "El apellido no puede estar vacío.")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]+$", message = "El apellido solo puede contener letras y espacios.")
    @Size(min = 3, max = 40, message = "El apellido debe tener entre 3 y 40 caracteres.")
    private String lastName2;


    // birthdate
    @Column(name = "F_NACIMIENTO")
    @Past(message = "La fecha de nacimiento debe estar en el pasado.")
    private LocalDate birthDate;


    // Phone 1
    @Column(name = "N_TELEFONO1", nullable = false)
    @NotBlank(message = "El teléfono principal no puede estar vacío.")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "El teléfono principal debe contener entre 7 y 15 dígitos.")
    private String phone1;

    // Phone 2
    @Column(name = "N_TELEFONO2", nullable = false)
    @NotBlank(message = "El teléfono secundario no puede estar vacío.")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "El teléfono secundario debe contener entre 7 y 15 dígitos.")
    private String phone2;

    // Email
    @Column(name = "TX_EMAIL", nullable = false)
    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "El email no es válido.")
    private String email;

    // Registration date
    @Column(name = "F_ALTA", nullable = false)
    @PastOrPresent(message = "La fecha de alta no puede estar en el futuro.")
    private LocalDate startDate;

    // End date (nullable)
    @Column(name = "F_BAJA")
    @PastOrPresent(message = "La fecha de baja no puede estar en el futuro.")
    private LocalDate endDate;

    // Civil status
    @Column(name = "CX_EDOCIVIL", nullable = false, length = 1)
    @NotNull(message = "El estado civil es obligatorio.")
    private Character civilStatus;

    // Has university education
    @Column(name = "B_FORMACIONU", nullable = false, length = 1)
    @NotNull(message = "Debe indicar si tiene formación universitaria.")
    private Character universityEducation;


    public Employee(String nif, LocalDate birthDate, Character civilStatus, String email, LocalDate endDate, String firstName, String lastName1, String lastName2, String phone1, String phone2, LocalDate startDate, Character universityEducation) {
        this.nif = nif;
        this.birthDate = birthDate;
        this.civilStatus = civilStatus;
        this.email = email;
        this.endDate = endDate;
        this.firstName = firstName;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.startDate = startDate;
        this.universityEducation = universityEducation;
    }


    public Employee() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Character getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(Character civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Character getUniversityEducation() {
        return universityEducation;
    }

    public void setUniversityEducation(Character universityEducation) {
        this.universityEducation = universityEducation;
    }


    @Override
    public String toString() {
        return "Employee{" +
                "birthDate=" + birthDate +
                ", id=" + id +
                ", nif='" + nif + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName1='" + lastName1 + '\'' +
                ", lastName2='" + lastName2 + '\'' +
                ", phone1='" + phone1 + '\'' +
                ", phone2='" + phone2 + '\'' +
                ", email='" + email + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", civilStatus=" + civilStatus +
                ", universityEducation=" + universityEducation +
                '}';
    }
}