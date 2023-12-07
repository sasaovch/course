package com.inquisition.inquisition.model.accusation;


import java.time.LocalDate;

import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity(name = "accusation_process")
public class AccusationProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "start_time", nullable = false)
    private LocalDate startTime;

    @Column(name = "finish_time")
    private LocalDate finishTime;

    @OneToOne
    @JoinColumn(name = "inquisition_process_id", nullable = false)
    private InquisitionProcess inquisitionProcess;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDate finishTime) {
        this.finishTime = finishTime;
    }

    public InquisitionProcess getInquisitionProcess() {
        return inquisitionProcess;
    }

    public void setInquisitionProcess(InquisitionProcess inquisitionProcess) {
        this.inquisitionProcess = inquisitionProcess;
    }
}
