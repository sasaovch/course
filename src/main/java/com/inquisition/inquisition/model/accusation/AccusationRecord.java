package com.inquisition.inquisition.model.accusation;

import java.time.LocalDate;

import com.inquisition.inquisition.model.official.Official;
import com.inquisition.inquisition.model.person.Person;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "accusation_record")
public class AccusationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "informer")
    private Person informer;

    @ManyToOne
    @JoinColumn(name = "bishop", nullable = false)
    private Official bishop;

    @ManyToOne
    @JoinColumn(name = "accused", nullable = false)
    private Person accused;

    @Column(name = "violation_place")
    private String violationPlace;

    @Column(name = "violation_time", nullable = false)
    private LocalDate violationTime;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_accusation", nullable = false)
    private AccusationProcess accusationProcess;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccusationStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getInformer() {
        return informer;
    }

    public void setInformer(Person informer) {
        this.informer = informer;
    }

    public Official getBishop() {
        return bishop;
    }

    public void setBishop(Official bishop) {
        this.bishop = bishop;
    }

    public Person getAccused() {
        return accused;
    }

    public void setAccused(Person accused) {
        this.accused = accused;
    }

    public String getViolationPlace() {
        return violationPlace;
    }

    public void setViolationPlace(String violationPlace) {
        this.violationPlace = violationPlace;
    }

    public LocalDate getViolationTime() {
        return violationTime;
    }

    public void setViolationTime(LocalDate violationTime) {
        this.violationTime = violationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccusationProcess getAccusationProcess() {
        return accusationProcess;
    }

    public void setAccusationProcess(AccusationProcess accusationProcess) {
        this.accusationProcess = accusationProcess;
    }

    public AccusationStatus getStatus() {
        return status;
    }

    public void setStatus(AccusationStatus status) {
        this.status = status;
    }
}
