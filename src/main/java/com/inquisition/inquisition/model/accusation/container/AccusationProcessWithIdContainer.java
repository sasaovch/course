package com.inquisition.inquisition.model.accusation.container;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccusationProcessWithIdContainer {
    @NotNull
    private Integer accusationId;
}
