package com.inquisition.inquisition.model.inquisition;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InquisitionProcessStartContainer {
    @NotNull
    private Integer bibleId;
    @NotNull
    private Integer localityId;
}
