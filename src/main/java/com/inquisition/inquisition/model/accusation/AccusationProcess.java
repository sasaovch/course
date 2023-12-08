package com.inquisition.inquisition.model.accusation;


import java.time.LocalDate;
import java.time.LocalDateTime;

import com.inquisition.inquisition.model.inquisition.InquisitionProcess;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

//@Entity(name = "accusation_process")
//public record AccusationProcess(
//        Integer id,
//        LocalDate startTime,
//        LocalDate finishTime,
//        Integer inquisitionProcessId
//) {}
//
@Data
public class AccusationProcess {
//
//    //    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//
////    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
//
////    @Column(name = "finish_time")
    private LocalDateTime finishTime;
//
////    @OneToOne
////    @JoinColumn(name = "inquisition_process_id", nullable = false)
    private Integer inquisitionProcessId;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public LocalDate getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(LocalDate startTime) {
//        this.startTime = startTime;
//    }
//
//    public LocalDate getFinishTime() {
//        return finishTime;
//    }
//
//    public void setFinishTime(LocalDate finishTime) {
//        this.finishTime = finishTime;
//    }
//
//    public InquisitionProcess getInquisitionProcess() {
//        return inquisitionProcess;
//    }
//
//    public void setInquisitionProcess(InquisitionProcess inquisitionProcess) {
//        this.inquisitionProcess = inquisitionProcess;
//    }
}
