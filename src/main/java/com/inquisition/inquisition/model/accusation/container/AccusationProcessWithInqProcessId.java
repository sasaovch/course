package com.inquisition.inquisition.model.accusation.container;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccusationProcessWithInqProcessId {
    @NotNull
    private Integer inquisitionProcessId;
}
