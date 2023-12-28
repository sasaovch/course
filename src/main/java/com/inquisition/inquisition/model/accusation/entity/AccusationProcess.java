package com.inquisition.inquisition.model.accusation.entity;


import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AccusationProcess {
    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private Integer inquisitionProcessId;
}
