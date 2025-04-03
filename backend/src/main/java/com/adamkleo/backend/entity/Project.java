package com.adamkleo.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "PR_PROYECTOS")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROYECTO")
    private Integer id;

    @Column(name = "TX_DESCRIPCIÓN", nullable = false, length = 125)
    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 125, message = "La descripción no puede tener más de 125 caracteres.")
    private String description;

    @Column(name = "F_INICIO", nullable = false)
    @NotNull(message = "La fecha de inicio es obligatoria.")
    @PastOrPresent(message = "La fecha de inicio no puede estar en el futuro.")
    private LocalDate startDate;

    @Column(name = "F_FIN")
    @PastOrPresent(message = "La fecha de fin no puede estar en el futuro.")
    private LocalDate endDate;

    @Column(name = "F_BAJA")
    private LocalDate terminationDate;

    @Column(name = "TX_LUGAR", length = 30)
    @Size(max = 30, message = "La ubicación no puede tener más de 30 caracteres.")
    private String location;

    @Column(name = "TX_OBSERVACIONES", length = 300)
    @Size(max = 300, message = "Las observaciones no pueden tener más de 300 caracteres.")
    private String observations;

    // Constructors
    public Project() {}

    public Project(String description, LocalDate startDate, LocalDate endDate, String location, String observations) {
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.observations = observations;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
