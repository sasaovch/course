package com.inquisition.inquisition.model.cases.entity;

import java.time.LocalDateTime;

import com.inquisition.inquisition.models.enums.CaseLogResult;
import com.inquisition.inquisition.models.enums.CaseLogStatus;
import lombok.Data;

@Data
public class CaseLog {
    private Integer id;
    private Integer caseId;
    private CaseLogStatus status;
    private Integer principal;
    private LocalDateTime startTime;
    private CaseLogResult result;
    private Integer prisonId;
    private LocalDateTime finishTime;
    private Integer punishmentId;
    private String description;
}
