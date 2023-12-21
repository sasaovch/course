package com.inquisition.inquisition.model.accusation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccusationProcessWithInqProcessId {
    @NotNull
    private Integer inquisitionProcessId;
}
