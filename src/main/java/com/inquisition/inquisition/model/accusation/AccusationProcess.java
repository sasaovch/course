package com.inquisition.inquisition.model.accusation;


import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AccusationProcess {
    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private Integer inquisitionProcessId;
}
